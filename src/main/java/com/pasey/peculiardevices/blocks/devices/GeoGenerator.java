package com.pasey.peculiardevices.blocks.devices;

import com.pasey.peculiardevices.blockentities.GeoGeneratorBlockEntity;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class GeoGenerator extends BaseDeviceBlock {
    public GeoGenerator() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(1.5f, 3.0f)
                .sound(SoundType.METAL)
                .noOcclusion());
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return PDBlockEntities.GEO_GENERATOR_BE.get().create(pPos, pState);
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity be = pLevel.getBlockEntity(pPos);

        if(!(be instanceof GeoGeneratorBlockEntity blockEntity))
            return InteractionResult.PASS;

        if(pLevel.isClientSide())
            return InteractionResult.SUCCESS;

        if(pPlayer instanceof ServerPlayer sPlayer) {
            NetworkHooks.openScreen(sPlayer, blockEntity, pPos);
        }

        return InteractionResult.CONSUME;
    }
}

