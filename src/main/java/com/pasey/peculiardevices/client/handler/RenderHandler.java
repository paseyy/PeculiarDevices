package com.pasey.peculiardevices.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pasey.peculiardevices.PeculiarDevices;
import com.pasey.peculiardevices.items.client.JackhammerItemRenderer;
import com.pasey.peculiardevices.items.tools.Jackhammer;
import com.pasey.peculiardevices.registration.PDItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PeculiarDevices.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderHandler {
    @SubscribeEvent
    public static void onRenderSpecificHand(net.minecraftforge.client.event.RenderHandEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        if (stack.getItem() == PDItems.JACKHAMMER.get()) {
            // stop vanilla from applying equip/swing transforms
            event.setCanceled(true);
            // use the JackhammerItemRenderer instance
            var renderer = (JackhammerItemRenderer) Jackhammer.RenderJackhammer.INSTANCE.getCustomRenderer();

            // get rendering inputs from the event
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource buffers = event.getMultiBufferSource();
            int packedLight = event.getPackedLight();
            ItemDisplayContext transformType = (event.getHand() == InteractionHand.MAIN_HAND)
                    ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;

            // basic first-person transform
            poseStack.translate(transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND ? 1.0f : -1.0f, -0.75f, -1.0f);

            // equip animation
            poseStack.translate((event.getEquipProgress()), -event.getEquipProgress(), 0.0f);

            // mining animation
            renderer.applyMiningPose(poseStack);

            // finally, render the item
            renderer.renderByItem(stack, transformType, poseStack, buffers, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}

