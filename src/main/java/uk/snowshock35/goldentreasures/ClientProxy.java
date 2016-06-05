package uk.snowshock35.goldentreasures;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import uk.snowshock35.goldentreasures.client.HudRenderer;
import uk.snowshock35.goldentreasures.client.RenderThrown;
import uk.snowshock35.goldentreasures.entity.EntityGoldenBomb;
import uk.snowshock35.goldentreasures.utils.LanguageHelper;
import uk.snowshock35.goldentreasures.references.ReferencesConfigInfo;


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
        if (ReferencesConfigInfo.GoldenBomb.ITEM_ENABLED)
            RenderingRegistry.registerEntityRenderingHandler(EntityGoldenBomb.class, new RenderThrown(12));
    }
}