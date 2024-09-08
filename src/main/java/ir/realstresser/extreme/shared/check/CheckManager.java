package ir.realstresser.extreme.shared.check;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import io.github.retrooper.packetevents.velocity.factory.VelocityPacketEventsBuilder;
import ir.realstresser.extreme.shared.enums.ProxyType;
import ir.realstresser.extreme.velocity.VelocityMain;
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
                                    } catch (Exception ex) {
                                        VelocityMain.getInstance().getLogger().error(ex.getMessage());
                                    }
                                }));
                new Reflections("ir.realstresser.extreme.velocity.check.packetlisteners").getSubTypesOf(CheckBase.class).forEach(
                        c -> Arrays.stream(VelocityMain.getInstance().getConfigData().getChecks().getEnables()).filter(s -> c.getSimpleName().equals(s)).forEach(
                                s -> {
                                    try {
                                        VelocityMain.getInstance().getLogger().info("Adding check " + c.getSimpleName());
                                        PacketEvents.getAPI().getEventManager().registerListener((PacketListener) c.getDeclaredConstructor().newInstance(),  PacketListenerPriority.NORMAL);
                                    } catch (Exception ex) {
                                        VelocityMain.getInstance().getLogger().error(ex.getMessage());
                                    }
                                }));
            }
        }
        VelocityMain.getInstance().getLogger().info("Successfully initialized!");
    }
}
