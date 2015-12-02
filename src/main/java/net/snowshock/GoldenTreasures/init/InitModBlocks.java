package net.snowshock.GoldenTreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTorch;
import net.snowshock.GoldenTreasures.blocks.BlockGoldenLilypad;
import net.snowshock.GoldenTreasures.blocks.BlockGoldenTorch;
import net.snowshock.GoldenTreasures.references.ReferencesModBlocks;
import net.snowshock.GoldenTreasures.references.ReferencesModInfo;

@GameRegistry.ObjectHolder(ReferencesModInfo.MOD_ID)
public class InitModBlocks {
    public static final BlockFlower blockGoldenLilypad = new BlockGoldenLilypad();
    public static final BlockTorch blockGoldenTorch = new BlockGoldenTorch();

    public static void init() {
        GameRegistry.registerBlock(blockGoldenLilypad, ReferencesModBlocks.GOLDEN_LILYPAD);
        GameRegistry.registerBlock(blockGoldenTorch, ReferencesModBlocks.GOLDEN_TORCH);
    }
}
