package com.pasey.peculiardevices.items.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;


public class JackhammerItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final JackhammerItemRenderer INSTANCE = new JackhammerItemRenderer();

    private final ResourceLocation modelLoc = ResourceLocation.fromNamespaceAndPath("peculiardevices", "item/jackhammer_model");
    // animation timing & magnitudes (tweak to taste)
    private static final float MOVE_IN_DURATION = 0.12f;   // seconds to move in
    private static final float MOVE_DISTANCE = 0.4f;       // how much Z moves toward screen center (positive reduces magnitude of negative Z)
    private static final float VIB_AMPLITUDE = 0.06f;      // vibration amplitude (blocks)
    private static final float VIB_FREQ = 20f;             // vibration frequency in Hz

    // internal animation state
    private boolean wasMining = false;
    private long pressStartNano = 0L;    // start time for moving-in

    public JackhammerItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    @ParametersAreNonnullByDefault
    public void renderByItem(ItemStack stack,
                             ItemDisplayContext displayContext,
                             PoseStack poseStack,
                             MultiBufferSource buffer,
                             int packedLight,
                             int packedOverlay) {

        // get the original jackhammer model
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = mc.getModelManager().getModel(modelLoc);

        // apply transformations
        poseStack.pushPose();
        if (displayContext != ItemDisplayContext.FIRST_PERSON_LEFT_HAND &&
            displayContext != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
            poseStack.translate(0.5F, 0.5F, 0.5F);
        }

        // render item
        boolean leftHanded = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        mc.getItemRenderer().render(stack, displayContext, leftHanded, poseStack, buffer, packedLight, packedOverlay, model);

        poseStack.popPose();
    }

    public void applyMiningPose(PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();

        boolean attackDown = mc.options.keyAttack.isDown();
        boolean lookingAtBlock = mc.hitResult instanceof net.minecraft.world.phys.BlockHitResult;
        boolean isMining = attackDown && lookingAtBlock;

        if (!isMining) {
            wasMining = false;
            return;
        }

        if (!wasMining) {
            pressStartNano = System.nanoTime();
            wasMining = true;
        }
        float elapsedSeconds = (System.nanoTime() - pressStartNano) / 1_000_000_000.0f;
        // move-in
        float moveInProgress = Mth.clamp(elapsedSeconds / MOVE_IN_DURATION, 0f, 1f);
        poseStack.translate(
                -MOVE_DISTANCE * moveInProgress,
                MOVE_DISTANCE * moveInProgress,
                -MOVE_DISTANCE * moveInProgress);

        // mine
        if (moveInProgress >= 1f) {
            float vibProgress = elapsedSeconds * VIB_FREQ;
            float vibOffset = Mth.sin(2 * (float) Math.PI * vibProgress) * VIB_AMPLITUDE;
            poseStack.translate(0f, 0f, vibOffset);
        }
    }
}
