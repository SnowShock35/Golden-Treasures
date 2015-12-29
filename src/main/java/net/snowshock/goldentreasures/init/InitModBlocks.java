package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTorch;
import net.snowshock.goldentreasures.blocks.BlockGoldenLilypad;
import net.snowshock.goldentreasures.blocks.BlockGoldenTorch;
import net.snowshock.goldentreasures.references.ReferencesModBlocks;
import net.snowshock.goldentreasures.references.ReferencesModInfo;

@GameRegistry.ObjectHolder(ReferencesModInfo.MOD_ID)
public class InitModBlocks {
    public static final BlockFlower blockGoldenLilypad = new BlockGoldenLilypad();
    public static final BlockTorch blockGoldenTorch = new BlockGoldenTorch();

    public static void init() {
        GameRegistry.registerBlock(blockGoldenLilypad, ReferencesModBlocks.GOLDEN_LILYPAD);
        GameRegistry.registerBlock(blockGoldenTorch, ReferencesModBlocks.GOLDEN_TORCH);
    }
}
