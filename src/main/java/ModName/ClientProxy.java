package ModName;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.cleanroommc.modularui.factory.GuiManager;

import ModName.Configs.ConfigServer;
import ModName.Events.GuiScreenEventHandler;
import ModName.GUI.GUIFactoryMilestones;
import ModName.ItemRenderer.TrophyItemRenderer;
import ModName.TESR.TrophyTESR;
import ModName.TileEntity.TrophyTileEntity;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        if (ConfigServer.enableTrophies) {
            ClientRegistry.bindTileEntitySpecialRenderer(TrophyTileEntity.class, new TrophyTESR());
        }
    }

    public void init(FMLInitializationEvent event) {
        super.init(event);

        if (ConfigServer.enableTrophies) {
            MinecraftForgeClient
                .registerItemRenderer(Item.getItemFromBlock(ModName.trophyBlock), new TrophyItemRenderer());
        }
        GuiManager.registerFactory(new GUIFactoryMilestones());
        MinecraftForge.EVENT_BUS.register(new GuiScreenEventHandler());
    }

}
