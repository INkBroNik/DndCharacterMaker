package charactermaker.model;

import charactermaker.enums.Stat;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Удобная фабрика для создания Choice, изменяющих характеристики.
 * Режимы:
 *  - DELTA: добавляет/вычитает delta (используется для racial bonuses)
 *  - ASSIGN: устанавливает значение base (используется для point-buy/standard-array)
 *
 * Choice создаются с groupId и локальным localId = stat.name()
 */
public final class StatChoice {

    private StatChoice() {}

    // DELTA: увеличивает/уменьшает значение (например +1)
    public static Choice delta(String groupId, Stat stat, int delta, int maxSelections, boolean exactRequired) {
        Objects.requireNonNull(groupId);
        Objects.requireNonNull(stat);

        String localId = stat.name();
        String display = stat.name() + (delta >= 0 ? " +" + delta : " " + delta);
        String desc = (delta >= 0 ? "Increase " : "Decrease ") + stat.name() + " by " + Math.abs(delta);

        Consumer<CharacterHolder> apply = ch -> ch.getStats().addRacialBonuses(stat, delta);
        Consumer<CharacterHolder> remove = ch -> ch.getStats().addRacialBonuses(stat, -delta);

        return new Choice(groupId, localId, display, desc, maxSelections, exactRequired, apply, remove);
    }

    // ASSIGN: присваивает конкретное base значение или добавляет очко (зависит от реализации setBaseStat)
    // apply вызывает character.setBaseStat(stat, value) — он должен защищать от перезаписи
    public static Choice assignBase(String groupId, Stat stat, int value, int maxSelections, boolean exactRequired) {
        Objects.requireNonNull(groupId);
        Objects.requireNonNull(stat);

        String localId = stat.name();
        String display = stat.name() + " = " + value;
        String desc = "Assign base " + stat.name() + " = " + value;

        Consumer<CharacterHolder> apply = ch -> ch.setBaseStat(stat, value);
        Consumer<CharacterHolder> remove = ch -> ch.clearBaseStat(stat);

        return new Choice(groupId, localId, display, desc, maxSelections, exactRequired, apply, remove);
    }
}
