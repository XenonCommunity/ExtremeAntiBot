package ir.realstresser.extreme.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import ir.realstresser.extreme.shared.check.CheckBase;
import ir.realstresser.extreme.shared.util.*;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(id = "extreme_ab", name = "ExtremeAntiBot", version = "0", description = "a lightweight antibot", authors = "realstresser")
@SuppressWarnings("unused") public class Main {
    @Getter private static Main instance;
    @Getter private final ProxyServer server;
    @Getter private final Logger  logger;
    @Getter private final FileManager fileManager;
    @Getter private final long startTime;
    @Getter private final TaskManager taskManager;
    @Setter @Getter private ConfigData configData;
    @Getter private boolean isProxyLoaded = false;
    @Inject public Main(final ProxyServer server, final Logger logger){
        startTime = System.currentTimeMillis();
        instance = this;
        this.taskManager = new TaskManager();
        this.server = server;
        this.logger = logger;
        this.fileManager = new FileManager(getServerPluginsDirectory());
    }
    @Subscribe public void onProxyInit(final ProxyInitializeEvent e){
        getLogger().info("Starting up ExtremeAntiBot!");
        taskManager.addTask(() -> {
            getLogger().info("ASYNC task is starting...");

            fileManager.init();

            SQLManager.init();

            Main.getInstance().getLogger().info("Initializing checks...");

            new Reflections("ir.realstresser.extreme.velocity.check").getSubTypesOf(CheckBase.class).forEach(c -> {
                try {
                    getServer().getEventManager().register(this, c.newInstance());
                } catch (Exception ex) {
                    getLogger().error(ex.getMessage());
                }
            });

            Main.getInstance().getLogger().info("Successfully initialized!");

            getLogger().info("Took " + (System.currentTimeMillis() - startTime) + "ms to load.");
            isProxyLoaded = true;
        });
        taskManager.repeatingTask(() -> {
            try {
                getServer().getAllPlayers()
                        .forEach(p -> {
                            if (((int) SQLManager.getPlayerData(p.getUniqueId().toString(), "violationCount")) > /*TODO: MAKE THIS BASED ON CFG*/15) {
                                p.disconnect(Component.text(/*TODO: ADD CONFIG MESSAGE*/""));
                            }
                        });
            }catch(Exception ignored) {}
        }, 100);
    }
    public Path getServerPluginsDirectory() {
        return Paths.get("plugins");
    }
}
