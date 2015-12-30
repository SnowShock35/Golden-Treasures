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

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.ConfigCategories;
import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenTorch;
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

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private static void loadGeneralSettings() {
        configuration.setCategoryComment(ConfigCategories.GENERAL, ConfigCategories.GENERAL_COMMENT);
        configuration.setCategoryRequiresMcRestart(ConfigCategories.GENERAL, true);
    }

    private static void loadGoldenTorchSettings() {

        configuration.setCategoryRequiresMcRestart(ConfigCategories.GOLDEN_TORCH, true);

        final String goldenTorchSubCategory = ConfigCategories.GOLDEN_TORCH;
        final String goldenTorchComment = ConfigCategories.GOLDEN_TORCH_COMMENT;
        configuration.setCategoryComment(goldenTorchSubCategory, goldenTorchComment);

        final int pushRadius = configuration.getInt("push_radius", goldenTorchSubCategory, 5, 1, 10, "Range of the golden torch's interdiction effect.");
        final Map<EntityHelper.EntityType, Boolean> goldenTorchEntityTypeConfiguration = new HashMap<>();
        goldenTorchEntityTypeConfiguration.put(EntityHelper.EntityType.PROJECTILE, configuration.getBoolean("projectiles_enabled", goldenTorchSubCategory, false, "Can projectiles be pushed?"));
        goldenTorchEntityTypeConfiguration.put(EntityHelper.EntityType.HOSTILE, configuration.getBoolean("hostile_enabled", goldenTorchSubCategory, true, "Can hostile mobs be pushed?"));
        goldenTorchEntityTypeConfiguration.put(EntityHelper.EntityType.PASSIVE, configuration.getBoolean("passive_enabled", goldenTorchSubCategory, false, "Can passive mobs be pushed?"));
        goldenTorchEntityTypeConfiguration.put(EntityHelper.EntityType.TC_GOLEM, configuration.getBoolean("thaumcraft_golems_enabled", goldenTorchSubCategory, false, "Can Thaumcraft golems mobs be pushed?"));
        goldenTorchEntityTypeConfiguration.put(EntityHelper.EntityType.OTHER, false);

        List<Class<? extends Entity>> entityBlacklist = loadEntityClassList("blacklist", goldenTorchSubCategory, "List of mobs that interdiction fields should NEVER push", new String[]{});
        List<Class<? extends Entity>> entityWhitelist = loadEntityClassList("whiteList", goldenTorchSubCategory, "List of mobs that interdiction fields should ALWAYS push", new String[]{});

        GoldenTorch.interdictionField = new InterdictionField(pushRadius, goldenTorchEntityTypeConfiguration, entityWhitelist, entityBlacklist);
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
