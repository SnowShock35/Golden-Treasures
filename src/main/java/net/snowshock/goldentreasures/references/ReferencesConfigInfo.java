package net.snowshock.goldentreasures.references;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.snowshock.goldentreasures.interdiction.InterdictionField;

import java.util.List;

public class ReferencesConfigInfo {

    public static final class ConfigCategories {
        public static final String GENERAL = "General";
        public static final String GENERAL_COMMENT = "General configuration options for the mod";
        public static final String GOLDEN_TORCH = "GoldenTorch";
        public static final String GOLDEN_TORCH_COMMENT = "Configuration options for the golden torch.";
        public static final String GOLDEN_COIN ="GoldenCoin";
        public static final String GOLDEN_COIN_COMMENT = "Configuration options for the golden coin.";
        public static final String GOLDEN_LANTERN ="GoldenLantern";
        public static final String GOLDEN_LANTERN_COMMENT = "Configuration options for the golden lantern.";
        public static final String GOLDEN_STAFF ="GoldenStaff";
        public static final String GOLDEN_STAFF_COMMENT = "Configuration options for the golden staff.";
    }

    public static final class GeneralConfigs {
    }

    public static final class GoldenStaff {
        public static int MAX_RANGE = 30;
        public static int TILE_PER_COST_MULTIPLIER = 6;
        public static int MAX_CAPACITY_PER_ITEM_TYPE = 1500;
        public static List<Item> TORCHES = ImmutableList.of();

    }

    public static final class GoldenLantern {
        public static int PLACEMENT_SCAN_RADIUS = 6;
        public static int MIN_LIGHT_LEVEL = 6;
    }

    public static final class GoldenCoin {
        public static int LONG_PULL_DISTANCE = 25;
        public static int STANDARD_PULL_DISTANCE = 5;
        public static boolean AUDIO_DISABLED = false;
    }

    public static final class GoldenTorch {
        public static InterdictionField interdictionField;
    }
}
