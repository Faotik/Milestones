package Milestones.UI;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widget.scroll.ScrollArea;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;

public class VerticalHiddenScrollData extends VerticalScrollData {
    public VerticalHiddenScrollData(boolean leftAlignment, int thickness) {
        super(leftAlignment, thickness);
    }

    @Override
    public void drawScrollbar(ScrollArea area, ModularGuiContext context, WidgetTheme widgetTheme, IDrawable texture) {}

    @Override
    public void drawScrollShadow(ScrollArea area, ModularGuiContext context) {}
}
