package uk.snowshock35.goldentreasures.references;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ChestGenHooks;
import uk.snowshock35.goldentreasures.interdiction.InterdictionField;
import uk.snowshock35.goldentreasures.items.ItemGoldenMiner;

import java.util.Arrays;
import java.util.List;

public class ReferencesConfigInfo {

    public static final class ConfigCategories {
        public static final String GENERAL = "General";
        public static final String GENERAL_COMMENT = "General configuration options for the mod";
        public static final String GOLDEN_TORCH = "Items.GoldenTorch";
        public static final String GOLDEN_TORCH_COMMENT = "Configuration options for the golden torch.";
        public static final String GOLDEN_COIN = "Items.GoldenCoin";
        public static final String GOLDEN_COIN_COMMENT = "Configuration options for the golden coin.";
        public static final String GOLDEN_LANTERN = "Items.GoldenLantern";
        public static final String GOLDEN_LANTERN_COMMENT = "Configuration options for the golden lantern.";
        public static final String GOLDEN_STAFF = "Items.GoldenStaff";
        public static final String GOLDEN_STAFF_COMMENT = "Configuration options for the golden staff.";
        public static final String GOLDEN_MINER = "Items.GoldenMiner";
        public static final String GOLDEN_MINER_COMMENT = "Configuration options for the golden miner.";
        public static final String GOLDEN_LILYPAD = "Items.GoldenLilypad";
        public static final String GOLDEN_LILYPAD_COMMENT = "Configuration options for the golden lilypad.";
        public static final String GOLDEN_CHALICE = "Items.GoldenChalice";
        public static final String GOLDEN_CHALICE_COMMENT = "Configuration options for the golden chalice.";
        public static final String GOLDEN_FOOD = "Items.GoldenFood";
        public static final String GOLDEN_FOOD_COMMENT = "Configuration options for the golden food.";
        public static final String GOLDEN_BOMB = "Items.GoldenBomb";
        public static final String GOLDEN_BOMB_COMMENT = "Configuration options for the golden bomb.";
        public static final String GOLDEN_FEATHER = "Items.GoldenFeather";
        public static final String GOLDEN_FEATHER_COMMENT = "Configuration options for the Golden Feather.";
    }

    public static final class GeneralConfigs {
        public static int NUM_CRAFTING_COMPONENTS = 3;
        public static List<String> DUNGEON_SPAWN_TYPES =
                Arrays.asList(ChestGenHooks.DUNGEON_CHEST,
                        ChestGenHooks.BONUS_CHEST,
                        ChestGenHooks.VILLAGE_BLACKSMITH,
                        ChestGenHooks.STRONGHOLD_LIBRARY,
                        ChestGenHooks.STRONGHOLD_CORRIDOR,
                        ChestGenHooks.STRONGHOLD_CROSSING,
                        ChestGenHooks.PYRAMID_JUNGLE_CHEST,
                        ChestGenHooks.PYRAMID_DESERT_CHEST,
                        ChestGenHooks.MINESHAFT_CORRIDOR);
    }


    public static final class GoldenFeather {
        public static boolean ITEM_ENABLED = true;
        public static int LEAPING_POTENCY = 1;
        public static float HUNGER_MULTIPLIER = 3.0F;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenFood {
        public static boolean ITEM_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenBomb {
        public static boolean ITEM_ENABLED = true;
        public static boolean DISPENSER_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenChalice {
        public static int HUNGER_SATURATION_MULTIPLIER = 4;
        public static boolean ITEM_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenLilypad {
        public static int SECONDS_BETWEEN_GROWTH_TICKS = 47;
        public static int TILE_RANGE = 4;
        public static int FULL_POTENCY_RANGE = 1;
        public static boolean ITEM_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenMiner {
        public static int HUD_POSITION = 3;
        public static List<String> BLOCKS = ItemGoldenMiner.defaultBlocks;
        public static boolean CENTERED_EXPLOSION = false;
        public static int EXPLOSION_RADIUS = 1;
        public static boolean PERFECT_CUBE = true;
        public static int COST = 3;
        public static int GUNPOWDER_WORTH = 1;
        public static int GUNPOWDER_LIMIT = 250;
        public static boolean ITEM_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenStaff {
        public static int MAX_RANGE = 30;
        public static int TILE_PER_COST_MULTIPLIER = 6;
        public static int MAX_CAPACITY_PER_ITEM_TYPE = 1500;
        public static List<String> TORCHES = ImmutableList.of();
        public static int HUD_POSITION = 3;
        public static boolean ITEM_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenLantern {
        public static int PLACEMENT_SCAN_RADIUS = 6;
        public static int MIN_LIGHT_LEVEL = 6;
        public static boolean ITEM_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenCoin {
        public static int LONG_PULL_DISTANCE = 25;
        public static int STANDARD_PULL_DISTANCE = 5;
        public static boolean AUDIO_DISABLED = false;
        public static boolean ITEM_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }

    public static final class GoldenTorch {
        public static InterdictionField interdictionField;
        public static boolean ITEM_ENABLED = true;
        public static int CHEST_SPAWN_CHANCE = 1;
    }
}
