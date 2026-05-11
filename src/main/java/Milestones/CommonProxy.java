package Milestones;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import Milestones.Commands.CommandMilestones;
import Milestones.Configs.ConfigMilestones;
import Milestones.Configs.ConfigRegister;
import Milestones.Configs.ConfigServer;
import Milestones.Events.PlayerLoggedInEventHandler;
import Milestones.ItemBlock.TrophyItemBlock;
import Milestones.Packets.PacketOpenMilestones;
import Milestones.SaveData.CompletedMilestonesCacheSaveData;
import Milestones.TileEntity.TrophyTileEntity;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Milestones.milestonesList = parseConfig();

        Milestones.network = NetworkRegistry.INSTANCE.newSimpleChannel(Milestones.MODID);

        if (ConfigServer.enableTrophies) {
            GameRegistry.registerBlock(Milestones.trophyBlock, TrophyItemBlock.class, "trophy");
            GameRegistry.registerTileEntity(TrophyTileEntity.class, "trophy");
        }

        Milestones.network.registerMessage(PacketOpenMilestones.Handler.class, PacketOpenMilestones.class, 0, Side.SERVER);
    }

    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance()
            .bus()
            .register(new PlayerLoggedInEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMilestones());

        CompletedMilestonesCacheSaveData.get();
    }

    private Set<String> parseConfig(){
        Set<String> result = new HashSet<>();

        for (String entry : ConfigMilestones.items) {
            if (entry.charAt(0) == '$' || entry.charAt(0) == '^'){
                continue;
            }
            result.add(entry.split("#")[0].trim());
        }

        return result;
    }
}
