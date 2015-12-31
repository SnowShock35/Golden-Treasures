package net.snowshock.goldentreasures;

import cpw.mods.fml.common.FMLCommonHandler;
import net.snowshock.goldentreasures.blocks.BlockGoldenTorch;
import net.snowshock.goldentreasures.references.ReferencesConfigInfo;

public class CommonProxy {
    public void init() {
        FMLCommonHandler.instance().bus().register(new BlockGoldenTorch(ReferencesConfigInfo.GoldenTorch.interdictionField));
    }

    public void preInit()
    {

    }
}
