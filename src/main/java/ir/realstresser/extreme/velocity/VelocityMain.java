package ir.realstresser.extreme.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import ir.realstresser.extreme.shared.check.CheckManager;
import ir.realstresser.extreme.shared.enums.ProxyType;
import ir.realstresser.extreme.shared.util.ConfigData;
import ir.realstresser.extreme.shared.util.FileManager;
import ir.realstresser.extreme.shared.util.SQLManager;
import ir.realstresser.extreme.shared.util.TaskManager;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "extreme_ab",
        name = "ExtremeAntiBot",
        version = "0",
        description = "a lightweight antibot",
        authors = "realstresser",
        dependencies = {
        @Dependency(id = "limboapi"), @Dependency(id = "packetevents")
        })
@SuppressWarnings("unused") public class VelocityMain {
    @Getter public static VelocityMain instance;
    @Getter private final ProxyServer server;
    @Getter private final Logger  logger;
    @Getter private final FileManager fileManager;
    @Getter private final long startTime;
    @Getter private final TaskManager taskManager;
    @Getter @Setter private ConfigData configData;
    @Getter private CheckManager checkManager;
    @Getter private SQLManager sqlManager;

    @Inject public VelocityMain(final ProxyServer server, final Logger logger, final PluginContainer plugin, @DataDirectory Path dataDirectory){
        this.startTime = System.currentTimeMillis();
        this.taskManager = new TaskManager();
        this.server = server;
        this.logger = logger;
        instance = this;
        this.fileManager = new FileManager(getServerPluginsDirectory());
        this.sqlManager = new SQLManager();
        this.checkManager = new CheckManager();
    }
    @Subscribe public void onProxyInit(final ProxyInitializeEvent e){
        getLogger().info("Starting up ExtremeAntiBot!");
        taskManager.addTask(() -> {
            getLogger().info("ASYNC task is starting...");

            fileManager.init();

            sqlManager.init();

            checkManager.addAllChecks(ProxyType.VELOCITY);

            getLogger().info("Took " + (System.currentTimeMillis() - startTime) + "ms to load.");
        });
        // TODO: add handler for every SECONDS seconds database clear (so, players can rejoin again, after a while.)
        taskManager.repeatingTask(() -> {
            try {
                update();
            }catch(Exception ignored) {}
        }, 100, TimeUnit.MILLISECONDS);
    }
    private void update(){
        getServer().getAllPlayers()
                .forEach(p -> {
                    switch(getConfigData().getBlacklistby().toLowerCase()){
                        case "ip" ->{
                            if (((int) sqlManager.getPlayerDataByIP(p.getRemoteAddress().getAddress().toString(), "violationCount") >= configData.getMaxviolations())) {
                                sqlManager.updatePlayerByIP(p.getRemoteAddress().getAddress().toString(), "lastBlacklist", System.currentTimeMillis());
                                p.disconnect(LegacyComponentSerializer.legacyAmpersand().deserialize(String.format("%s %s", configData.getPrefix(), configData.getKickmessage())));
                            }
                        }
                        case "uuid"->{
                            if (((int) sqlManager.getPlayerDataByUUID(p.getUniqueId().toString(), "violationCount") >= configData.getMaxviolations())) {
                                p.disconnect(LegacyComponentSerializer.legacyAmpersand().deserialize(String.format("%s %s", configData.getPrefix(), configData.getKickmessage())));
                                sqlManager.updatePlayerByUUID(p.getUniqueId().toString(), "lastBlacklist", System.currentTimeMillis());
                            }
                        }
                    }
                });
        clearViolation();
    }

    private void clearViolation() {
        long reconnectionSeconds = getConfigData().getReconnectionseconds();
        if (reconnectionSeconds == -1) return;

        for (Map<String, Object> playerData : sqlManager.getAllPlayers()) {
            String ip = (String) playerData.get("ip");
            Long lastDisconnect = (Long) playerData.get("lastBlacklist");

            if (lastDisconnect == null || lastDisconnect == -1) continue;

            if (System.currentTimeMillis() - lastDisconnect >= reconnectionSeconds * 1000) {
                sqlManager.updatePlayerByIP(ip, "violationCount", 0);
                System.out.println("cleared someone's violation");
            }
        }
    }


    public Path getServerPluginsDirectory() {
        return Paths.get("plugins");
    }
}
