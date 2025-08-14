package com.pasey.peculiardevices.items.tools;

import com.pasey.peculiardevices.items.base.EnergyItem;
import com.pasey.peculiardevices.items.client.JackhammerItemRenderer;
import com.pasey.peculiardevices.registration.PDItems;
import com.pasey.peculiardevices.tags.PDTags;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Jackhammer extends EnergyItem {
    private static final float attackDamageBonus = 1.0f;
    private static final float attackSpeed = -2.8f;
    private static final int energyPerBlock = 10;   // energy cost for breaking a block
    private static final int energyPerHit = 10;     // energy cost when hitting an entity

    Map<Item, Float> drillHeadSpeeds = Map.of(
            PDItems.COPPER_DRILL_HEAD.get(), Tiers.STONE.getSpeed(),
            PDItems.IRON_DRILL_HEAD.get(), Tiers.IRON.getSpeed(),
            PDItems.DIAMOND_DRILL_HEAD.get(), Tiers.DIAMOND.getSpeed(),
            Items.AIR, 1.0f // default speed for no head
    );

    public Jackhammer() {
        super(new Item.Properties(), 10000, 100, 100);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(RenderJackhammer.INSTANCE);
    }

    public static class RenderJackhammer implements IClientItemExtensions {
        public static RenderJackhammer INSTANCE = new RenderJackhammer();

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return JackhammerItemRenderer.INSTANCE;
        }
    }

    // Tool behavior: mining speed
    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        // If block is pickaxe-minable, use the tier speed; otherwise fallback to default 1.0F
        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
            return drillHeadSpeeds.get(getDrillHead(stack).getItem());
        }
        return 1.0F;
    }

    // Block breaking: consume energy
    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        // Only perform energy consumption server-side
        boolean success = false;
        if (!level.isClientSide()) {
            // Optional: reject breaking blocks that require a higher tier than this tool.
            // Many vanilla blocks check item instanceof TieredItem; if you require that
            // behaviour, adapt here by checking the block's required tier and comparing.
            if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                success = consumeEnergy(stack, energyPerBlock);
            }
        }
        // Do not call super that would damage the item (we use energy instead of durability)
        return success;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        boolean correctHead;

        if (state.is(BlockTags.NEEDS_IRON_TOOL)) {
            correctHead = getDrillHead(stack).is(PDItems.IRON_DRILL_HEAD.get()) ||
                   getDrillHead(stack).is(PDItems.DIAMOND_DRILL_HEAD.get());
        }
        else if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            correctHead = getDrillHead(stack).is(PDItems.DIAMOND_DRILL_HEAD.get());
        }
        else {
            correctHead = true;
        }

        return correctHead && state.is(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    // Entity hits: consume energy instead of damaging the tool
    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {
            return consumeEnergy(stack, energyPerHit);
        }
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack jackhammer = pPlayer.getItemInHand(pUsedHand);
        ItemStack held = pPlayer.getItemInHand(pUsedHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if (pPlayer.isShiftKeyDown()) {
            if (!pLevel.isClientSide) {
                ItemStack currentHead = getDrillHead(jackhammer);

                // If holding a new drill head, swap
                if (held.is(PDTags.Items.PD_DRILL_HEADS)) {
                    if (!currentHead.isEmpty()) {
                        pPlayer.addItem(currentHead); // give old head back
                    }
                    setDrillHead(jackhammer, held.copyWithCount(1));
                    held.shrink(1);
                    pPlayer.displayClientMessage(Component.literal("Equipped drill head!"), true);
                }
                // If no head in hand, eject the current one
                else if (!currentHead.isEmpty()) {
                    pPlayer.addItem(currentHead);
                    setDrillHead(jackhammer, ItemStack.EMPTY);
                    pPlayer.displayClientMessage(Component.literal("Removed drill head!"), true);
                }
            }
            return InteractionResultHolder.sidedSuccess(jackhammer, pLevel.isClientSide());
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    // ToolActions support (so mods can query if this tool can perform pickaxe actions)
    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        // Allow default pickaxe actions (break blocks that rely on ToolAction)
        return ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction) || super.canPerformAction(stack, toolAction);
    }


    // Tooltip for the equipped drill head
    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        String equippedBitString = getDrillHead(pStack).is(PDTags.Items.PD_DRILL_HEADS) ? getDrillHead(pStack).getDisplayName().getString() : "No bit";
        pTooltipComponents.add(Component.literal(equippedBitString + " equipped").withStyle(net.minecraft.ChatFormatting.GRAY));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static ItemStack getDrillHead(ItemStack jackhammer) {
        CompoundTag tag = jackhammer.getOrCreateTag();
        if (tag.contains("DrillHead")) {
            return ItemStack.of(tag.getCompound("DrillHead"));
        }
        return ItemStack.EMPTY;
    }

    public static void setDrillHead(ItemStack jackhammer, ItemStack drillHead) {
        CompoundTag tag = jackhammer.getOrCreateTag();
        if (!drillHead.isEmpty()) {
            tag.put("DrillHead", drillHead.save(new CompoundTag()));
        } else {
            tag.remove("DrillHead");
        }
    }
}
