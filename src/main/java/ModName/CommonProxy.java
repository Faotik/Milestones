package ModName;

import ModName.Commands.CommandMilestones;
import ModName.Configs.ConfigRegister;
import ModName.GUI.GUIFactoryMilestones;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.GuiManager;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent event) {
        ConfigRegister.init();
        GuiManager.registerFactory(new GUIFactoryMilestones());
    }

    public void postInit(FMLPostInitializationEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMilestones());
    }
}
