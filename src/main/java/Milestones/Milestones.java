package Milestones;

import java.util.*;

import net.minecraft.block.Block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Milestones.BlockContainer.TrophyBlockContainer;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Milestones.MODID, version = Tags.VERSION, name = Milestones.MODNAME, acceptedMinecraftVersions = "[1.7.10]")
public class Milestones {

    public static final String MODID = "milestones";
    public static final String COMPACT_MODID = "ms";
    public static final String MODNAME = "Milestones";
    public static final Logger LOG = LogManager.getLogger(MODID);

    public static SimpleNetworkWrapper network;
    public static Block trophyBlock = new TrophyBlockContainer();

    public static Set<String> milestonesList;
    public static final Map<UUID, Set<String>> completedMilestonesCache = new HashMap<>();

    @SidedProxy(clientSide = "Milestones.ClientProxy", serverSide = "Milestones.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
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
