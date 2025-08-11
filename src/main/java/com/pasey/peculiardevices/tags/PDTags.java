package com.pasey.peculiardevices.tags;

import com.pasey.peculiardevices.PeculiarDevices;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.minecraft.tags.BlockTags;

public class PDTags {
    @SuppressWarnings("unused")
    public static class Blocks {
        public static final TagKey<Block> PD_DEVICES = tag("pd_machines");
        public static final TagKey<Block> PD_ORES = tag("pd_ores");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(PeculiarDevices.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> PD_MACHINES = tag("pd_machines");
        public static final TagKey<Item> PD_MILLINGS = tag("pd_millings");

        public static final TagKey<Item> PD_GRIME_DYNAMO_BURNABLES = tag("pd_grime_dynamo_burnables");
        public static final TagKey<Item> PD_DRILL_HEADS = tag("pd_drill_heads");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(PeculiarDevices.MODID, name));
        }
    }
}
