package ir.realstresser.extreme.velocity.check.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import ir.realstresser.extreme.shared.check.CheckBase;
import ir.realstresser.extreme.velocity.VelocityMain;

@SuppressWarnings("unused") public class PlayerInserter extends CheckBase {
    public PlayerInserter(){
        super("PlayerInserter", 0);
    }
    @Subscribe public void preLoginEvent(final PreLoginEvent e){
        VelocityMain.getInstance().getTaskManager().addTask(() -> VelocityMain.getInstance().getSqlManager().addPlayer(e.getConnection().getRemoteAddress().getAddress().toString(), "", e.getUsername(), false, 0, "-1"));
    }
    @Subscribe public void loginEvent(final LoginEvent e){
        VelocityMain.getInstance().getTaskManager().addTask(() -> VelocityMain.getInstance().getSqlManager().updatePlayerByUsername(e.getPlayer().getUsername(), "uuid", e.getPlayer().getUniqueId().toString()));
    }
}
