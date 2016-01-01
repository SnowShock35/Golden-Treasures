package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.snowshock.goldentreasures.GoldenTreasures;
import net.snowshock.goldentreasures.entity.EntityGoldenBomb;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.snowshock.goldentreasures.references.ReferencesModEntities.GOLDEN_BOMB_ENTITY;

public class InitModEntities {

    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);

    public static void init() {
        LOGGER.debug("Initializing Entities....");

        EntityRegistry.registerModEntity(EntityGoldenBomb.class, GOLDEN_BOMB_ENTITY, 0,
                GoldenTreasures.instance, 128, 5, true);

        LOGGER.log(Level.INFO, "Mod Entities Initialized");
    }

}
