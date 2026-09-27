package Milestones;

import static Milestones.Utils.getFluidStackFromId;
import static Milestones.Utils.getItemStackFromId;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import Milestones.Commands.CommandMilestones;
import Milestones.Configs.ConfigMilestones;
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
        Milestones.network = NetworkRegistry.INSTANCE.newSimpleChannel(Milestones.MODID);

        if (ConfigServer.enableTrophies) {
            GameRegistry.registerBlock(Milestones.trophyBlock, TrophyItemBlock.class, "trophy");
            GameRegistry.registerTileEntity(TrophyTileEntity.class, "trophy");
        }

        Milestones.network
            .registerMessage(PacketOpenMilestones.Handler.class, PacketOpenMilestones.class, 0, Side.SERVER);
    }

    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance()
            .bus()
            .register(new PlayerLoggedInEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        parseConfig();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMilestones());

        CompletedMilestonesCacheSaveData.get();
    }

    private void parseConfig() {
        for (String entry : ConfigMilestones.items) {
            if (entry.charAt(0) == '$' || entry.charAt(0) == '^') {
                continue;
            }
            String id = entry.split("#")[0].trim();
            if (!id.isBlank()) {
                ItemStack itemStack = getItemStackFromId(id);
                if (itemStack != null) {
                    Milestones.milestonesIdToItemStack.put(id, itemStack);
                } else {
                    FluidStack fluidStack = getFluidStackFromId(id);
                    if (fluidStack != null) {
                        Milestones.milestonesIdToFluidStack.put(id, fluidStack);
                    } else {
                        continue;
                    }
                }

                if (id.split(":").length < 2) {
                    id += ":0";
                }

                Milestones.milestonesId.add(id);
            }
        }
    }
}
