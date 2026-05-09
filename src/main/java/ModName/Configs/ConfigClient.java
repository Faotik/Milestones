package ModName.Configs;

import ModName.ModName;
import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = ModName.MODID, category = "client", configSubDirectory = "Milestones", filename = "client")
public class ConfigClient {

    @Config.DefaultBoolean(false)
    @Config.Order(1)
    public static boolean replaceAchievementButton;
}
