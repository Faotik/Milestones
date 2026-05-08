package ModName;

import ModName.BlockContainer.TrophyBlockContainer;
import ModName.Commands.CommandMilestones;
import ModName.Configs.ConfigMilestones;
import ModName.Configs.ConfigRegister;
import ModName.Events.PlayerLoggedInEventHandler;
import ModName.GUI.GUIFactoryMilestones;
import ModName.Packets.PacketOpenMilestones;
import ModName.TileEntity.TrophyTileEntity;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.GuiManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;

import java.util.Arrays;
import java.util.HashSet;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ConfigRegister.init();
        ModName.milestonesList = new HashSet<>(Arrays.asList(ConfigMilestones.milestones.items));

        ModName.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModName.MODID);

        GameRegistry.registerBlock(ModName.trophyBlock, "trophy");
        GameRegistry.registerTileEntity(TrophyTileEntity.class, "trophy");

        ModName.network.registerMessage(PacketOpenMilestones.Handler.class, PacketOpenMilestones.class, 0, Side.SERVER);
    }

    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new PlayerLoggedInEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMilestones());
    }
}
