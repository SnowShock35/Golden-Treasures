package uk.snowshock35.goldentreasures.init;

import cpw.mods.fml.common.registry.EntityRegistry;
import uk.snowshock35.goldentreasures.GoldenTreasures;
import uk.snowshock35.goldentreasures.entity.EntityGoldenBomb;
import uk.snowshock35.goldentreasures.references.ReferencesModInfo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static uk.snowshock35.goldentreasures.references.ReferencesConfigInfo.GoldenBomb;
import static uk.snowshock35.goldentreasures.references.ReferencesModEntities.GOLDEN_BOMB_ENTITY;

public class InitModEntities {

    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);

    public static void init() {
        LOGGER.debug("Initializing Entities....");

        if (GoldenBomb.ITEM_ENABLED) {
            EntityRegistry.registerModEntity(EntityGoldenBomb.class, GOLDEN_BOMB_ENTITY, 0,
                    GoldenTreasures.instance, 128, 5, true);
        }

        LOGGER.log(Level.INFO, "Mod Entities Initialized");
    }

}
