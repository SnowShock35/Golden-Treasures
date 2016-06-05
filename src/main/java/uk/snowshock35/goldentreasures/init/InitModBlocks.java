package uk.snowshock35.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockTorch;
import uk.snowshock35.goldentreasures.blocks.BlockGoldenLilypad;
import uk.snowshock35.goldentreasures.blocks.BlockGoldenTorch;
import uk.snowshock35.goldentreasures.blocks.BlockGoldenTreasures;
import uk.snowshock35.goldentreasures.interdiction.InterdictionField;
import uk.snowshock35.goldentreasures.items.block.ItemBlockGoldenLilypad;
import uk.snowshock35.goldentreasures.items.block.ItemBlockGoldenTreasures;
import uk.snowshock35.goldentreasures.references.ReferencesModBlocks;
import uk.snowshock35.goldentreasures.references.ReferencesModInfo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static uk.snowshock35.goldentreasures.references.ReferencesConfigInfo.GoldenLilypad;
import static uk.snowshock35.goldentreasures.references.ReferencesConfigInfo.GoldenTorch;

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
