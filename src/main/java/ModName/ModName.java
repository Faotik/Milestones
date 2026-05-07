package ModName;

import ModName.BlockContainer.TrophyBlockContainer;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModName.MODID, version = Tags.VERSION, name = ModName.MODNAME, acceptedMinecraftVersions = "[1.7.10]")
public class ModName {

    public static final String MODID = "modname";
    public static final String COMPACT_MODID = "mn";
    public static final String MODNAME = "ModName";
    public static final Logger LOG = LogManager.getLogger(MODID);

    public static SimpleNetworkWrapper network;
    public static Block trophyBlock = new TrophyBlockContainer();

    @SidedProxy(clientSide = "ModName.ClientProxy", serverSide = "ModName.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
