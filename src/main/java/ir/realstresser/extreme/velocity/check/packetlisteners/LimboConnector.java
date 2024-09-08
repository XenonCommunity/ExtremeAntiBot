package ir.realstresser.extreme.velocity.check.packetlisteners;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.PluginContainer;
import ir.realstresser.extreme.shared.check.CheckBase;
import ir.realstresser.extreme.velocity.VelocityMain;
import net.elytrium.limboapi.api.Limbo;
import net.elytrium.limboapi.api.LimboFactory;
import net.elytrium.limboapi.api.LimboSessionHandler;
import net.elytrium.limboapi.api.chunk.Dimension;
import net.elytrium.limboapi.api.event.LoginLimboRegisterEvent;
import net.elytrium.limboapi.api.player.LimboPlayer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused") public class LimboConnector extends CheckBase implements LimboSessionHandler, PacketListener {
    private final Limbo limboServer;

    public LimboConnector() {
        super("LimboConnector", 0);
        final LimboFactory factory = (LimboFactory) VelocityMain.getInstance().getServer().getPluginManager().getPlugin("limboapi").flatMap(PluginContainer::getInstance).orElseThrow();
        this.limboServer = factory.createLimbo(factory.createVirtualWorld(Dimension.OVERWORLD, 0, 100, 0, (float) 90, (float) 0.0)).setName("LimboServer").setWorldTime(6000);
    }
    @Subscribe public void onConnect(final LoginLimboRegisterEvent e){
        VelocityMain.getInstance().getTaskManager().addTask(() -> {
            limboServer.spawnPlayer(e.getPlayer(), this);
        });
    }
    @Override public void onSpawn(final Limbo server, final LimboPlayer player) {
        player.disableFalling();

    }

    @Override public void onDisconnect() {
    }
}
