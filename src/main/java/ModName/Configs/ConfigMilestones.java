package ModName.Configs;

import static ModName.ModName.MODID;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = MODID, category = "milestones", configSubDirectory = "Milestones", filename = "milestones")
public class ConfigMilestones {
    @Config.DefaultStringList({})
    @Config.Order(1)
    public static String[] items;
}
