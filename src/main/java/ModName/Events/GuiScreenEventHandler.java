package ModName.Events;

import ModName.Configs.ConfigMilestones;
import ModName.ModName;
import ModName.Packets.PacketOpenMilestones;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;

public class GuiScreenEventHandler {
    private static final int ACHIEVEMENTS_BUTTON_ID = 5;
    private static final int MILESTONES_BUTTON_ID = 200;

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (ConfigMilestones.ui.replaceAchievementButton){
            if (event.gui instanceof GuiIngameMenu) {
                GuiButton achievementsBtn = null;

                for (Object obj : event.buttonList) {
                    GuiButton btn = (GuiButton) obj;
                    if (btn.id == ACHIEVEMENTS_BUTTON_ID) {
                        achievementsBtn = btn;
                        break;
                    }
                }

                if (achievementsBtn != null) {
                    event.buttonList.remove(achievementsBtn);

                    event.buttonList.add(
                        new GuiButton(
                            MILESTONES_BUTTON_ID,
                            achievementsBtn.xPosition,
                            achievementsBtn.yPosition,
                            achievementsBtn.width,
                            achievementsBtn.height,
                            "Milestones"
                        )
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (ConfigMilestones.ui.replaceAchievementButton) {
            if (event.gui instanceof GuiIngameMenu) {
                if (event.button.id == MILESTONES_BUTTON_ID) {
                    ModName.network.sendToServer(new PacketOpenMilestones());
                    Minecraft.getMinecraft().displayGuiScreen(null);
                }
            }
        }
    }
}
