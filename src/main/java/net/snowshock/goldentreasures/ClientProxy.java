package net.snowshock.goldentreasures;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.snowshock.goldentreasures.client.HudRenderer;
import net.snowshock.goldentreasures.client.RenderThrown;
import net.snowshock.goldentreasures.entity.EntityGoldenBomb;
import net.snowshock.goldentreasures.utils.LanguageHelper;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();

        FMLCommonHandler.instance().bus().register(new HudRenderer());
        this.registerRenderers();
    }

    @Override
    public void preInit() {
        super.preInit();
        LanguageHelper.setupColors();
    }

    protected void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityGoldenBomb.class, new RenderThrown(12));
    }
}