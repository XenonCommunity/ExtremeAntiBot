package ir.realstresser.extreme.shared.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("unused")
public class ConfigData {
    private String prefix;
    private String kickmessage;
    private int maxviolations;
    private String blacklistby;
    private int reconnectionseconds;
    private ChecksData checks;
}
