package ModName.GUI;

import ModName.ModName;
import com.cleanroommc.modularui.api.UIFactory;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class GUIFactoryMilestones implements UIFactory<GUIDataMilestones> {
    @Override
    public String getFactoryName() {
        return "milestonesguifactory";
    }

    @Override
    public ModularPanel createPanel(GUIDataMilestones guiData, PanelSyncManager syncManager, UISettings settings) {
        return new ModularPanel("milestonesgui")
            .child(new TextWidget<>("Some text"))
            .pos(10, 10);
    }

    @Override
    public ModularScreen createScreen(GUIDataMilestones guiData, ModularPanel mainPanel) {
        return new ModularScreen(ModName.MODID, mainPanel);
    }

    @Override
    public void writeGuiData(GUIDataMilestones guiData, PacketBuffer buffer) {

    }

    @Override
    public GUIDataMilestones readGuiData(EntityPlayer player, PacketBuffer buffer) {
        return new GUIDataMilestones(player);
    }
}
