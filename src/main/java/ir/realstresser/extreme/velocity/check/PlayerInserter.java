package ir.realstresser.extreme.velocity.check;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import ir.realstresser.extreme.shared.check.CheckBase;
import ir.realstresser.extreme.shared.util.SQLManager;
import ir.realstresser.extreme.velocity.Main;

@SuppressWarnings("unused") public class PlayerInserter extends CheckBase {
    public PlayerInserter(){
        super("PlayerInserter");
    }
    @Subscribe public void preLoginEvent(final PreLoginEvent e){
        Main.getInstance().getTaskManager().addTask(() -> SQLManager.addPlayer("", e.getConnection().getRemoteAddress().toString(), e.getUsername(), false, 0));
    }
    @Subscribe public void loginEvent(final LoginEvent e){
        Main.getInstance().getTaskManager().addTask(() -> SQLManager.updatePlayer(e.getPlayer().getUsername(), "uuid", e.getPlayer().getUniqueId().toString()));
    }
}
