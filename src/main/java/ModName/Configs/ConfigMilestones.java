package ModName.Configs;

import static ModName.ModName.MODID;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = MODID, category = "milestones", configSubDirectory = "Milestones", filename = "milestones")
//@Config.LangKey("minecraftimprovements.config.hud.name")
public class ConfigMilestones {

    public static final Milestones milestones = new Milestones();
    public static final UI ui = new UI();

//    @Config.LangKey("minecraftimprovements.config.hud.hud_general.name")
    public static class Milestones {
        @Config.DefaultStringList({})
        @Config.Order(1)
        public String[] items;
    }

    public static class UI {
        @Config.DefaultBoolean(false)
        @Config.Order(1)
        public boolean replaceAchievementButton;
    }
}
