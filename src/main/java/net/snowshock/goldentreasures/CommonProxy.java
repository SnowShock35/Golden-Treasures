package net.snowshock.goldentreasures;

import net.snowshock.goldentreasures.init.InitModBlocks;
import net.snowshock.goldentreasures.init.InitModEntities;
import net.snowshock.goldentreasures.init.InitModItems;

public class CommonProxy {

    public void init() {
        InitModEntities.init();
    }

    public void preInit() {
        InitModItems.preInit();
        InitModBlocks.preInit();
    }
}
