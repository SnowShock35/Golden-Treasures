package net.snowshock.goldentreasures.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.snowshock.goldentreasures.references.ReferencesModInfo;

public class ContentHelper {
    public static String getIdent(Block block) {
        return block == null ? null : Block.blockRegistry.getNameForObject(block);
    }

    public static String getIdent(Item item) {
        return item == null ? null : Item.itemRegistry.getNameForObject(item);
    }

    public static boolean areBlocksEqual(Block block1, Block block2) {
        return getIdent(block1).equals(getIdent(block2));
    }

    public static boolean areItemsEqual(Item item1, Item item2) {
        return getIdent(item1).equals(getIdent(item2));
    }


    public static Block getBlock(String blockName) {
        String selection = blockName;
        if(!blockName.contains(":")) {
            selection = ReferencesModInfo.MOD_ID + ":" + blockName;
        }

        if(selection.indexOf(":") == 0) {
            selection = selection.substring(1);
        }

        return (Block)Block.blockRegistry.getObject(selection);
    }

    public static Item getItem(String itemName) {
        String selection = itemName;
        if(!itemName.contains(":")) {
            selection = ReferencesModInfo.MOD_ID + ":" + itemName;
        }

        if(selection.indexOf(":") == 0) {
            selection = selection.substring(1);
        }

        return (Item)Item.itemRegistry.getObject(selection);
    }

    public static Item getItemBlock(String blockName) {
        String selection = blockName;
        if(!blockName.contains(":")) {
            selection = ReferencesModInfo.MOD_ID + ":" + blockName;
        }

        if(selection.indexOf(":") == 0) {
            selection = selection.substring(1);
        }

        return Item.getItemFromBlock((Block)Block.blockRegistry.getObject(selection));
    }
}
