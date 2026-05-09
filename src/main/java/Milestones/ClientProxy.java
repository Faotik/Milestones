package Milestones;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.cleanroommc.modularui.factory.GuiManager;

import Milestones.Configs.ConfigServer;
import Milestones.Events.GuiScreenEventHandler;
import Milestones.GUI.GUIFactoryMilestones;
import Milestones.ItemRenderer.TrophyItemRenderer;
import Milestones.TESR.TrophyTESR;
import Milestones.TileEntity.TrophyTileEntity;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        if (ConfigServer.enableTrophies) {
            ClientRegistry.bindTileEntitySpecialRenderer(TrophyTileEntity.class, new TrophyTESR());
        }
        GuiManager.registerFactory(new GUIFactoryMilestones());
    }

    public void init(FMLInitializationEvent event) {
        super.init(event);

        if (ConfigServer.enableTrophies) {
            MinecraftForgeClient
                .registerItemRenderer(Item.getItemFromBlock(Milestones.trophyBlock), new TrophyItemRenderer());
        }
        MinecraftForge.EVENT_BUS.register(new GuiScreenEventHandler());
    }

}
