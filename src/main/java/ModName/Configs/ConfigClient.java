package ModName.Configs;

import static ModName.ModName.MODID;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = MODID, category = "client", configSubDirectory = "Milestones", filename = "client")
public class ConfigClient {
    @Config.DefaultBoolean(false)
    @Config.Order(1)
    public static boolean replaceAchievementButton;
}
