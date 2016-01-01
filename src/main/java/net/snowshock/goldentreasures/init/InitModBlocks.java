package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTorch;
import net.minecraft.item.ItemBlock;
import net.snowshock.goldentreasures.blocks.BlockGoldenLilypad;
import net.snowshock.goldentreasures.blocks.BlockGoldenTorch;
import net.snowshock.goldentreasures.blocks.BlockGoldenTreasures;
import net.snowshock.goldentreasures.interdiction.InterdictionField;
import net.snowshock.goldentreasures.items.block.ItemBlockGoldenLilypad;
import net.snowshock.goldentreasures.items.block.ItemBlockGoldenTreasures;
import net.snowshock.goldentreasures.references.ReferencesConfigInfo;
import net.snowshock.goldentreasures.references.ReferencesModBlocks;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@GameRegistry.ObjectHolder(ReferencesModInfo.MOD_ID)
public class InitModBlocks {

    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);

    @GameRegistry.ObjectHolder(ReferencesModBlocks.GOLDEN_LILYPAD)
    public static final BlockGoldenTreasures blockGoldenLilypad = null;

    @GameRegistry.ObjectHolder(ReferencesModBlocks.GOLDEN_TORCH)
    public static final BlockTorch blockGoldenTorch = null;

    public static void preInit() {
        LOGGER.debug("Initializing Blocks....");

        InterdictionField goldenTorchInterdictionField = ReferencesConfigInfo.GoldenTorch.interdictionField;
        BlockGoldenTorch goldenTorch = new BlockGoldenTorch(goldenTorchInterdictionField);

        GameRegistry.registerBlock(new BlockGoldenLilypad(), ItemBlockGoldenLilypad.class, ReferencesModBlocks.GOLDEN_LILYPAD);
        GameRegistry.registerBlock(goldenTorch, ItemBlockGoldenTreasures.class, ReferencesModBlocks.GOLDEN_TORCH);

        LOGGER.log(Level.INFO, "Mod Blocks Initialized");
    }
}
