package ModName.GUI;

import ModName.Configs.ConfigMilestones;
import ModName.ModName;
import ModName.UI.HorizontalHiddenScrollData;
import com.cleanroommc.modularui.api.UIFactory;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.ScrollWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.scroll.HorizontalScrollData;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Unique;

import java.io.IOException;
import java.util.ArrayList;
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
        final int textOffsetX = 21;
        final int textOffsetY = 4;
        final int sectionTitleOffsetX = 0;
        final int sectionTitleOffsetY = 8;
        final int tabTitleOffsetY = 10;
        final int tabOffsetX = 2;
        final int tabOffsetY = -26;
        final int tabOffsetBetween = 8;
        final int tabSize = 22;
        final int tabGridHeight = 27;
        final int tabIconSize = 18;
        final int tabPadding = (22 - tabIconSize) / 2;
        final int milestoneWidth = columnWidth - 10;
        final int milestoneHeight = 22;
        final int milestonePadding = 2;

        int columnIndex = 0;
        int rowIndex = 0;
        int tabIndex = 0;

        ModularPanel panel = new ModularPanel("milestonesgui")
            .size(panelWidth, panelHeight);

        String[] allMilestones = ConfigMilestones.milestones.items;

        List<Widget<?>> tabPanels = new ArrayList<>();
        List<Widget<?>> tabScrollbars = new ArrayList<>();

        HorizontalScrollData scrollData = new HorizontalHiddenScrollData(true);
        scrollData.texture(IDrawable.EMPTY);
        scrollData.setScrollSize(2);

        Grid tabGrid = new Grid()
            .size(panelWidth - tabOffsetX * 2, tabGridHeight)
            .pos(tabOffsetX, tabOffsetY)
            .minColWidth(tabSize)
            .minElementMargin(tabPadding)
            .scrollable(scrollData);

        panel.child(
            ButtonWidget.panelCloseButton()
        );

        ListWidget tab = null;

        for (var milestone : allMilestones) {
            if (milestone.charAt(0) == '$') {
                if (tab != null) {
                    tab.disabled();
                    tabPanels.add(tab);
                    panel.child(tab);
                    columnIndex = 0;
                    rowIndex = 0;
                }
                tab = new ListWidget<>()
                    .full();

                String[] parts = milestone.split(",");
                String[] partsItem = parts[1].split(":");
                String title = parts[0].substring(1);
                String modid = partsItem[0];
                String name = partsItem[1];
                int meta = partsItem.length > 2 ? Integer.parseInt(partsItem[2]) : 0;

                Item item = GameRegistry.findItem(modid, name);
                ItemStack stack = new ItemStack(item, 1, meta);

                int offsetX = tabIndex == 0 ? tabOffsetX : tabOffsetBetween;
                final int finalTabIndex = tabIndex;

                tabGrid.child(
                    new ButtonWidget<>()
                        .size(tabSize)
                        .padding(tabPadding)
                        .overlay(new ItemDrawable(stack))
                        .onMousePressed(mouseButton -> {
                            if (mouseButton == 0 || mouseButton == 1) {
                                for (int i = 0; i < tabPanels.size(); i++) {
                                    tabPanels.get(i).setEnabled(i == finalTabIndex);
                                }
                                return true;
                            }
                            return false;
                        })
                );

                tabIndex++;

                tab.child(
                    new TextWidget<>(title)
                        .posRel(Alignment.TopCenter)
                        .marginTop(tabTitleOffsetY)
                );
            } else if (milestone.charAt(0) == '^') {
                columnIndex = 0;
                if(rowIndex != 0) {
                    rowIndex++;
                }

                tab.child(
                    new TextWidget<>(milestone.substring(1))
                        .pos(columnOffset + sectionTitleOffsetX, rowIndex * rowHeight + rowOffset + sectionTitleOffsetY)
                );

                rowIndex++;
            }
            else {
                String[] parts = milestone.split(":");
                String modid = parts[0];
                String name = parts[1];
                int meta = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

                Item item = GameRegistry.findItem(modid, name);
                ItemStack stack = new ItemStack(item, 1, meta);

                String timeText = "Incomplete";
                if (guiData.completedMilestones != null && guiData.completedMilestones.hasKey(milestone)) {
                    timeText = EnumChatFormatting.GREEN + getTotalWorldTimeString(guiData.completedMilestones.getLong(milestone));
                }

                tab.child(
                    new ListWidget<>()
                        .pos(columnIndex * columnWidth + columnOffset, rowIndex * rowHeight + rowOffset)
                        .size(milestoneWidth, milestoneHeight)
                        .background(GuiTextures.MC_BUTTON)
                        .child(
                            new ItemDisplayWidget()
                                .item(stack)
                                .pos(milestonePadding, milestonePadding)
                        )
                        .child(
                            new TextWidget<>(timeText)
                                .pos(textOffsetX + milestonePadding, textOffsetY + milestonePadding)
                        )
                );

                columnIndex++;
                if (columnIndex > columnCount - 1) {
                    columnIndex = 0;
                    rowIndex++;
                }
            }
        }
        tab.disabled();
        tabPanels.add(tab);
        panel.child(tab);
        tabPanels.get(0).setEnabled(true);

        panel.child(tabGrid);

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
