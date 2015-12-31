package net.snowshock.goldentreasures;

import net.snowshock.goldentreasures.utils.LanguageHelper;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();
    }

    @Override
    public void preInit()
    {
        LanguageHelper.setupColors();
    }
}
