package net.snowshock.goldentreasures;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.snowshock.goldentreasures.interdiction.InterdictionField;
import net.snowshock.goldentreasures.items.ItemGoldenMiner;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.utils.EntityHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.*;


public class ConfigHandler {
    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID + ".Config");


    public static Configuration configuration;

    public static void preInit(final File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
        }
        loadConfiguration();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(ReferencesModInfo.MOD_ID)) {
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        try {
            LOGGER.info("Loading configuration....");
            loadGeneralSettings();
            loadGoldenTorchSettings();
            loadGoldenCoinSettings();
            loadGoldenLanternSettings();
            loadGoldenStaffSettings();
            loadGoldenMinerSettings();
            loadGoldenLilypadSettings();
            loadGoldenChaliceSettings();
            loadGoldenBombSettings();
            loadGoldenFoodSettings();
            loadGoldenFeatherSettings();
            LOGGER.info("Loaded!");
        } catch (Exception e) {
            LOGGER.error("Failed to load configuration.");
            throw new RuntimeException("Failed to load Golden Treasures configuration.", e);
        } finally {
            if (configuration.hasChanged()) {
                LOGGER.info("Configuration has changed. Saving....");
                configuration.save();
                LOGGER.debug("Saved!");
            }

            logLoadedConfiguration(configuration);
        }
    }

    private static void logLoadedConfiguration(final Configuration configuration) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Loaded configuration [{}]", configuration.toString());
            final Set<String> categoryNames = getTopLevelCategoryNames(configuration);
            for (String categoryName : categoryNames) {
                final ConfigCategory category = configuration.getCategory(categoryName);
                logCategory(category, 0);
            }
        }
    }

    private static Set<String> getTopLevelCategoryNames(Configuration configuration) {
        final Set<String> categoryNames = configuration.getCategoryNames();
        final Set<String> topLevelCategoryNames = new HashSet<>(categoryNames);
        for (String categoryName : categoryNames) {
            if(categoryName.contains("."))
                topLevelCategoryNames.remove(categoryName);
        }
        return topLevelCategoryNames;
    }

    private static void logCategory(final ConfigCategory category, final int level) {
        final String indent = getIndent(level);
        LOGGER.trace("{}Category: [{}] {", indent, category.getName());
        for (ConfigCategory subCategory : category.getChildren()) {
            logCategory(subCategory, level + 1);
        }
        for (Property property : category.getOrderedValues()) {
            LOGGER.trace("{}\t[{}} -> [{}]", indent, property.getName(), property.getString());
        }
        LOGGER.trace("{}}\n", indent);
    }

    private static String getIndent(final int level) {
        StringBuilder builder = new StringBuilder("\t");
        for(int index = 0; index < level; index++)
            builder.append("\t");

        return builder.toString();
    }

    private static void loadGeneralSettings() {
        final String category = ConfigCategories.GENERAL;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryComment(category, ConfigCategories.GENERAL_COMMENT);
        configuration.setCategoryRequiresMcRestart(category, true);


        GeneralConfigs.NUM_CRAFTING_COMPONENTS = configuration.getInt("num_crafting_components", category, 3, 3, 128,
                "Advanced: Set the number of possible crafting components. To create a custom component that " +
                        "component's name must be in the locale files (e.g. lang/en_US.lang) and an icon must exist in" +
                        "textures/items/golden_crafting_component_N.png. This feature is intended for experienced " +
                        "modpack authors who wish to create custom recipes using something like minetweaker only!");
        GeneralConfigs.DUNGEON_SPAWN_TYPES = Arrays.asList(configuration.getStringList("dungeon_types", category,
                new String[]{
                        ChestGenHooks.DUNGEON_CHEST,
                        ChestGenHooks.BONUS_CHEST,
                        ChestGenHooks.VILLAGE_BLACKSMITH,
                        ChestGenHooks.STRONGHOLD_LIBRARY,
                        ChestGenHooks.STRONGHOLD_CORRIDOR,
                        ChestGenHooks.STRONGHOLD_CROSSING,
                        ChestGenHooks.PYRAMID_JUNGLE_CHEST,
                        ChestGenHooks.PYRAMID_DESERT_CHEST,
                        ChestGenHooks.MINESHAFT_CORRIDOR
                },
                "List of chest types to spawn items in, as used in ChestGenHooks. See the default list for examples."));
    }

    private static void loadGoldenFeatherSettings() {
        final String category = ConfigCategories.GOLDEN_FEATHER;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_FEATHER_COMMENT);

        GoldenFeather.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");
        GoldenFeather.LEAPING_POTENCY = configuration.getInt("leaping_potency", category, 3, 0, 5,
                "How potent is the leaping effect. 1 = Just enough to jump a 2 block high wall).");
        GoldenFeather.HUNGER_MULTIPLIER = configuration.getFloat("hunger_multiplier", category, 1F, 0F, 9999F,
                "Multiplier which affects the amount of hunger is consumed when negating fall damage");
        GoldenFeather.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }

    private static void loadGoldenBombSettings() {
        final String category = ConfigCategories.GOLDEN_BOMB;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_BOMB_COMMENT);

        GoldenBomb.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");
        GoldenBomb.DISPENSER_ENABLED = configuration.getBoolean("dispenser", category, true,
                "true = bomb shoots from dispenser as if thrown. false = bomb dropped from dispensers as item.");
        GoldenBomb.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }

    private static void loadGoldenFoodSettings() {
        final String category = ConfigCategories.GOLDEN_FOOD;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_FOOD_COMMENT);

        GoldenFood.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");
        GoldenFood.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }

    private static void loadGoldenChaliceSettings() {
        final String category = ConfigCategories.GOLDEN_CHALICE;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_CHALICE_COMMENT);

        GoldenChalice.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");

        GoldenChalice.HUNGER_SATURATION_MULTIPLIER = configuration.getInt("hunger_satiation_multiplier", category, 4, 0, 9999,
                "Multiplies the amount of saturation restored (and drowning damage taken) when drinking from the chalice.");
        GoldenChalice.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }

    private static void loadGoldenLilypadSettings() {
        final String category = ConfigCategories.GOLDEN_LILYPAD;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_LILYPAD_COMMENT);

        GoldenLilypad.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");

        GoldenLilypad.SECONDS_BETWEEN_GROWTH_TICKS = configuration.getInt("seconds_between_growth_ticks", category, 47, 1, 9999,
                "Interval between growth ticks in seconds.");
        GoldenLilypad.TILE_RANGE = configuration.getInt("tile_range", category, 4, 1, 15,
                "Range at which the lilypad will operate at partial potency.");
        GoldenLilypad.FULL_POTENCY_RANGE = configuration.getInt("full_potency_range", category, 1, 1, 15,
                "Range at which the lilypad will operate at full potency.");
        GoldenLilypad.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }

    private static void loadGoldenMinerSettings() {
        final String category = ConfigCategories.GOLDEN_MINER;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_MINER_COMMENT);

        GoldenMiner.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");

        GoldenMiner.BLOCKS = Arrays.asList(configuration.getStringList("blocks", category,
                ItemGoldenMiner.defaultBlocks.toArray(new String[]{}),
                "List of blocks which the golden miner is allowed to mine."));
        GoldenMiner.CENTERED_EXPLOSION = configuration.getBoolean("centered_explosion", category, false,
                "Centre the explosion at target block?(otherwise explosion will have starting edge at target block)");
        GoldenMiner.PERFECT_CUBE = configuration.getBoolean("perfect_cube", category, true,
                "Should the explosion be shaped like a perfect cube? (Otherwise it will be a sphere)");
        GoldenMiner.EXPLOSION_RADIUS = configuration.getInt("explosion_radius", category, 1, 1, 5,
                "Radius of the explosion");
        GoldenMiner.COST = configuration.getInt("cost", category, 3, 0, 9999,
                "How many charges will each explosion consume.");
        GoldenMiner.GUNPOWDER_WORTH = configuration.getInt("gunpowder_worth", category, 1, 0, 9999,
                "How many charges is one gunpowder worth.");
        GoldenMiner.GUNPOWDER_LIMIT = configuration.getInt("gunpowder_limit", category, 250, 0, 9999,
                "How many gunpowder may be stored in internal storage.");
        GoldenMiner.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }

    private static void loadGoldenStaffSettings() {
        final String category = ConfigCategories.GOLDEN_STAFF;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_STAFF_COMMENT);

        GoldenStaff.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");

        GoldenStaff.MAX_RANGE = configuration.getInt("max_range", category, 30, 1, 30,
                "How far out the golden staff will place torches.");
        GoldenStaff.MAX_CAPACITY_PER_ITEM_TYPE = configuration.getInt("max_capacity_per_item_type",
                category, 1500, 1, 9999,
                "Maximum number of each type of torch the golden staff can hold.");
        GoldenStaff.TILE_PER_COST_MULTIPLIER = configuration.getInt("tile_per_cost_multiplier", category, 6, 6, 30,
                "Multiplier determining how many extra torches will be consumed per unit of distance from the player.");
        GoldenStaff.TORCHES = Arrays.asList(configuration.getStringList("torches", category, new String[]{},
                "List of items (other than vanilla torch) supported as torches for the golden staff."));
        GoldenStaff.HUD_POSITION = configuration.getInt("hud_position", category, 3, 1, 4, "Position of HUD on screen");
        GoldenStaff.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }

    private static void loadGoldenLanternSettings() {
        final String category = ConfigCategories.GOLDEN_LANTERN;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_LANTERN_COMMENT);

        GoldenLantern.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");

        GoldenLantern.MIN_LIGHT_LEVEL = configuration.getInt("min_light_level", category, 8, 0, 15,
                "Minimum light level before a torch is placed.");
        GoldenLantern.PLACEMENT_SCAN_RADIUS = configuration.getInt("placement_scan_radius", category, 6, 1, 15,
                "How far to scan for torch placement.");
//        TODO should probably implement only_place_on_visible_blocks even though the original didn't
//        Reliquary.CONFIG.require(Names.lantern_of_paranoia, "only_place_on_visible_blocks", new ConfigReference(false));
        GoldenLantern.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");

    }

    private static void loadGoldenCoinSettings() {
        final String category = ConfigCategories.GOLDEN_COIN;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(category, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_COIN_COMMENT);

        GoldenCoin.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");

        GoldenCoin.LONG_PULL_DISTANCE = configuration.getInt("long_pull_distance", category, 15, 0, 64,
                "Distance the coin will pull items whilst being used (holding right mouse button).");
        GoldenCoin.STANDARD_PULL_DISTANCE = configuration.getInt("standard_pull_distance", category, 5, 0, 64,
                "Distance the coin will pull items whilst activated.");
        GoldenCoin.AUDIO_DISABLED = configuration.getBoolean("audio_disabled", category, false,
                "Disable audio when item is activated?");
        GoldenCoin.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }


    private static void loadGoldenTorchSettings() {
        final String category = ConfigCategories.GOLDEN_TORCH;
        LOGGER.debug("Loading configuration category [{}]", category);
        configuration.setCategoryRequiresMcRestart(ConfigCategories.GOLDEN_TORCH, true);
        configuration.setCategoryComment(category, ConfigCategories.GOLDEN_TORCH_COMMENT);

        GoldenTorch.ITEM_ENABLED = configuration.getBoolean("enabled", category, true, "Set to false to disable the item");

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
        List<String> entityBlacklist = Arrays.asList(configuration.getStringList("blacklist", category, new String[]{},
                "List of entities that golden torch should NEVER push"));
        List<String> entityWhitelist = Arrays.asList(configuration.getStringList("whiteList", category, new String[]{},
                "List of entities that golden torch should ALWAYS push"));
        GoldenTorch.interdictionField = new InterdictionField(pushRadius, entityTypeConfiguration,
                entityWhitelist, entityBlacklist);
        GoldenTorch.CHEST_SPAWN_CHANCE = configuration.getInt("chest_probability", category, 1, 0, 100,
                "How likely is this item to spawn in a random chest. Higher number = more spawns. 0 = no spawns.");
    }

    public static List<IConfigElement> getConfigElements() {
        List<IConfigElement> elements = new ArrayList<>();
        for (String categoryName : configuration.getCategoryNames()) {
            elements.add(new ConfigElement(configuration.getCategory(categoryName)));
        }
        return elements;
    }
}
