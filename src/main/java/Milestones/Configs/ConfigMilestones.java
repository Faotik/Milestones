package Milestones.Configs;

import static Milestones.Milestones.MODID;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = MODID, category = "milestones", configSubDirectory = "Milestones", filename = "milestones")
public class ConfigMilestones {

    @Config.DefaultStringList({})
    @Config.Order(1)
    public static String[] items;
}
