package Milestones.Configs;

import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.typesafe.config.ConfigException;

public class ConfigRegister {

    public static void init() {
        register(ConfigMilestones.class);
        register(ConfigClient.class);
        register(ConfigServer.class);
    }

    private static void register(Class<?> configClass) {
        try {
            ConfigurationManager.registerConfig(configClass);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }
}
