package ir.realstresser.extreme.shared.check;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@SuppressWarnings("unused") public class CheckBase {
    private String name;
    private int violation;
}
