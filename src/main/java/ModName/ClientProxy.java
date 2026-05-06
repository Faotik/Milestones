package ModName;

import ModName.Configs.ConfigRegister;
import ModName.Events.GuiScreenEventHandler;
import ModName.GUI.GUIFactoryMilestones;
import com.cleanroommc.modularui.factory.GuiManager;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    public void init(FMLInitializationEvent event) {
        ConfigRegister.init();
        GuiManager.registerFactory(new GUIFactoryMilestones());
        MinecraftForge.EVENT_BUS.register(new GuiScreenEventHandler());
    }

}
