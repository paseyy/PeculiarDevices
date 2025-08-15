package com.pasey.peculiardevices.blocks.devices;

import com.pasey.peculiardevices.blockentities.GeoElectricFurnaceBlockEntity;
import com.pasey.peculiardevices.blockentities.util.CreateTickerHelper;
import com.pasey.peculiardevices.blocks.base.BaseDeviceBlock;
import com.pasey.peculiardevices.registration.PDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class GeoElectricFurnace extends BaseDeviceBlock {
    public GeoElectricFurnace() {
        super(Properties.of()
                .strength(1.0f)
                .requiresCorrectToolForDrops()
                .mapColor(MapColor.METAL)
                .sound(SoundType.METAL));
    }

    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GeoElectricFurnaceBlockEntity(pPos, pState);
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity be = pLevel.getBlockEntity(pPos);

        if(!(be instanceof GeoElectricFurnaceBlockEntity blockEntity))
            return InteractionResult.PASS;

        if(pLevel.isClientSide())
            return InteractionResult.SUCCESS;

        if(pPlayer instanceof ServerPlayer sPlayer) {
            NetworkHooks.openScreen(sPlayer, blockEntity, pPos);
        }

        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return CreateTickerHelper.createTickerHelper(pBlockEntityType, PDBlockEntities.GEO_ELECRIC_FURNACE_BE.get());
    }
}
