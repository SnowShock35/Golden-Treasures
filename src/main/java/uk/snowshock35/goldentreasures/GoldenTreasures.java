package uk.snowshock35.goldentreasures;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import uk.snowshock35.goldentreasures.init.InitModRecipes;
import uk.snowshock35.goldentreasures.utils.MetadataHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static uk.snowshock35.goldentreasures.references.ReferencesModInfo.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, acceptedMinecraftVersions = ACCEPTED_MC_VERSION, guiFactory = GUI_FACTORY_CLASS)
public class GoldenTreasures {

    @Mod.Instance(MOD_ID)
    public static GoldenTreasures instance;

    @Mod.Metadata(MOD_ID)
    public static ModMetadata metadata;

    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs CREATIVE_TAB = new CreativeTabGoldenTreasures(CreativeTabs.getNextID(), MOD_ID);
    public static Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.log(Level.INFO, "Pre Initialization: Starting...");

        File configFile = event.getSuggestedConfigurationFile();
        ConfigHandler.preInit(configFile);

        FMLCommonHandler.instance().bus().register(new ConfigHandler());

        metadata = MetadataHelper.transformMetadata(metadata);

        proxy.preInit();

        LOGGER.log(Level.INFO, "Pre Initialization: Complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.log(Level.INFO, "Initialization: Starting...");
        proxy.init();
        LOGGER.log(Level.INFO, "Initialization: Complete");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LOGGER.log(Level.INFO, "Post Initialization: Starting...");
        proxy.postInit();
        LOGGER.log(Level.INFO, "Post Initialization: Complete");
    }
}
