package uk.snowshock35.goldentreasures.client.gui;

import net.minecraft.client.gui.GuiScreen;
import uk.snowshock35.goldentreasures.ConfigHandler;
import uk.snowshock35.goldentreasures.references.ReferencesModInfo;

public class GuiConfig extends cpw.mods.fml.client.config.GuiConfig {

    public GuiConfig(GuiScreen guiScreen) {
        super(guiScreen, ConfigHandler.getConfigElements(), ReferencesModInfo.MOD_ID, true, true, "Golden Treasures Configuration");
    }
}
