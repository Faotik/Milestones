package ModName.GUI;

import ModName.Configs.ConfigMilestones;
import ModName.ModName;
import com.cleanroommc.modularui.api.UIFactory;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GUIFactoryMilestones implements UIFactory<GUIDataMilestones> {
    private static final long TICKS_IN_SECOND = 20;
    private static final long TICKS_IN_MINUTES = TICKS_IN_SECOND * 60;
    private static final long TICKS_IN_HOURS = TICKS_IN_MINUTES * 60;

    @Override
    public String getFactoryName() {
        return "milestonesguifactory";
    }

    @Override
    public ModularPanel createPanel(GUIDataMilestones guiData, PanelSyncManager syncManager, UISettings settings) {
        final int panelWidth = 400;
        final int panelHeight = 300;
        final int columnOffset = 10;
        final int rowOffset = 10;
        final int columnCount = 4;
        final int columnWidth = (panelWidth - columnOffset * 2) / columnCount;
        final int rowHeight = 24;
        final int textOffsetX = 20;
        final int textOffsetY = 4;

        int columnIndex = 0;
        int rowIndex = 0;

        ModularPanel panel = new ModularPanel("milestonesgui")
            .size(panelWidth, panelHeight);

        List<String> allMilestones = Arrays.asList(ConfigMilestones.milestones.items);

        for (var milestone : allMilestones) {
            String[] parts = milestone.split(":");
            String modid = parts[0];
            String name = parts[1];
            int meta = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

            Item item = GameRegistry.findItem(modid, name);
            ItemStack stack = new ItemStack(item, 1, meta);
            var new
                panel.child(
                new Scrol
            )
            panel.child(
                new ItemDisplayWidget()
                    .item(stack)
                    .pos(columnIndex * columnWidth + columnOffset, rowIndex * rowHeight + rowOffset)
            );

            String time = "Incomplete";
            if (guiData.completedMilestones != null && guiData.completedMilestones.hasKey(milestone)) {
                time = EnumChatFormatting.GREEN + getTotalWorldTimeString(guiData.completedMilestones.getLong(milestone));
            }
            panel.child(
                new TextWidget<>(time)
                    .pos(columnIndex * columnWidth + columnOffset + textOffsetX, rowIndex * rowHeight + rowOffset + textOffsetY)
            );

            columnIndex++;
            if (columnIndex > columnCount - 1) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        return panel;
    }

    private String getTotalWorldTimeString(long totalWorldTimeTicks) {
        String totalWorldTime;
        if (totalWorldTimeTicks < TICKS_IN_MINUTES) {
            totalWorldTime = (totalWorldTimeTicks / TICKS_IN_SECOND) + "s";
        }
        else if (totalWorldTimeTicks < TICKS_IN_HOURS) {
            totalWorldTime = (totalWorldTimeTicks / TICKS_IN_MINUTES) + "m";
        }
        else {
            totalWorldTime = (totalWorldTimeTicks / TICKS_IN_HOURS) + "h";
        }
        return totalWorldTime;
    }

    @Override
    public ModularScreen createScreen(GUIDataMilestones guiData, ModularPanel mainPanel) {
        return new ModularScreen(ModName.MODID, mainPanel);
    }

    @Override
    public void writeGuiData(GUIDataMilestones guiData, PacketBuffer buffer) {
        try {
            buffer.writeNBTTagCompoundToBuffer(guiData.completedMilestones);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GUIDataMilestones readGuiData(EntityPlayer player, PacketBuffer buffer) {
        GUIDataMilestones data = new GUIDataMilestones(player);
        try {
            data.completedMilestones = buffer.readNBTTagCompoundFromBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
