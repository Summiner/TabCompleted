package rs.jamie.tabcompleted.config;

import space.arim.dazzleconf.ConfigurationFactory;
import space.arim.dazzleconf.ConfigurationOptions;
import space.arim.dazzleconf.error.ConfigFormatSyntaxException;
import space.arim.dazzleconf.error.InvalidConfigException;
import space.arim.dazzleconf.ext.snakeyaml.CommentMode;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlConfigurationFactory;
import space.arim.dazzleconf.ext.snakeyaml.SnakeYamlOptions;
import space.arim.dazzleconf.helper.ConfigurationHelper;
import space.arim.dazzleconf.serialiser.ValueSerialiser;
import space.arim.dazzleconf.sorter.AnnotationBasedSorter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ConfigManager {

    private final static Logger logger = Logger.getLogger("TabCompleted Config Manager");


    private volatile MainConfig config;
    private final ConfigurationHelper<MainConfig> configHelper;

    public ConfigManager(File pluginFolder) {
        configHelper = createHelper(MainConfig.class, new File(pluginFolder, "config.yml"), new ComponentSerializer());
        reload();
    }

    private static <T> ConfigurationHelper<T> createHelper(Class<T> configClass, File file, ValueSerialiser<?>... serializers) {
        SnakeYamlOptions yamlOptions = new SnakeYamlOptions.Builder().commentMode(CommentMode.fullComments()).build();
        ConfigurationOptions.Builder optionBuilder = new ConfigurationOptions.Builder();
        optionBuilder.sorter(new AnnotationBasedSorter());
        if (serializers != null && serializers.length > 0) optionBuilder.addSerialisers(serializers);
        ConfigurationFactory<T> configFactory = SnakeYamlConfigurationFactory.create(configClass, optionBuilder.build(), yamlOptions);
        return new ConfigurationHelper<>(file.getParentFile().toPath(), file.getName(), configFactory);
    }
    public void reload() {
        try {
            config = configHelper.reloadConfigData();
        } catch (IOException e) {
            logger.severe("Couldn't open config file!");
            e.printStackTrace();
        } catch (ConfigFormatSyntaxException e) {
            logger.severe("Invalid config syntax!");
            e.printStackTrace();
        } catch (InvalidConfigException e) {
            logger.severe("Invalid config value!");
            e.printStackTrace();
        }
    }

    public MainConfig getConfig() {
        return config;
    }

}
