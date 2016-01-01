package net.snowshock.goldentreasures;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.utils.MetadataHelper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = ReferencesModInfo.MOD_ID, name = ReferencesModInfo.MOD_NAME, version = ReferencesModInfo.VERSION, acceptedMinecraftVersions = "[1.7.10]")
public class GoldenTreasures {

    @Mod.Instance(ReferencesModInfo.MOD_ID)
    public static GoldenTreasures instance;

    @Mod.Metadata(ReferencesModInfo.MOD_ID)
    public static ModMetadata metadata;

    @SidedProxy(clientSide = ReferencesModInfo.CLIENT_PROXY_CLASS, serverSide = ReferencesModInfo.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs CREATIVE_TAB = new CreativeTabGoldenTreasures(CreativeTabs.getNextID(), ReferencesModInfo.MOD_ID);
    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);

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
        if (Minecraft.isRunningOnMac) {
            LOGGER.log(Level.DEBUG, "Warning: Pointless Message!");
            LOGGER.log(Level.ERROR, "Mac User Detected! Exterminate! Exterminate! Exterminate!");
        }
        LOGGER.log(Level.INFO, "Post Initialization: Complete");
    }
}
