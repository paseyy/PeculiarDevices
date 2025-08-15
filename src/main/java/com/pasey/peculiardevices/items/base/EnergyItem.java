package com.pasey.peculiardevices.items.base;

import com.pasey.peculiardevices.capabilities.EnergyCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pasey.peculiardevices.client.screen.util.FormatText.formatCapacity;

public abstract class EnergyItem extends Item {
    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;

    public EnergyItem(Properties pProperties, int capacity, int maxReceive, int maxExtract) {
        super(pProperties
                .stacksTo(1)
        );

        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public static int getEnergy(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getInt("Energy");
    }

    public static void setEnergy(ItemStack stack, int energy) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Energy", Math.min(energy, getMaxEnergyStoredStatic(stack)));
    }

    public static int getMaxEnergyStoredStatic(ItemStack stack) {
        if (stack.getItem() instanceof EnergyItem item) {
            return item.capacity;
        }
        return 0;
    }

    protected boolean consumeEnergy(ItemStack stack, int amount) {
        AtomicInteger consumed = new AtomicInteger(0);
        stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(handler -> {
            // simulate
            int sim = handler.extractEnergy(amount, true);
            if (sim >= amount) {
                int did = handler.extractEnergy(amount, false);
                consumed.set(did);
            }
        });
        return consumed.get() >= amount;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * getEnergy(stack) / (float) capacity);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getBarColor(ItemStack stack) {
        return 0x3355FF;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal(formatCapacity(getEnergy(pStack), capacity)).withStyle(ChatFormatting.BLUE));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EnergyCapabilityProvider(stack, capacity, maxReceive, maxExtract);
    }
}




