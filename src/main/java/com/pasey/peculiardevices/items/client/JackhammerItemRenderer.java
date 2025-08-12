package com.pasey.peculiardevices.items.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Custom renderer for the Jackhammer item to animate mining:
 * 1) Move the model towards the middle of the screen,
 * 2) Vibrate back and forth while LMB is held,
 * 3) Return to original pose when LMB released.
 */
public class JackhammerItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final ResourceLocation modelLoc = ResourceLocation.fromNamespaceAndPath("peculiardevices", "item/jackhammer_model");


    // animation timing & magnitudes (tweak to taste)
    private static final float MOVE_IN_DURATION = 0.12f;   // seconds to move in
    private static final float RETURN_DURATION = 0.12f;    // seconds to return
    private static final float MOVE_DISTANCE = 2.0f;       // how much Z moves toward screen center (positive reduces magnitude of negative Z)
    private static final float VIB_AMPLITUDE = 0.06f;      // vibration amplitude (blocks)
    private static final float VIB_FREQ = 30f;             // vibration frequency in Hz

    // internal animation state
    private boolean wasPressed = false;
    private boolean returning = false;
    private long pressStartNano = 0L;    // start time for moving-in
    private long releaseStartNano = 0L;  // start time for returning

    public JackhammerItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    private static float nowSeconds(long nanoTime) {
        return nanoTime / 1_000_000_000.0f;
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
        else {
            // TODO apply first-person transformations!
        }

        // render item
        boolean leftHanded = displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        mc.getItemRenderer().render(stack, displayContext, leftHanded, poseStack, buffer, packedLight, packedOverlay, model);

        poseStack.popPose();
    }

    // simple defaults if not animating
    private void renderDefault(ItemStack stack,
                               ItemDisplayContext pDisplayContext,
                               PoseStack poseStack,
                               MultiBufferSource buffer,
                               int packedLight,
                               int packedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = mc.getItemRenderer().getModel(stack, null, mc.player, 0);
        boolean leftHanded = pDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        mc.getItemRenderer().render(stack, pDisplayContext, leftHanded, poseStack, buffer, packedLight, packedOverlay, model);
    }



    // helpers
    private static float clamp(float v, float a, float b) {
        return v < a ? a : Math.min(v, b);
    }

    private static float easeOutQuad(float t) {
        return 1f - (1f - t) * (1f - t);
    }
}
