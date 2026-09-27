package Milestones.Configs;

import com.gtnewhorizon.gtnhlib.config.Config;

import Milestones.Milestones;

@Config(modid = Milestones.MODID, category = "client", configSubDirectory = "Milestones", filename = "client")
public class ConfigClient {

    @Config.DefaultBoolean(false)
    @Config.Order(1)
    public static boolean replaceAchievementButton;
}
