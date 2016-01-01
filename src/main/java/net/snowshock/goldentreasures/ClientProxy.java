package net.snowshock.goldentreasures;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.snowshock.goldentreasures.client.HudRenderer;
import net.snowshock.goldentreasures.client.RenderThrown;
import net.snowshock.goldentreasures.entity.EntityGoldenBomb;
import net.snowshock.goldentreasures.utils.LanguageHelper;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenBomb;


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
        if (GoldenBomb.ITEM_ENABLED)
            RenderingRegistry.registerEntityRenderingHandler(EntityGoldenBomb.class, new RenderThrown(12));
    }
}