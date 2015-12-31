package net.snowshock.goldentreasures;

import cpw.mods.fml.common.FMLCommonHandler;
import net.snowshock.goldentreasures.client.HudRenderer;
import net.snowshock.goldentreasures.utils.LanguageHelper;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();

        FMLCommonHandler.instance().bus().register(new HudRenderer());
    }

    @Override
    public void preInit() {
        super.preInit();
        LanguageHelper.setupColors();
    }
}
