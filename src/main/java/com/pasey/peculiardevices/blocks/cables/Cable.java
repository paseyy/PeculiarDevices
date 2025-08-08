// Credit to McJty https://github.com/McJty/Tut4_3Power/blob/main/src/main/java/com/mcjty/tutpower/cables/blocks/CableBlock.java (08/08/2025)
//MIT License
//
//        Copyright (c) 2023 McJty
//
//        Permission is hereby granted, free of charge, to any person obtaining a copy
//        of this software and associated documentation files (the "Software"), to deal
//        in the Software without restriction, including without limitation the rights
//        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//        copies of the Software, and to permit persons to whom the Software is
//        furnished to do so, subject to the following conditions:
//
//        The above copyright notice and this permission notice shall be included in all
//        copies or substantial portions of the Software.
//
//        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//        SOFTWARE.

package com.pasey.peculiardevices.blocks.cables;

import com.pasey.peculiardevices.blockentities.cables.CableBlockEntity;
import com.pasey.peculiardevices.blockentities.util.CreateTickerHelper;
import com.pasey.peculiardevices.registration.PDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class Cable extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<ConnectorType> NORTH = EnumProperty.create("north", ConnectorType.class);
    public static final EnumProperty<ConnectorType> SOUTH = EnumProperty.create("south", ConnectorType.class);
    public static final EnumProperty<ConnectorType> WEST = EnumProperty.create("west", ConnectorType.class);
    public static final EnumProperty<ConnectorType> EAST = EnumProperty.create("east", ConnectorType.class);
    public static final EnumProperty<ConnectorType> UP = EnumProperty.create("up", ConnectorType.class);
    public static final EnumProperty<ConnectorType> DOWN = EnumProperty.create("down", ConnectorType.class);

    private static VoxelShape[] shapeCache = null;

    private static final VoxelShape SHAPE_CABLE_NORTH = Shapes.box(.3, .3, 0, .7, .7, .3);
    private static final VoxelShape SHAPE_CABLE_SOUTH = Shapes.box(.3, .3, .7, .7, .7, 1);
    private static final VoxelShape SHAPE_CABLE_WEST = Shapes.box(0, .3, .3, .3, .7, .7);
    private static final VoxelShape SHAPE_CABLE_EAST = Shapes.box(.7, .3, .3, 1, .7, .7);
    private static final VoxelShape SHAPE_CABLE_UP = Shapes.box(.3, .7, .3, .7, 1, .7);
    private static final VoxelShape SHAPE_CABLE_DOWN = Shapes.box(.3, 0, .3, .7, .3, .7);

    private static final VoxelShape SHAPE_BLOCK_NORTH = Shapes.box(.25, .25, 0, .75, .75, .2);
    private static final VoxelShape SHAPE_BLOCK_SOUTH = Shapes.box(.25, .25, .8, .75, .75, 1);
    private static final VoxelShape SHAPE_BLOCK_WEST = Shapes.box(0, .25, .25, .2, .75, .75);
    private static final VoxelShape SHAPE_BLOCK_EAST = Shapes.box(.8, .25, .25, 1, .75, .75);
    private static final VoxelShape SHAPE_BLOCK_UP = Shapes.box(.25, .8, .25, .75, 1, .75);
    private static final VoxelShape SHAPE_BLOCK_DOWN = Shapes.box(.25, 0, .25, .75, .2, .75);


    public Cable() {
        super(Properties.of()
                .strength(1.0f)
                .sound(SoundType.METAL)
                .noOcclusion());

        makeShapes();
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Nullable
    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CableBlockEntity(pPos, pState);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return CreateTickerHelper.createTickerHelper(pBlockEntityType, PDBlockEntities.CABLE_BE.get());
    }

    private int calculateShapeIndex(ConnectorType north, ConnectorType south, ConnectorType west, ConnectorType east, ConnectorType up, ConnectorType down) {
        int l = ConnectorType.values().length;
        return ((((south.ordinal() * l + north.ordinal()) * l + west.ordinal()) * l + east.ordinal()) * l + up.ordinal()) * l + down.ordinal();
    }

    private void makeShapes() {
        if (shapeCache == null) {
            int length = ConnectorType.values().length;
            shapeCache = new VoxelShape[length * length * length * length * length * length];

            for (ConnectorType up : ConnectorType.VALUES) {
                for (ConnectorType down : ConnectorType.VALUES) {
                    for (ConnectorType north : ConnectorType.VALUES) {
                        for (ConnectorType south : ConnectorType.VALUES) {
                            for (ConnectorType east : ConnectorType.VALUES) {
                                for (ConnectorType west : ConnectorType.VALUES) {
                                    int idx = calculateShapeIndex(north, south, west, east, up, down);
                                    shapeCache[idx] = makeShape(north, south, west, east, up, down);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private VoxelShape makeShape(ConnectorType north, ConnectorType south, ConnectorType west, ConnectorType east, ConnectorType up, ConnectorType down) {
        VoxelShape shape = Shapes.box(.3, .3, .3, .7, .7, .7);
        shape = combineShape(shape, north, SHAPE_CABLE_NORTH, SHAPE_BLOCK_NORTH);
        shape = combineShape(shape, south, SHAPE_CABLE_SOUTH, SHAPE_BLOCK_SOUTH);
        shape = combineShape(shape, west, SHAPE_CABLE_WEST, SHAPE_BLOCK_WEST);
        shape = combineShape(shape, east, SHAPE_CABLE_EAST, SHAPE_BLOCK_EAST);
        shape = combineShape(shape, up, SHAPE_CABLE_UP, SHAPE_BLOCK_UP);
        shape = combineShape(shape, down, SHAPE_CABLE_DOWN, SHAPE_BLOCK_DOWN);
        return shape;
    }

    private VoxelShape combineShape(VoxelShape shape, ConnectorType connectorType, VoxelShape cableShape, VoxelShape blockShape) {
        if (connectorType == ConnectorType.CABLE) {
            return Shapes.join(shape, cableShape, BooleanOp.OR);
        } else if (connectorType == ConnectorType.BLOCK) {
            return Shapes.join(shape, Shapes.join(blockShape, cableShape, BooleanOp.OR), BooleanOp.OR);
        } else {
            return shape;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        ConnectorType north = getConnectorType(pLevel, pPos, Direction.NORTH);
        ConnectorType south = getConnectorType(pLevel, pPos, Direction.SOUTH);
        ConnectorType west = getConnectorType(pLevel, pPos, Direction.WEST);
        ConnectorType east = getConnectorType(pLevel, pPos, Direction.EAST);
        ConnectorType up = getConnectorType(pLevel, pPos, Direction.UP);
        ConnectorType down = getConnectorType(pLevel, pPos, Direction.DOWN);
        int index = calculateShapeIndex(north, south, west, east, up, down);
        return shapeCache[index];
    }

    @ParametersAreNonnullByDefault
    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        if (level.isClientSide()) return;
        if (level.getBlockEntity(pos) instanceof CableBlockEntity cable) {
            cable.markDirty();
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if(!pLevel.isClientSide() && pLevel.getBlockEntity(pPos) instanceof CableBlockEntity cable) {
            cable.markDirty();
        }

        BlockState blockState = calculateState(pLevel, pPos, pState);
        if (pState != blockState) {
            pLevel.setBlockAndUpdate(pPos, blockState);
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(WATERLOGGED, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.getFluidTicks().schedule(new ScheduledTick<>(Fluids.WATER, pPos, Fluids.WATER.getTickDelay(pLevel), 0L));
        }

        return calculateState(pLevel, pPos, pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        return calculateState(level, pos, defaultBlockState())
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
    }

    // Return the connector type for the given position and facing direction
    private static ConnectorType getConnectorType(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof Cable) {
            // TODO When refactoring, only connect same tier cables
            return ConnectorType.CABLE;
        } else if (isConnectable(world, connectorPos, facing)) {
            return ConnectorType.BLOCK;
        } else {
            return ConnectorType.NONE;
        }
    }

    // Return true if the block at the given position is connectable to a cable. This is the
    // case if the block supports forge energy
    public static boolean isConnectable(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }
        BlockEntity be = world.getBlockEntity(pos);
        if (be == null) {
            return false;
        }
        return be.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    @Nonnull
    public static BlockState calculateState(LevelAccessor pLevel, BlockPos pos, BlockState state) {
        ConnectorType north = getConnectorType(pLevel, pos, Direction.NORTH);
        ConnectorType south = getConnectorType(pLevel, pos, Direction.SOUTH);
        ConnectorType west = getConnectorType(pLevel, pos, Direction.WEST);
        ConnectorType east = getConnectorType(pLevel, pos, Direction.EAST);
        ConnectorType up = getConnectorType(pLevel, pos, Direction.UP);
        ConnectorType down = getConnectorType(pLevel, pos, Direction.DOWN);

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(EAST, east)
                .setValue(UP, up)
                .setValue(DOWN, down);
    }
}
