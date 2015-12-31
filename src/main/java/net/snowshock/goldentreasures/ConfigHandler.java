package net.snowshock.goldentreasures;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.snowshock.goldentreasures.interdiction.InterdictionField;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.utils.ContentHelper;
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
        loadGoldenLanternSettings();
        loadGoldenStaffSettings();

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private static void loadGoldenStaffSettings() {
        final String category = ConfigCategories.GOLDEN_STAFF;
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_STAFF_COMMENT);

        GoldenStaff.MAX_RANGE = configuration.getInt("max_range", category, 30, 1, 30,
                "How far out the golden staff will place torches.");
        GoldenStaff.MAX_CAPACITY_PER_ITEM_TYPE = configuration.getInt("max_capacity_per_item_type",
                category, 1500, 1, 9999,
                "Maximum number of each type of torch the golden staff can hold.");
        GoldenStaff.TILE_PER_COST_MULTIPLIER = configuration.getInt("tile_per_cost_multiplier", category, 6, 6, 30,
                "Multiplier determining how many extra torches will be consumed per unit of distance from the player.");
        GoldenStaff.TORCHES = loadTorchList("torches", category,
                "List of items supported as torches for the golden staff.", new String[]{"torch"});
    }


    private static void loadGoldenLanternSettings() {
        final String category = ConfigCategories.GOLDEN_LANTERN;
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_LANTERN_COMMENT);

        GoldenLantern.MIN_LIGHT_LEVEL = configuration.getInt("min_light_level", category, 8, 0, 15,
                "Minimum light level before a torch is placed.");
        GoldenLantern.PLACEMENT_SCAN_RADIUS = configuration.getInt("placement_scan_radius", category, 6, 1, 15,
                "How far to scan for torch placement.");
//        TODO should probably implement only_place_on_visible_blocks even though the original didn't
//        Reliquary.CONFIG.require(Names.lantern_of_paranoia, "only_place_on_visible_blocks", new ConfigReference(false));

    }

    private static void loadGoldenCoinSettings() {
        final String category = ConfigCategories.GOLDEN_COIN;
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_COIN_COMMENT);
        GoldenCoin.LONG_PULL_DISTANCE = configuration.getInt("long_pull_distance", category, 15, 0, 64,
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

        final int pushRadius = configuration.getInt("push_radius", category, 5, 1, 15,
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


    private static List<Item> loadTorchList(String name, String category, String comment, String[] defaults) {
        LOGGER.info("Loading entity class list [{}]", name);
        List<String> itemNames = Arrays.asList(configuration.getStringList(name, category, defaults, comment));

        if (!itemNames.contains("torch"))
            itemNames.add(0, "torch");

        List<Item> result = new ArrayList<>();
        for (String itemName : itemNames) {
            // Prepend : if it's bare so that contenthelper doesn't try to resolve it to this mod's item.
            if(!itemName.contains(":"))
                itemName = ":" + itemName;

            final Item item = ContentHelper.getItem(itemName);
            if(item != null)
                result.add(item);
            else
                LOGGER.warn("Could not add item [{}] to torch list . Item not found.", itemName);
        }
        return result;
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
