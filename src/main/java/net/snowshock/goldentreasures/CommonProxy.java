package net.snowshock.goldentreasures;

import cpw.mods.fml.common.FMLCommonHandler;
import net.snowshock.goldentreasures.blocks.BlockGoldenTorch;

public class CommonProxy {
    public void init() {
        FMLCommonHandler.instance().bus().register(new BlockGoldenTorch());
    }
}
