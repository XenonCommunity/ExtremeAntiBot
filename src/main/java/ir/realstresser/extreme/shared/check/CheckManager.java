package ir.realstresser.extreme.shared.check;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.listener.AbstractPacketListener;
import ir.realstresser.extreme.shared.enums.ProxyType;
import ir.realstresser.extreme.velocity.VelocityMain;
import ir.realstresser.extreme.velocity.check.listeners.LimboAPIConnector;
import org.reflections.Reflections;

import java.util.Arrays;

public class CheckManager {
    public void addAllChecks(final ProxyType type){
        VelocityMain.getInstance().getLogger().info("Initializing checks...");
        switch(type){
            case BUNGEE -> {

            }
            case VELOCITY -> {
                new Reflections("ir.realstresser.extreme.velocity.check.listeners").getSubTypesOf(CheckBase.class).forEach(
                        c -> Arrays.stream(VelocityMain.getInstance().getConfigData().getChecks().getEnables()).filter(s -> c.getSimpleName().equals(s)).forEach(
                                s -> {
                                    try {
                                        VelocityMain.getInstance().getLogger().info("Adding check " + c.getSimpleName());
                                        VelocityMain.getInstance().getServer().getEventManager().register(VelocityMain.getInstance(), c.getDeclaredConstructor().newInstance());
                                    } catch (final Exception ex) {
                                        VelocityMain.getInstance().getLogger().error(ex.getMessage());
                                    }
                                }));
                new Reflections("ir.realstresser.extreme.velocity.check.packetlisteners").getSubTypesOf(AbstractPacketListener.class).forEach(
                        c -> Arrays.stream(VelocityMain.getInstance().getConfigData().getChecks().getEnables()).filter(s -> c.getSimpleName().equals(s)).forEach(
                                s -> {
                                    try {
                                        VelocityMain.getInstance().getLogger().info("Adding packet check " + c.getSimpleName());
                                        Protocolize.listenerProvider().registerListener(c.getDeclaredConstructor().newInstance());
                                    } catch (final Exception ex) {
                                        VelocityMain.getInstance().getLogger().error(ex.getMessage());
                                    }
                                }));
            }
        }
        // temp code
        VelocityMain.getInstance().getServer().getEventManager().register(VelocityMain.getInstance(), new LimboAPIConnector());
        VelocityMain.getInstance().getLogger().info("Successfully initialized!");
    }
}
