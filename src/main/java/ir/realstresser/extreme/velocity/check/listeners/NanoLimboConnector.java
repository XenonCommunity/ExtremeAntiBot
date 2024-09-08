package ir.realstresser.extreme.velocity.check.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.server.ServerInfo;
import ir.realstresser.extreme.shared.check.CheckBase;
import ir.realstresser.extreme.velocity.VelocityMain;
import ua.nanit.limbo.configuration.LimboConfig;
import ua.nanit.limbo.server.ConsoleCommandHandler;
import ua.nanit.limbo.server.LimboServer;
import ua.nanit.limbo.server.data.BossBar;
import ua.nanit.limbo.server.data.InfoForwarding;
import ua.nanit.limbo.server.data.PingData;
import ua.nanit.limbo.server.data.Title;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@SuppressWarnings("unused") public class NanoLimboConnector extends CheckBase {
    private LimboServer server;

    public NanoLimboConnector(){
        super("NanoLimboConnector", 0);
        VelocityMain.getInstance().getTaskManager().addTask(this::init);

    }
    private void init() {
        VelocityMain.getInstance().getLogger().info("Initializing integrated limbo server...");
        try {
            server = new LimboServer(new LimboConfiguration(), new ConsoleCommandHandler(), getClass().getClassLoader());
            VelocityMain.getInstance().getServer().registerServer(new ServerInfo("extremelimbo", (InetSocketAddress) server.getConfig().getAddress()));
            server.start();
        } catch(final Exception e){
            VelocityMain.getInstance().getLogger().error(e.getMessage());
        }
        VelocityMain.getInstance().getLogger().info("Successfully initialized!");
    }
    @Subscribe public void onInitConnect(final PlayerChooseInitialServerEvent e){
        if(!server.isRunning()) return;

        e.setInitialServer(VelocityMain.getInstance().getServer().getServer("extremelimbo").orElse(null));
    }
}
class LimboConfiguration implements LimboConfig {
    @Override
    public int getDebugLevel() {
        return -1;
    }

    @Override
    public boolean isUseBrandName() {
        return false;
    }

    @Override
    public boolean isUseJoinMessage() {
        return false;
    }

    @Override
    public boolean isUseBossBar() {
        return false;
    }

    @Override
    public boolean isUseTitle() {
        return false;
    }

    @Override
    public boolean isUsePlayerList() {
        return false;
    }

    @Override
    public boolean isUseHeaderAndFooter() {
        return false;
    }
    @Override
    public String getBrandName() {
        return "EXTREME ANTI BOT";
    }

    @Override
    public String getJoinMessage() {
        return "";
    }

    @Override
    public BossBar getBossBar() {
        return null;
    }

    @Override
    public Title getTitle() {
        return null;
    }

    @Override
    public String getPlayerListUsername() {
        return "";
    }

    @Override
    public String getPlayerListHeader() {
        return "";
    }

    @Override
    public String getPlayerListFooter() {
        return "";
    }

    @Override
    public boolean isUseEpoll() {
        return false;
    }

    @Override
    public int getBossGroupSize() {
        return 0;
    }

    @Override
    public int getWorkerGroupSize() {
        return 0;
    }

    @Override
    public double getInterval() {
        return 0;
    }

    @Override
    public double getMaxPacketRate() {
        return 0;
    }
    @Override
    public SocketAddress getAddress(){
        return new InetSocketAddress("localhost", 27776);
    }

    @Override
    public int getMaxPlayers() {
        return 10;
    }

    @Override
    public PingData getPingData() {
        return null;
    }

    @Override
    public String getDimensionType() {
        return "THE_END";
    }

    @Override
    public int getGameMode() {
        return 1;
    }

    @Override
    public InfoForwarding getInfoForwarding() {
        return null;
    }

    @Override
    public long getReadTimeout() {
        return 3000;
    }

}
