package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTorch;
import net.snowshock.goldentreasures.blocks.BlockGoldenLilypad;
import net.snowshock.goldentreasures.blocks.BlockGoldenTorch;
import net.snowshock.goldentreasures.interdiction.InterdictionField;
import net.snowshock.goldentreasures.references.ReferencesConfigInfo;
import net.snowshock.goldentreasures.references.ReferencesModBlocks;
import net.snowshock.goldentreasures.references.ReferencesModInfo;

@GameRegistry.ObjectHolder(ReferencesModInfo.MOD_ID)
public class InitModBlocks {
    public static final BlockFlower blockGoldenLilypad = null;
    public static final BlockTorch blockGoldenTorch = null;

    public static void init() {
        InterdictionField goldenTorchInterdictionField = ReferencesConfigInfo.GoldenTorch.interdictionField;
        BlockGoldenTorch goldenTorch = new BlockGoldenTorch(goldenTorchInterdictionField);

        GameRegistry.registerBlock(new BlockGoldenLilypad(), ReferencesModBlocks.GOLDEN_LILYPAD);
        GameRegistry.registerBlock(goldenTorch, ReferencesModBlocks.GOLDEN_TORCH);
    }
}
