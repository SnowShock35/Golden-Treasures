package net.snowshock.goldentreasures.references;

import net.snowshock.goldentreasures.interdiction.InterdictionField;

public class ReferencesConfigInfo {

    public static final class ConfigCategories {
        public static final String GENERAL = "General";
        public static final String GENERAL_COMMENT = "General configuration options for the mod";
        public static final String GOLDEN_TORCH = "GoldenTorch";
        public static final String GOLDEN_TORCH_COMMENT = "Configuration options for the golden torch.";
        public static final String GOLDEN_COIN ="GoldenCoin";
        public static final String GOLDEN_COIN_COMMENT = "Configuration options for the golden coin.";
    }

    public static final class GeneralConfigs {
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
