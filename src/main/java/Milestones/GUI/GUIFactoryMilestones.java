package Milestones.GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Milestones.UI.VerticalHiddenScrollData;
import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widget.sizer.Area;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;

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

import Milestones.Milestones;
import Milestones.UI.HorizontalHiddenScrollData;
import cpw.mods.fml.common.registry.GameRegistry;
import org.jetbrains.annotations.Nullable;

public class GUIFactoryMilestones implements UIFactory<GUIDataMilestones> {

    private static final long TICKS_IN_SECOND = 20;
    private static final long TICKS_IN_MINUTES = TICKS_IN_SECOND * 60;
    private static final long TICKS_IN_HOURS = TICKS_IN_MINUTES * 60;

    final int panelWidth = 350;
    final int panelHeight = 250;
    final int columnOffset = 10;
    final int rowOffset = 10;
    final int columnCount = 4;
    final int columnWidth = (panelWidth - columnOffset * 2) / columnCount;
    final int titleHeight = 28;
    final int textMarginLeft = 22;
    final int sectionTitleOffsetX = 0;
    final int sectionTitleOffsetY = 8;
    final int tabTitleOffsetY = 10;
    final int tabOffsetX = 2;
    final int tabOffsetY = -47; //-26
    final int tabSize = 22; // 22
    final int tabGridHeight = 27;
    final int tabIconSize = 16;
    final int tabPadding = 1; //(tabSize - tabIconSize) / 2;
    final int milestoneWidth = columnWidth - 16;
    final int milestoneHeight = 18;
    final int milestonePadding = 2;
    final int milestoneGap = 10;

    private String tabTitle = "";

    @Override
    public String getFactoryName() {
        return "milestonesguifactory";
    }

    @Override
    public ModularPanel createPanel(GUIDataMilestones guiData, PanelSyncManager syncManager, UISettings settings) {
        int columnIndex = 0;
        int tabIndex = 0;

        ModularPanel panel = new ModularPanel("milestonesgui")
            .marginTop(40)
            .size(panelWidth, panelHeight)
            .background((context, x, y, width, height, widgetTheme) -> {
                GuiDraw.drawRect(0, 0, width, height, 0xff403f40);
            })
            .overlay((context, x, y, width, height, widgetTheme) -> {
                int borderColor = 0xff313031;
                float thickness = 5.0f;
                float topThickness = 20.0f;

                //Border
                GuiDraw.drawRect(-thickness, -topThickness, width + thickness * 2, topThickness, borderColor);
                GuiDraw.drawRect(width, -topThickness, thickness, height + topThickness + thickness, borderColor);
                GuiDraw.drawRect(-thickness, height, width + thickness * 2, thickness, borderColor);
                GuiDraw.drawRect(-thickness, -topThickness, thickness, height + topThickness + thickness, borderColor);

                int lightShadowColor = 0xffaaaaaa;
                int lightShadowColorTransparent = 0x00aaaaaa;
                int darkShadowColor = 0xff282828;
                int darkShadowColorTransparent = 0x10282828;
                int blackShadowColor = 0xff101010;
                int blackShadowColorTransparent = 0x10101010;
                float lightShadowThickness = 0.8f;
                float darkShadowThickness = 2.0f;

                //Outer shadow
                GuiDraw.drawVerticalGradientRect(-thickness + 5, -topThickness, width + thickness * 2 - 5 * 2, lightShadowThickness, lightShadowColorTransparent, lightShadowColor);
                GuiDraw.drawHorizontalGradientRect(width + thickness, -topThickness + 1, lightShadowThickness, height + topThickness + thickness, darkShadowColor, darkShadowColorTransparent);
                GuiDraw.drawVerticalGradientRect(-thickness, height + thickness, width + thickness * 2, darkShadowThickness, blackShadowColor, blackShadowColorTransparent);
                GuiDraw.drawHorizontalGradientRect(-thickness - darkShadowThickness, -topThickness + 1, darkShadowThickness, height + topThickness + thickness + darkShadowThickness * 0.3f, darkShadowColorTransparent, darkShadowColor);

                //Inner shadow
                GuiDraw.drawVerticalGradientRect(0, 0, width, darkShadowThickness, blackShadowColor, blackShadowColorTransparent);
                GuiDraw.drawHorizontalGradientRect(width - darkShadowThickness, 0, darkShadowThickness, height, blackShadowColorTransparent, blackShadowColor);
                GuiDraw.drawVerticalGradientRect(0, height - darkShadowThickness, width, darkShadowThickness, blackShadowColorTransparent, blackShadowColor);
                GuiDraw.drawHorizontalGradientRect(0, 0, darkShadowThickness, height, blackShadowColor, blackShadowColorTransparent);
            });
        panel.posRel(0.5f, 0.65f);

        String[] allMilestones = guiData.allMilestones;

        List<Widget<?>> tabPanels = new ArrayList<>();
        List<Widget<?>> tabButtons = new ArrayList<>();
        List<String> tabTitles = new ArrayList<>();

        int gridPadding = 0;
        ListWidget tabGrid = new ListWidget<>()
            .pos(0 + gridPadding, -tabGridHeight - 20)
            .size(panelWidth - gridPadding * 2, tabGridHeight + 1)
            .paddingLeft(6)
            .paddingRight(6)
//            .minColWidth(tabSize)
//            .minElementMargin(tabPadding)
            .scrollDirection(new HorizontalHiddenScrollData(true))
            .background((context, x, y, width, height, widgetTheme) -> {
                float thickness = 5.0f;

                GuiDraw.drawRect(-gridPadding, 0, width + gridPadding * 2, height - 0.8f, 0xff242324);

                GuiDraw.drawRect(-thickness - gridPadding, 0, thickness, height, 0xff313031);
                GuiDraw.drawRect(-thickness - gridPadding, -thickness, width + thickness * 2 + gridPadding * 2, thickness, 0xff313031);
                GuiDraw.drawRect(width + gridPadding, 0, thickness, height, 0xff313031);

                int lightShadowColor = 0xffaaaaaa;
                int lightShadowColorTransparent = 0x00aaaaaa;
                int darkShadowColor = 0xff282828;
                int darkShadowColorTransparent = 0x10282828;
                int blackShadowColor = 0xff101010;
                int blackShadowColorTransparent = 0x10101010;
                float lightShadowThickness = 0.8f;
                float darkShadowThickness = 2.0f;

                //Outer shadow
                GuiDraw.drawVerticalGradientRect(-thickness - gridPadding, -thickness - lightShadowThickness, width + thickness * 2 + lightShadowThickness * 0.5f + gridPadding * 2 , lightShadowThickness, lightShadowColorTransparent, lightShadowColor);
                GuiDraw.drawHorizontalGradientRect(width + thickness + gridPadding, -thickness - lightShadowThickness * 0.5f, lightShadowThickness, height + thickness + lightShadowThickness * 0.5f, darkShadowColor, darkShadowColorTransparent);
//                GuiDraw.drawVerticalGradientRect(-thickness, height + thickness, width + thickness * 2, darkShadowThickness, blackShadowColor, blackShadowColorTransparent);
                GuiDraw.drawHorizontalGradientRect(-thickness - darkShadowThickness - gridPadding, -thickness - lightShadowThickness, darkShadowThickness, height + thickness + lightShadowThickness, darkShadowColorTransparent, darkShadowColor);

                //Inner shadow
                GuiDraw.drawVerticalGradientRect(-gridPadding, 0, width + gridPadding * 2, darkShadowThickness, blackShadowColor, blackShadowColorTransparent);
                GuiDraw.drawHorizontalGradientRect(width - darkShadowThickness + gridPadding, 0, darkShadowThickness, height, blackShadowColorTransparent, blackShadowColor);
//                GuiDraw.drawVerticalGradientRect(0, height - darkShadowThickness, width, darkShadowThickness, blackShadowColorTransparent, blackShadowColor);
                GuiDraw.drawHorizontalGradientRect(-gridPadding, 0, darkShadowThickness, height, blackShadowColor, blackShadowColorTransparent);
            });
//            .paddingLeft(6);
//            .paddingRight(6);

        int closeButtonSize = 12;
        Widget<?> cross = new Widget<>()
            .background(GuiTextures.CROSS);
        cross.size(8);
        cross.posRel(0.5f, 0.5f);
        panel.child(new ButtonWidget<>()
            .pos(panelWidth - closeButtonSize - 2, -20 + ((20 - closeButtonSize) / 2))
            .size(closeButtonSize)
            .background((context, x, y, width, height, widgetTheme) -> {
                GuiDraw.drawRect(0, 0, width, height, 0xff313031);
            })
            .overlay((context, x, y, width, height, widgetTheme) -> {
//                int borderColor = 0xff313031;
                float thickness = 1.2f;

//                //Border
//                GuiDraw.drawRect(-thickness, -thickness, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(width, -thickness, thickness, height + thickness * 2, borderColor);
//                GuiDraw.drawRect(-thickness, height, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(-thickness, -thickness, thickness, height + thickness * 2, borderColor);

                int topShadowColor = 0xff101010;
                int topShadowColorTransparent = 0x10101010;
                int rightShadowColor = 0xff201a1a;
                int rightShadowColorTransparent = 0x10201a1a;
                int bottomShadowColor = 0xff565352;
                int bottomShadowColorTransparent = 0x00565352;
                int leftShadowColor = 0xff201a1a;
                int leftShadowColorTransparent = 0x10201a1a;

                int topShadowColorInner = 0xff565352;
                int topShadowColorTransparentInner = 0x00565352;
                int rightShadowColorInner = 0xff201a1a;
                int rightShadowColorTransparentInner = 0x10201a1a;
                int bottomShadowColorInner = 0xff101010;
                int bottomShadowColorTransparentInner = 0x10101010;
                int leftShadowColorInner = 0xff201a1a;
                int leftShadowColorTransparentInner = 0x10201a1a;

                //Outer shadow
                GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, -thickness, width + thickness * 0.4f, thickness, topShadowColorTransparent, topShadowColor);
                GuiDraw.drawHorizontalGradientRect(width, -thickness * 0.2f, thickness * 0.5f, height + thickness * 0.4f, rightShadowColor, rightShadowColorTransparent);
                GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, height, width + thickness * 0.4f, thickness, bottomShadowColor, bottomShadowColorTransparent);
                GuiDraw.drawHorizontalGradientRect(-thickness * 0.5f, -thickness * 0.2f, thickness * 0.5f, height + thickness * 0.4f, leftShadowColorTransparent, leftShadowColor);

                //Inner shadow
                GuiDraw.drawVerticalGradientRect(0, 0, width, thickness, topShadowColorInner, topShadowColorTransparentInner);
                GuiDraw.drawHorizontalGradientRect(width - thickness * 0.5f, 0, thickness * 0.5f, height, rightShadowColorTransparentInner, rightShadowColorInner);
                GuiDraw.drawVerticalGradientRect(0, height - thickness, width, thickness, bottomShadowColorTransparentInner, bottomShadowColorInner);
                GuiDraw.drawHorizontalGradientRect(0, 0, thickness * 0.5f, height, leftShadowColorInner, leftShadowColorTransparentInner);
            })
            .child(
                cross
            )
            .onMousePressed(mouseButton -> {
                if (mouseButton == 0 || mouseButton == 1) {
                    panel.closeIfOpen();
                    return true;
                }
                return false;
            }));

        panel.child(new TextWidget<>
            (
                new IKey() {
                    @Override
                    public String get() {
                        return tabTitle;
                    }

                    @Override
                    public IKey style(@Nullable EnumChatFormatting formatting) {
                        return null;
                    }

                    @Override
                    public IKey removeStyle() {
                        return null;
                    }
                }
            )
            .color(0xfffee5bf)
            .pos(2, -14)
            .scale(1.0f));

        ListWidget tab = null;
        Grid row = createRow();

        for (var milestone : allMilestones) {
            if (milestone.charAt(0) == '$') {
                if (tab != null) {
                    if (!row.getChildren()
                        .isEmpty()) {
                        tab.child(row);
                        row = createRow();
                    }
                    tab.disabled();
                    tabPanels.add(tab);
                    panel.child(tab);
                    columnIndex = 0;
                }
                tab = new ListWidget<>().size(panelWidth, panelHeight - 2)
//                    .marginLeft(2)
                    .marginTop(1)
                    .scrollDirection(new VerticalScrollData(false, 6));
//                tab.paddingTop(10);

                String[] parts = milestone.split(",");
                String[] partsItem = parts[1].split(":");
                String title = parts[0].substring(1);
                String modid = partsItem[0];
                String name = partsItem[1];
                int meta = partsItem.length > 2 ? Integer.parseInt(partsItem[2]) : 0;

                Item item = GameRegistry.findItem(modid, name);
                ItemStack stack = new ItemStack(item, 1, meta);

                final int finalTabIndex = tabIndex;

                Widget<?> button = new ButtonWidget<>().size(tabSize)
//                    .padding(tabPadding)
                    .marginRight(tabPadding)
                    .addTooltipLine(title)
                    .background((context, x, y, width, height, widgetTheme) -> {
                        GuiDraw.drawRect(0, 0, width, height, 0xff6e6e6e);

//                int borderColor = 0xff313031;
                        float thickness = 1.2f;

//                //Border
//                GuiDraw.drawRect(-thickness, -thickness, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(width, -thickness, thickness, height + thickness * 2, borderColor);
//                GuiDraw.drawRect(-thickness, height, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(-thickness, -thickness, thickness, height + thickness * 2, borderColor);

                        int topShadowColor = 0xff101010;
                        int topShadowColorTransparent = 0x10101010;
                        int rightShadowColor = 0xff201a1a;
                        int rightShadowColorTransparent = 0x10201a1a;
                        int bottomShadowColor = 0xff101010;
                        int bottomShadowColorTransparent = 0x10101010;
                        int leftShadowColor = 0xff201a1a;
                        int leftShadowColorTransparent = 0x10201a1a;

                        int topShadowColorInner = 0xffeeeeee;
                        int topShadowColorTransparentInner = 0x00eeeeee;
                        int rightShadowColorInner = 0xff201a1a;
                        int rightShadowColorTransparentInner = 0x10201a1a;
                        int bottomShadowColorInner = 0xff101010;
                        int bottomShadowColorTransparentInner = 0x10101010;
                        int leftShadowColorInner = 0xff201a1a;
                        int leftShadowColorTransparentInner = 0x10201a1a;

                        //Outer shadow
                        GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, -thickness, width + thickness * 0.4f, thickness, topShadowColorTransparent, topShadowColor);
                        GuiDraw.drawHorizontalGradientRect(width, -thickness * 0.2f, thickness, height + thickness * 0.4f, rightShadowColor, rightShadowColorTransparent);
                        GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, height, width + thickness * 0.4f, thickness * 0.5f, bottomShadowColor, bottomShadowColorTransparent);
                        GuiDraw.drawHorizontalGradientRect(-thickness, -thickness * 0.2f, thickness, height + thickness * 0.4f, leftShadowColorTransparent, leftShadowColor);

                        //Inner shadow
                        GuiDraw.drawVerticalGradientRect(0, 0, width, thickness, topShadowColorInner, topShadowColorTransparentInner);
                        GuiDraw.drawHorizontalGradientRect(width - thickness, 0, thickness, height, rightShadowColorTransparentInner, rightShadowColorInner);
                        GuiDraw.drawVerticalGradientRect(0, height - thickness, width, thickness, bottomShadowColorTransparentInner, bottomShadowColorInner);
                        GuiDraw.drawHorizontalGradientRect(0, 0, thickness, height, leftShadowColorInner, leftShadowColorTransparentInner);
                    })
                    .overlay((context, x, y, width, height, widgetTheme) -> {
                        GuiDraw.drawItem(stack, 3, 3, width - 6, height - 6, 0);
                    })
                    .onMousePressed(mouseButton -> {
                        if (mouseButton == 0 || mouseButton == 1) {
                            for (int i = 0; i < tabPanels.size(); i++) {
                                if (i == finalTabIndex) {
                                    tabPanels.get(i)
                                        .setEnabled(true);
                                    tabButtons.get(i)
                                        .background((context, x, y, width, height, widgetTheme) -> {
                                            GuiDraw.drawRect(0, 0, width, height + 2, 0xff313031);

                                            //                int borderColor = 0xff313031;
                                            float thickness = 1.2f;

//                //Border
//                GuiDraw.drawRect(-thickness, -thickness, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(width, -thickness, thickness, height + thickness * 2, borderColor);
//                GuiDraw.drawRect(-thickness, height, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(-thickness, -thickness, thickness, height + thickness * 2, borderColor);

                                            int topShadowColor = 0xff101010;
                                            int topShadowColorTransparent = 0x10101010;
                                            int rightShadowColor = 0xff201a1a;
                                            int rightShadowColorTransparent = 0x10201a1a;
                                            int bottomShadowColor = 0xff565352;
                                            int bottomShadowColorTransparent = 0x00565352;
                                            int leftShadowColor = 0xff201a1a;
                                            int leftShadowColorTransparent = 0x10201a1a;

                                            int topShadowColorInner = 0xff565352;
                                            int topShadowColorTransparentInner = 0x00565352;
                                            int rightShadowColorInner = 0xff201a1a;
                                            int rightShadowColorTransparentInner = 0x10201a1a;
                                            int bottomShadowColorInner = 0xff101010;
                                            int bottomShadowColorTransparentInner = 0x10101010;
                                            int leftShadowColorInner = 0xff201a1a;
                                            int leftShadowColorTransparentInner = 0x10201a1a;

                                            //Outer shadow
                                            GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, -thickness, width + thickness * 0.4f, thickness, topShadowColorTransparent, topShadowColor);
                                            GuiDraw.drawHorizontalGradientRect(width, -thickness * 0.2f, thickness, height + thickness * 0.4f, rightShadowColor, rightShadowColorTransparent);
//                                            GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, height, width + thickness * 0.4f, thickness, bottomShadowColor, bottomShadowColorTransparent);
                                            GuiDraw.drawHorizontalGradientRect(-thickness, -thickness * 0.2f, thickness, height + thickness * 0.4f, leftShadowColorTransparent, leftShadowColor);

                                            //Inner shadow
                                            GuiDraw.drawVerticalGradientRect(0, 0, width, thickness, topShadowColorInner, topShadowColorTransparentInner);
                                            GuiDraw.drawHorizontalGradientRect(width - thickness, 0, thickness, height + 2, rightShadowColorTransparentInner, rightShadowColorInner);
//                                            GuiDraw.drawVerticalGradientRect(0, height - thickness, width, thickness, bottomShadowColorTransparentInner, bottomShadowColorInner);
                                            GuiDraw.drawHorizontalGradientRect(0, 0, thickness, height + 2, leftShadowColorInner, leftShadowColorTransparentInner);
                                        });
//                                    tabButtons.get(i).marginTop(2);
                                    tabTitle = tabTitles.get(i);
                                } else {
                                    tabPanels.get(i)
                                        .setEnabled(false);
                                    tabButtons.get(i)
                                        .background((context, x, y, width, height, widgetTheme) -> {
                                            GuiDraw.drawRect(0, 0, width, height, 0xff6e6e6e);

//                int borderColor = 0xff313031;
                                            float thickness = 1.2f;

//                //Border
//                GuiDraw.drawRect(-thickness, -thickness, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(width, -thickness, thickness, height + thickness * 2, borderColor);
//                GuiDraw.drawRect(-thickness, height, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(-thickness, -thickness, thickness, height + thickness * 2, borderColor);

                                            int topShadowColor = 0xff101010;
                                            int topShadowColorTransparent = 0x10101010;
                                            int rightShadowColor = 0xff201a1a;
                                            int rightShadowColorTransparent = 0x10201a1a;
                                            int bottomShadowColor = 0xff101010;
                                            int bottomShadowColorTransparent = 0x10101010;
                                            int leftShadowColor = 0xff201a1a;
                                            int leftShadowColorTransparent = 0x10201a1a;

                                            int topShadowColorInner = 0xffeeeeee;
                                            int topShadowColorTransparentInner = 0x00eeeeee;
                                            int rightShadowColorInner = 0xff201a1a;
                                            int rightShadowColorTransparentInner = 0x10201a1a;
                                            int bottomShadowColorInner = 0xff101010;
                                            int bottomShadowColorTransparentInner = 0x10101010;
                                            int leftShadowColorInner = 0xff201a1a;
                                            int leftShadowColorTransparentInner = 0x10201a1a;

                                            //Outer shadow
                                            GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, -thickness, width + thickness * 0.4f, thickness, topShadowColorTransparent, topShadowColor);
                                            GuiDraw.drawHorizontalGradientRect(width, -thickness * 0.2f, thickness, height + thickness * 0.4f, rightShadowColor, rightShadowColorTransparent);
                                            GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, height, width + thickness * 0.4f, thickness * 0.5f, bottomShadowColor, bottomShadowColorTransparent);
                                            GuiDraw.drawHorizontalGradientRect(-thickness, -thickness * 0.2f, thickness, height + thickness * 0.4f, leftShadowColorTransparent, leftShadowColor);

                                            //Inner shadow
                                            GuiDraw.drawVerticalGradientRect(0, 0, width, thickness, topShadowColorInner, topShadowColorTransparentInner);
                                            GuiDraw.drawHorizontalGradientRect(width - thickness, 0, thickness, height, rightShadowColorTransparentInner, rightShadowColorInner);
                                            GuiDraw.drawVerticalGradientRect(0, height - thickness, width, thickness, bottomShadowColorTransparentInner, bottomShadowColorInner);
                                            GuiDraw.drawHorizontalGradientRect(0, 0, thickness, height, leftShadowColorInner, leftShadowColorTransparentInner);
                                        });
//                                    tabButtons.get(i).marginTop(0);
                                }
                            }
                            return true;
                        }
                        return false;
                    });

                tabButtons.add(button);
                tabGrid.child(button);

                tabIndex++;
                tabTitles.add(title);
            } else if (milestone.charAt(0) == '^') {
                columnIndex = 0;
                if (!row.getChildren()
                    .isEmpty()) {
                    tab.child(row);
                    row = createRow();
                }

                tab.child(
                    new TextWidget<>(milestone.substring(1))
                        .style(EnumChatFormatting.WHITE)
                        .height(20)
                        .fullWidth()
                        .textAlign(Alignment.BottomLeft)
                        .paddingLeft(25)
                        .paddingBottom(1)
                // .pos(columnOffset + sectionTitleOffsetX, rowIndex * rowHeight + rowOffset + sectionTitleOffsetY)
                );
            } else {
                String[] parts = milestone.split(":");
                String modid = parts[0];
                String name = parts[1];
                int meta = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

                Item item = GameRegistry.findItem(modid, name);
                ItemStack stack = new ItemStack(item, 1, meta);

                String timeText = EnumChatFormatting.GRAY + "Incomplete";
                if (guiData.completedMilestones != null && guiData.completedMilestones.hasKey(milestone)) {
                    timeText = EnumChatFormatting.GREEN + getTimeString(guiData.completedMilestones.getLong(milestone));
                }

                row.child(
                    new ListWidget<>()
                        // .pos(columnIndex * columnWidth + columnOffset, rowIndex * rowHeight + rowOffset)
                        .size(milestoneWidth, milestoneHeight)
//                        .background(GuiTextures.MC_BUTTON)
                        .child(
                            new ItemDisplayWidget().item(stack)
                                .pos(milestonePadding, milestonePadding)
                                .addTooltipLine(stack.getDisplayName())
                                .background(IDrawable.EMPTY)
                                .hoverBackground(IDrawable.EMPTY))
                        .child(
                            new TextWidget<>(timeText)
                                .textAlign(Alignment.CenterLeft)
                                .full()
                                .scale(0.6f)
                                .marginTop(1)
                                .marginLeft(textMarginLeft)));

                columnIndex++;
                if (columnIndex > columnCount - 1) {
                    columnIndex = 0;
                    if (!row.getChildren()
                        .isEmpty()) {
                        tab.child(row);
                        row = createRow();
                    }
                }
            }
        }
        if (!row.getChildren()
            .isEmpty()) {
            tab.child(row);
        }
        tab.disabled();
        tabPanels.add(tab);
        panel.child(tab);
        tabPanels.get(0)
            .setEnabled(true);
        tabButtons.get(0)
            .background((context, x, y, width, height, widgetTheme) -> {
                GuiDraw.drawRect(0, 0, width, height + 2, 0xff313031);

                //                int borderColor = 0xff313031;
                float thickness = 1.2f;

//                //Border
//                GuiDraw.drawRect(-thickness, -thickness, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(width, -thickness, thickness, height + thickness * 2, borderColor);
//                GuiDraw.drawRect(-thickness, height, width + thickness * 2, thickness, borderColor);
//                GuiDraw.drawRect(-thickness, -thickness, thickness, height + thickness * 2, borderColor);

                int topShadowColor = 0xff101010;
                int topShadowColorTransparent = 0x10101010;
                int rightShadowColor = 0xff201a1a;
                int rightShadowColorTransparent = 0x10201a1a;
                int bottomShadowColor = 0xff565352;
                int bottomShadowColorTransparent = 0x00565352;
                int leftShadowColor = 0xff201a1a;
                int leftShadowColorTransparent = 0x10201a1a;

                int topShadowColorInner = 0xff565352;
                int topShadowColorTransparentInner = 0x00565352;
                int rightShadowColorInner = 0xff201a1a;
                int rightShadowColorTransparentInner = 0x10201a1a;
                int bottomShadowColorInner = 0xff101010;
                int bottomShadowColorTransparentInner = 0x10101010;
                int leftShadowColorInner = 0xff201a1a;
                int leftShadowColorTransparentInner = 0x10201a1a;

                //Outer shadow
                GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, -thickness, width + thickness * 0.4f, thickness, topShadowColorTransparent, topShadowColor);
                GuiDraw.drawHorizontalGradientRect(width, -thickness * 0.2f, thickness, height + thickness * 0.4f, rightShadowColor, rightShadowColorTransparent);
//                                            GuiDraw.drawVerticalGradientRect(-thickness * 0.2f, height, width + thickness * 0.4f, thickness, bottomShadowColor, bottomShadowColorTransparent);
                GuiDraw.drawHorizontalGradientRect(-thickness, -thickness * 0.2f, thickness, height + thickness * 0.4f, leftShadowColorTransparent, leftShadowColor);

                //Inner shadow
                GuiDraw.drawVerticalGradientRect(0, 0, width, thickness, topShadowColorInner, topShadowColorTransparentInner);
                GuiDraw.drawHorizontalGradientRect(width - thickness, 0, thickness, height + 2, rightShadowColorTransparentInner, rightShadowColorInner);
//                                            GuiDraw.drawVerticalGradientRect(0, height - thickness, width, thickness, bottomShadowColorTransparentInner, bottomShadowColorInner);
                GuiDraw.drawHorizontalGradientRect(0, 0, thickness, height + 2, leftShadowColorInner, leftShadowColorTransparentInner);
            });
//        tabButtons.get(0)
//            .marginTop(2);
        tabTitle = tabTitles.get(0);

        panel.child(tabGrid);

        return panel;
    }

    private Grid createRow() {
        return new Grid()
            .size((milestoneWidth + milestoneGap) * columnCount - milestoneGap, milestoneHeight)
            .minElementMargin(milestoneGap / 2);
    }

    private String getTimeString(long timeTicks) {
        String timeString;
        if (timeTicks < TICKS_IN_MINUTES) {
            timeString = (timeTicks / TICKS_IN_SECOND) + "s";
        } else if (timeTicks < TICKS_IN_HOURS) {
            timeString = (timeTicks / TICKS_IN_MINUTES) + "m "
                + ((timeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND)
                + "s";
        } else {
            timeString = (timeTicks / TICKS_IN_HOURS) + "h "
                + ((timeTicks % TICKS_IN_HOURS) / TICKS_IN_MINUTES)
                + "m "
                + ((timeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND)
                + "s";
        }
        return timeString;
    }

    @Override
    public ModularScreen createScreen(GUIDataMilestones guiData, ModularPanel mainPanel) {
        return new ModularScreen(Milestones.MODID, mainPanel);
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
