package ir.realstresser.extreme.shared.util;

import ir.realstresser.extreme.velocity.Main;
import lombok.Cleanup;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class FileManager {
    private final File ExtremeFolder;
    private final File SQL_DB;
    private final File CONFIG_FILE;

    public FileManager(final Path pluginDir) {
        ExtremeFolder = new File(pluginDir.toString(), "ExtremeAntiBot");
        SQL_DB = new File(ExtremeFolder, "database.db");
        CONFIG_FILE = new File(ExtremeFolder, "config.yml");
    }

    public void init() {
        Main.getInstance().getLogger().info("Initializing FileManager...");
        try {
            if (!ExtremeFolder.exists()) ExtremeFolder.mkdirs();
            if (!SQL_DB.exists()) Files.createFile(SQL_DB.toPath());
            if (!CONFIG_FILE.exists()) copyConfig();
            Main.getInstance().setConfigData(getConfig());
        } catch (final Exception e) {
            Main.getInstance().getLogger().error(e.getMessage());
        }
        Main.getInstance().getLogger().info("Successfully initialized!");
    }

    private void copyConfig() {
        try {
            @Cleanup final InputStream in = getClass().getClassLoader().getResourceAsStream("config.yml");
            @Cleanup final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            @Cleanup final BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (final Exception e) {
            Main.getInstance().getLogger().error(e.getMessage());
        }
    }

    public ConfigData getConfig() {
        try {
            @Cleanup
            final FileInputStream is = new FileInputStream(CONFIG_FILE);
            return new Yaml(new Constructor(ConfigData.class)).loadAs(is, ConfigData.class);
        } catch (final Exception e) {
            Main.getInstance().getLogger().error(e.getMessage());
            return null;
        }
    }
}
