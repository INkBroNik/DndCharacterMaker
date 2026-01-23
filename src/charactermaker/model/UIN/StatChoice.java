package charactermaker.model.UIN;

import charactermaker.enums.Stat;
import charactermaker.model.dataHolders.CharacterHolder;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@link StatChoice} == Fabric to creating {@link Choice} that changes Stats.
 * Mods:
 *  - DELTA: add/remove delta (used for racial bonuses)
 *  - ASSIGN: set the base values
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public final class StatChoice {

    private StatChoice() {}

    /**
     * DELTA - increase the Stat
     * @param groupId - ID from source
     * @param stat - Stat which has bonus
     * @param delta - Bonus
     * @param maxSelections - Number of selections
     * @param exactRequired - exact required
     * @return - Choice
     */
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

    /**
     * ASSIGN - assign the base value for stat
     * @param groupId - ID of the source
     * @param stat - Stat
     * @param value - value of the base
     * @param maxSelections - Number of selections
     * @param exactRequired - - exact required
     * @return - Choice
     */
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
