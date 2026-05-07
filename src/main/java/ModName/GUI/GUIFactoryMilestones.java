    package ModName.GUI;

    import ModName.Configs.ConfigMilestones;
    import ModName.ModName;
    import ModName.UI.HorizontalHiddenScrollData;
    import com.cleanroommc.modularui.api.UIFactory;
    import com.cleanroommc.modularui.drawable.GuiTextures;
    import com.cleanroommc.modularui.drawable.ItemDrawable;
    import com.cleanroommc.modularui.screen.ModularPanel;
    import com.cleanroommc.modularui.screen.ModularScreen;
    import com.cleanroommc.modularui.screen.UISettings;
    import com.cleanroommc.modularui.utils.Alignment;
    import com.cleanroommc.modularui.value.sync.PanelSyncManager;
    import com.cleanroommc.modularui.widget.Widget;
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

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;

    public class GUIFactoryMilestones implements UIFactory<GUIDataMilestones> {
        private static final long TICKS_IN_SECOND = 20;
        private static final long TICKS_IN_MINUTES = TICKS_IN_SECOND * 60;
        private static final long TICKS_IN_HOURS = TICKS_IN_MINUTES * 60;

        final int panelWidth = 400;
        final int panelHeight = 300;
        final int columnOffset = 10;
        final int rowOffset = 10;
        final int columnCount = 4;
        final int columnWidth = (panelWidth - columnOffset * 2) / columnCount;
        final int rowHeight = 24;
        final int textMarginLeft = 22;
        final int sectionTitleOffsetX = 0;
        final int sectionTitleOffsetY = 8;
        final int tabTitleOffsetY = 10;
        final int tabOffsetX = 2;
        final int tabOffsetY = -26;
        final int tabSize = 22;
        final int tabGridHeight = 27;
        final int tabIconSize = 18;
        final int tabPadding = (22 - tabIconSize) / 2;
        final int milestoneWidth = columnWidth - 10;
        final int milestoneHeight = 22;
        final int milestonePadding = 2;

        @Override
        public String getFactoryName() {
            return "milestonesguifactory";
        }

        @Override
        public ModularPanel createPanel(GUIDataMilestones guiData, PanelSyncManager syncManager, UISettings settings) {
            int columnIndex = 0;
            int rowIndex = 0;
            int tabIndex = 0;

            ModularPanel panel = new ModularPanel("milestonesgui")
                .size(panelWidth, panelHeight);

            String[] allMilestones = guiData.allMilestones;

            List<Widget<?>> tabPanels = new ArrayList<>();
            List<Widget<?>> tabButtons = new ArrayList<>();

            Grid tabGrid = new Grid()
                .size(panelWidth - tabOffsetX * 2, tabGridHeight)
                .pos(tabOffsetX, tabOffsetY)
                .minColWidth(tabSize)
                .minElementMargin(tabPadding)
                .scrollable(new HorizontalHiddenScrollData(true));

            panel.child(
                ButtonWidget.panelCloseButton()
            );

            ListWidget tab = null;
            Grid row = createRow();

            for (var milestone : allMilestones) {
                if (milestone.charAt(0) == '$') {
                    if (tab != null) {
                        if (!row.getChildren().isEmpty()) {
                            tab.child(row);
                            row = createRow();
                        }
                        tab.disabled();
                        tabPanels.add(tab);
                        panel.child(tab);
                        columnIndex = 0;
                        rowIndex = 0;
                    }
                    tab = new ListWidget<>()
                        .size(panelWidth - 6, panelHeight - 6)
                        .marginLeft(3)
                        .marginTop(3)
                        .scrollDirection(new VerticalScrollData());

                    String[] parts = milestone.split(",");
                    String[] partsItem = parts[1].split(":");
                    String title = parts[0].substring(1);
                    String modid = partsItem[0];
                    String name = partsItem[1];
                    int meta = partsItem.length > 2 ? Integer.parseInt(partsItem[2]) : 0;

                    Item item = GameRegistry.findItem(modid, name);
                    ItemStack stack = new ItemStack(item, 1, meta);

                    final int finalTabIndex = tabIndex;

                    Widget<?> button = new ButtonWidget<>()
                        .size(tabSize)
                        .padding(tabPadding)
                        .overlay(new ItemDrawable(stack))
                        .addTooltipLine(title)
                        .onMousePressed(mouseButton -> {
                            if (mouseButton == 0 || mouseButton == 1) {
                                for (int i = 0; i < tabPanels.size(); i++) {
                                    if (i == finalTabIndex){
                                        tabPanels.get(i).setEnabled(true);
                                        tabButtons.get(i).background(GuiTextures.BUTTON_CLEAN);
                                        tabButtons.get(i).marginTop(2);
                                    }
                                    else {
                                        tabPanels.get(i).setEnabled(false);
                                        tabButtons.get(i).background(GuiTextures.MC_BUTTON);
                                        tabButtons.get(i).marginTop(0);
                                    }
                                }
                                return true;
                            }
                            return false;
                        });

                    tabButtons.add(button);
                    tabGrid.child(
                        button
                    );

                    tabIndex++;

                    tab.child(
                        new TextWidget<>(title)
    //                        .posRel(Alignment.TopCenter)
    //                        .marginTop(tabTitleOffsetY)
                            .height(rowHeight)
                            .scale(1.5f)
                    );
                } else if (milestone.charAt(0) == '^') {
                    columnIndex = 0;
                    if(rowIndex != 0) {
                        rowIndex++;
                        if (!row.getChildren().isEmpty()) {
                            tab.child(row);
                            row = createRow();
                        }
                    }

                    tab.child(
                        new TextWidget<>(milestone.substring(1))
                            .height(16)
                            .fullWidth()
                            .textAlign(Alignment.BottomLeft)
                            .paddingLeft(25)
                            .paddingBottom(6)
    //                        .pos(columnOffset + sectionTitleOffsetX, rowIndex * rowHeight + rowOffset + sectionTitleOffsetY)
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
                        timeText = EnumChatFormatting.GREEN + getTimeString(guiData.completedMilestones.getLong(milestone));
                    }

                    row.child(
                        new ListWidget<>()
    //                        .pos(columnIndex * columnWidth + columnOffset, rowIndex * rowHeight + rowOffset)
                            .size(milestoneWidth, milestoneHeight)
                            .background(GuiTextures.MC_BUTTON)
                            .child(
                                new ItemDisplayWidget()
                                    .item(stack)
                                    .pos(milestonePadding, milestonePadding)
                                    .addTooltipLine(stack.getDisplayName())
                            )
                            .child(
                                new TextWidget<>(timeText)
                                    .textAlign(Alignment.CenterLeft)
                                    .full()
                                    .scale(0.7f)
                                    .marginLeft(textMarginLeft)
                            )
                    );

                    columnIndex++;
                    if (columnIndex > columnCount - 1) {
                        columnIndex = 0;
                        if (!row.getChildren().isEmpty()) {
                            tab.child(row);
                            row = createRow();
                        }
                        rowIndex++;
                    }
                }
            }
            if (!row.getChildren().isEmpty()) {
                tab.child(row);
            }
            tab.disabled();
            tabPanels.add(tab);
            panel.child(tab);
            tabPanels.get(0).setEnabled(true);
            tabButtons.get(0).background(GuiTextures.BUTTON_CLEAN);
            tabButtons.get(0).marginTop(2);

            panel.child(tabGrid);

            return panel;
        }

        private Grid createRow() {
             return new Grid()
                .size((milestoneWidth + tabPadding * 2) * columnCount - tabPadding * 2, milestoneHeight + tabPadding * 2)
                .minElementMargin(tabPadding);
        }

        private String getTimeString(long timeTicks) {
            String timeString;
            if (timeTicks < TICKS_IN_MINUTES) {
                timeString = (timeTicks / TICKS_IN_SECOND) + "s";
            }
            else if (timeTicks < TICKS_IN_HOURS) {
                timeString = (timeTicks / TICKS_IN_MINUTES) + "m " + ((timeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND) + "s";
            }
            else {
                timeString = (timeTicks / TICKS_IN_HOURS) + "h " + ((timeTicks % TICKS_IN_HOURS) / TICKS_IN_MINUTES) + "m " + ((timeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND) + "s";
            }
            return timeString;
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
            if (guiData.allMilestones == null) {
                buffer.writeInt(0);
            } else {
                buffer.writeInt(guiData.allMilestones.length);
                for (String s : guiData.allMilestones) {
                    try {
                        buffer.writeStringToBuffer(s);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
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
            int length = buffer.readInt();
            String[] milestones = new String[length];
            for (int i = 0; i < length; i++) {
                try {
                    milestones[i] = buffer.readStringFromBuffer(32767);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            data.allMilestones = milestones;
            return data;
        }
    }
