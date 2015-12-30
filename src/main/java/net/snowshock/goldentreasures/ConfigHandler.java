package net.snowshock.goldentreasures;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.config.Configuration;
import net.snowshock.goldentreasures.interdiction.InterdictionField;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.utils.EntityHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.*;
import static net.snowshock.goldentreasures.utils.EntityHelper.resolveEntityClass;


public class ConfigHandler {
    public static Configuration configuration;

    private static Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);

    public static void init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        loadGeneralSettings();
        loadGoldenTorchSettings();
        loadGoldenCoinSettings();

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private static void loadGoldenCoinSettings() {
        final String category = ConfigCategories.GOLDEN_COIN;
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_COIN_COMMENT);
        GoldenCoin.LONG_PULL_DISTANCE = configuration.getInt("long_pull_distance", category, 25, 0, 64,
                "Distance the coin will pull items whilst being used (holding right mouse button).");
        GoldenCoin.STANDARD_PULL_DISTANCE = configuration.getInt("standard_pull_distance", category, 5, 0, 64,
                "Distance the coin will pull items whilst activated.");
        GoldenCoin.AUDIO_DISABLED = configuration.getBoolean("audio_disabled", category, false,
                "Disable audio when item is activated?");
    }

    private static void loadGeneralSettings() {
        configuration.setCategoryComment(ConfigCategories.GENERAL, ConfigCategories.GENERAL_COMMENT);
        configuration.setCategoryRequiresMcRestart(ConfigCategories.GENERAL, true);
    }

    private static void loadGoldenTorchSettings() {
        final String category = ConfigCategories.GOLDEN_TORCH;
        configuration.setCategoryRequiresMcRestart(ConfigCategories.GOLDEN_TORCH, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_TORCH_COMMENT);

        final int pushRadius = configuration.getInt("push_radius", category, 5, 1, 10,
                "Range of the golden torch's interdiction effect.");
        final Map<EntityHelper.EntityType, Boolean> entityTypeConfiguration = new HashMap<>();
        entityTypeConfiguration.put(EntityHelper.EntityType.PROJECTILE,
                configuration.getBoolean("projectiles_enabled", category, false, "Can projectiles be pushed?"));
        entityTypeConfiguration.put(EntityHelper.EntityType.HOSTILE,
                configuration.getBoolean("hostile_enabled", category, true, "Can hostile mobs be pushed?"));
        entityTypeConfiguration.put(EntityHelper.EntityType.PASSIVE,
                configuration.getBoolean("passive_enabled", category, false, "Can passive mobs be pushed?"));
        entityTypeConfiguration.put(EntityHelper.EntityType.TC_GOLEM,
                configuration.getBoolean("thaumcraft_golems_enabled", category, false,
                        "Can Thaumcraft golems mobs be pushed?"));
        entityTypeConfiguration.put(EntityHelper.EntityType.OTHER, false);

        List<Class<? extends Entity>> entityBlacklist = loadEntityClassList("blacklist", category,
                "List of mobs that interdiction fields should NEVER push", new String[]{});
        List<Class<? extends Entity>> entityWhitelist = loadEntityClassList("whiteList", category,
                "List of mobs that interdiction fields should ALWAYS push", new String[]{});

        GoldenTorch.interdictionField = new InterdictionField(pushRadius, entityTypeConfiguration,
                entityWhitelist, entityBlacklist);
    }


    private static List<Class<? extends Entity>> loadEntityClassList(String name, String category, String comment, String[] defaults) {
        LOGGER.info("Loading entity class list [{}]", name);
        List<String> entityNames = Arrays.asList(configuration.getStringList(name, category, defaults, comment));
        List<Class<? extends Entity>> result = new ArrayList<>();
        for (String entityName : entityNames) {
            final Class<? extends Entity> entityClass = resolveEntityClass(entityName);
            result.add(entityClass);
        }
        return result;
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(ReferencesModInfo.MOD_ID)) {
            loadConfiguration();
        }
    }
}
