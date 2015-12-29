package net.snowshock.goldentreasures.utils;

import net.minecraft.block.Block;

public class ContentHelper {
    public static String getIdent(Block block) {
        return block == null ? null : Block.blockRegistry.getNameForObject(block);
    }

    public static boolean areBlocksEqual(Block block1, Block block2) {
        return getIdent(block1).equals(getIdent(block2));
    }
}
