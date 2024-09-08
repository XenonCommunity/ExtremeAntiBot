package ir.realstresser.extreme.velocity.check.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import ir.realstresser.extreme.shared.check.CheckBase;
import ir.realstresser.extreme.velocity.VelocityMain;

import java.util.Arrays;

@SuppressWarnings("unused") public class NameChecker extends CheckBase {
    private final String namesToCheck;
    public NameChecker(){
        super("NameChecker", VelocityMain.getInstance().getConfigData().getChecks().getNamechecker());
        namesToCheck = Arrays.toString(VelocityMain.getInstance().getConfigData().getChecks().getNamecheckernames());
    }
    @Subscribe public void preLoginEvent(final PreLoginEvent e){
        VelocityMain.getInstance().getTaskManager().addTask(() -> {
            if(namesToCheck.indexOf(e.getUsername()) != -1)
                VelocityMain.getInstance().getSqlManager().updatePlayerByUsername(e.getUsername(), "violationCount", getViolation());
        });
    }
}
