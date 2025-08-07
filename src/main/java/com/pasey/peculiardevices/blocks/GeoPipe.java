package com.pasey.peculiardevices.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class GeoPipe extends Block {
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    public GeoPipe() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_ORANGE)
                .requiresCorrectToolForDrops()
                .strength(1.5f, 3.0f)
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.COPPER)
                .noOcclusion()
        );

        registerDefaultState(
            this.stateDefinition.any()
                    .setValue(BOTTOM, true)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(BOTTOM);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos belowPos = pContext.getClickedPos().below();
        Level level = pContext.getLevel();

        if(level.getBlockState(belowPos).is(this)) {
            return this.defaultBlockState().setValue(BOTTOM, false);
        }
        return this.defaultBlockState();
    }

    @SuppressWarnings("deprecation")
    @Override
    @ParametersAreNonnullByDefault
    public @NotNull BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if(pNeighborPos.equals(pPos.below())) {
            if(pNeighborState.is(this)) {
                return pState.setValue(BOTTOM, false);
            }
            else {
                return pState.setValue(BOTTOM, true);
            }
        }

        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }
}
