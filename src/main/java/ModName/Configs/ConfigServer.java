package ModName.Configs;

import static ModName.ModName.MODID;

import com.gtnewhorizon.gtnhlib.config.Config;

@Config(modid = MODID, category = "server", configSubDirectory = "Milestones", filename = "server")
public class ConfigServer {
    @Config.DefaultBoolean(true)
    @Config.Order(1)
    public static boolean GTIntegration;

    @Config.DefaultBoolean(true)
    @Config.Order(2)
    public static boolean AE2Integration;

    @Config.DefaultBoolean(true)
    @Config.Order(3)
    public static boolean SUIntegration;

    @Config.DefaultBoolean(true)
    @Config.Order(4)
    public static boolean enableTrophies;

    @Config.DefaultBoolean(true)
    @Config.Order(5)
    public static boolean enableFireworks;
}
