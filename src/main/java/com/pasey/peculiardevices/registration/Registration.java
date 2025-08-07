package com.pasey.peculiardevices.registration;

import net.minecraftforge.eventbus.api.IEventBus;

public class Registration {
    public static void init(IEventBus modEventBus) {
        PDCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        PDItems.ITEMS.register(modEventBus);
        PDBlocks.BLOCKS.register(modEventBus);
        PDBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        PDMenus.MENU_TYPES.register(modEventBus);
        PDRecipes.SERIALIZERS.register(modEventBus);
    }
}
