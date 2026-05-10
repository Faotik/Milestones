package Milestones.UI;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widget.scroll.HorizontalScrollData;
import com.cleanroommc.modularui.widget.scroll.ScrollArea;

public class HorizontalHiddenScrollData extends HorizontalScrollData {

    public HorizontalHiddenScrollData(boolean topAlignment) {
        super(topAlignment);
    }

    @Override
    public void drawScrollbar(ScrollArea area, ModularGuiContext context, WidgetTheme widgetTheme, IDrawable texture) {}

//    @Override
//    public void drawScrollShadow(ScrollArea area, ModularGuiContext context) {}
}
