package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockTorch;
import net.snowshock.goldentreasures.blocks.BlockGoldenLilypad;
import net.snowshock.goldentreasures.blocks.BlockGoldenTorch;
import net.snowshock.goldentreasures.blocks.BlockGoldenTreasures;
import net.snowshock.goldentreasures.interdiction.InterdictionField;
import net.snowshock.goldentreasures.items.block.ItemBlockGoldenLilypad;
import net.snowshock.goldentreasures.items.block.ItemBlockGoldenTreasures;
import net.snowshock.goldentreasures.references.ReferencesModBlocks;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenLilypad;
import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenTorch;

@GameRegistry.ObjectHolder(ReferencesModInfo.MOD_ID)
public class InitModBlocks {

    @GameRegistry.ObjectHolder(ReferencesModBlocks.GOLDEN_LILYPAD)
    public static final BlockGoldenTreasures blockGoldenLilypad = null;
    @GameRegistry.ObjectHolder(ReferencesModBlocks.GOLDEN_TORCH)
    public static final BlockTorch blockGoldenTorch = null;
    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);

    public static void preInit() {
        LOGGER.debug("Initializing Blocks....");

        if (GoldenTorch.ITEM_ENABLED) {
            InterdictionField goldenTorchInterdictionField = GoldenTorch.interdictionField;
            BlockGoldenTorch goldenTorch = new BlockGoldenTorch(goldenTorchInterdictionField);
            GameRegistry.registerBlock(goldenTorch, ItemBlockGoldenTreasures.class, ReferencesModBlocks.GOLDEN_TORCH);
        }

        if (GoldenLilypad.ITEM_ENABLED) {
            GameRegistry.registerBlock(new BlockGoldenLilypad(), ItemBlockGoldenLilypad.class,
                    ReferencesModBlocks.GOLDEN_LILYPAD);
        }

        LOGGER.log(Level.INFO, "Mod Blocks Initialized");
    }
}
