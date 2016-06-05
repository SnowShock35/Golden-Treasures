package uk.snowshock35.goldentreasures;

import uk.snowshock35.goldentreasures.init.InitModBlocks;
import uk.snowshock35.goldentreasures.init.InitModEntities;
import uk.snowshock35.goldentreasures.init.InitModItems;
import uk.snowshock35.goldentreasures.init.InitModRecipes;

public class CommonProxy {

    public void init() {
        InitModEntities.init();
        InitModItems.initDungeonLoot();
    }

    public void preInit() {
        InitModItems.preInit();
        InitModBlocks.preInit();
    }

    public void postInit() {
        InitModRecipes.postInit();
    }
}
