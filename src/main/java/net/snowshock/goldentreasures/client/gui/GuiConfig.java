package net.snowshock.goldentreasures.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.snowshock.goldentreasures.ConfigHandler;
import net.snowshock.goldentreasures.references.ReferencesModInfo;

public class GuiConfig extends cpw.mods.fml.client.config.GuiConfig {

    public GuiConfig(GuiScreen guiScreen) {
        super(guiScreen, ConfigHandler.getConfigElements(), ReferencesModInfo.MOD_ID, true, true, "Golden Treasures Configuration");
    }
}
