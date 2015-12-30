package net.snowshock.goldentreasures.references;

import net.snowshock.goldentreasures.interdiction.InterdictionField;

public class ReferencesConfigInfo {

    public static final class ConfigCategories {
        public static final String GENERAL = "General";
        public static final String GENERAL_COMMENT = "General configuration options for the mod";
        public static final String GOLDEN_TORCH = "GoldenTorch";
        public static final String GOLDEN_TORCH_COMMENT = "Configuration options for the golden torch.";
    }

    public static final class GeneralConfigs {
    }

    public static final class GoldenTorch {
        public static InterdictionField interdictionField;
    }
}
