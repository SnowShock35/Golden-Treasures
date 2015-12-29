package net.snowshock.goldentreasures;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import net.snowshock.goldentreasures.references.ReferencesConfigInfo;
import net.snowshock.goldentreasures.references.ReferencesModInfo;

import java.io.File;

//import net.snowshock.GoldenTreasures.blocks.BlockGoldenTorch;

public class ConfigHandler {
    public static Configuration configuration;

    public static void init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
    }

    private static void loadConfiguration() {
        // General config category and
        configuration.setCategoryComment(ReferencesConfigInfo.ConfigCategories.GENERAL, ReferencesConfigInfo.ConfigCategories.GENERAL_COMMENT);
        configuration.setCategoryRequiresMcRestart(ReferencesConfigInfo.ConfigCategories.GENERAL, true);

        ReferencesConfigInfo.GeneralConfigs.PUSH_RADIUS = configuration.getInt("Push Radius", ReferencesConfigInfo.ConfigCategories.GENERAL, 5, 1, 10, null);
        ReferencesConfigInfo.GeneralConfigs.CAN_PROJECTILES_BE_PUSHED = configuration.getBoolean("Can projectiles be pushed", ReferencesConfigInfo.ConfigCategories.GENERAL, false, null);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(ReferencesModInfo.MOD_ID)) {
            loadConfiguration();
        }
    }
}
