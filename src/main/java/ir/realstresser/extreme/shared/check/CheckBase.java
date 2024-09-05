package ir.realstresser.extreme.shared.check;

import com.velocitypowered.api.proxy.Player;
import ir.realstresser.extreme.shared.util.SQLManager;
import ir.realstresser.extreme.velocity.Main;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import org.reflections.Reflections;

@AllArgsConstructor
public class CheckBase {
    private String name;
}
