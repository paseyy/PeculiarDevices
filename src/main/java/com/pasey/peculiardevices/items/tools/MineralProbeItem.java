package com.pasey.peculiardevices.items.tools;

import com.pasey.peculiardevices.items.base.EnergyItem;
import com.pasey.peculiardevices.util.CircularList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class MineralProbeItem extends EnergyItem {
    public static final int CAPACITY = 10000;
    public static final int MAX_RECEIVE = 100;
    public static final int MAX_EXTRACT = 10000;
    public static final int ENERGY_PER_USE = 50;

    public static final CircularList<Block> DETECTABLE_BLOCKS = new CircularList<>(
            Blocks.GRANITE,
            Blocks.DIORITE,
            Blocks.ANDESITE,
            Blocks.SANDSTONE,
            Blocks.TUFF,
            Blocks.CALCITE
    );

    public MineralProbeItem() {
        super(new Item.Properties(), CAPACITY, MAX_RECEIVE, MAX_EXTRACT);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack probe = pPlayer.getItemInHand(pUsedHand);

        if (pPlayer.isShiftKeyDown()) {
            Block nextDetectBlock = DETECTABLE_BLOCKS.next(getDetectBlock(probe));
            setDetectBlock(probe, nextDetectBlock);
            pPlayer.displayClientMessage(Component.literal("Now detecting: " + nextDetectBlock.getName().getString()), true);
            return InteractionResultHolder.sidedSuccess(probe, pLevel.isClientSide());
        }

        if (getEnergy(probe) >= ENERGY_PER_USE) {
            if (!pLevel.isClientSide()) {
                return InteractionResultHolder.consume(probe);
            }
            BlockPos pos = pPlayer.blockPosition();
            BlockState state;
            do {
                state = pLevel.getBlockState(pos);
                if (state.is(getDetectBlock(probe))) {
                    pPlayer.displayClientMessage(Component.literal(
                            "Detected block: " + state.getBlock().getName().getString() + " at y= " + pos.getY()), true);
                    return InteractionResultHolder.sidedSuccess(probe, pLevel.isClientSide());
                }
                pos = pos.below();
            }
            while (!state.is(Blocks.BEDROCK));
            consumeEnergy(probe, ENERGY_PER_USE);
        }

        return InteractionResultHolder.sidedSuccess(probe, pLevel.isClientSide());
    }

    public static Block getDetectBlock(ItemStack probe) {
        CompoundTag tag = probe.getOrCreateTag();
        if(tag.contains("DetectBlock")) {
            return ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse(tag.getString("DetectBlock")));
        } else {
            setDetectBlock(probe, Blocks.GRANITE);
            return Blocks.GRANITE;
        }
    }

    public static void setDetectBlock(ItemStack probe, Block block) {
        CompoundTag tag = probe.getOrCreateTag();
        tag.putString("DetectBlock", Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString());
        probe.setTag(tag);
    }
}
