package charactermaker.model.features;

import charactermaker.enums.Stat;
import charactermaker.model.dataHolders.CharacterHolder;

/**
 * {@link StatBonusFeature} == Class that work under rules of {@link RacialFeature}
 * Assign the bonuses for stats
 * @param stat - Stat which has racial bonus
 * @param value - value of the bonus
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public record StatBonusFeature (Stat stat, int value) implements RacialFeature{

    /**
     * Apply the bonus to the stat
     * @param character - where info holds
     */
    @Override
    public void apply(CharacterHolder character) { character.getStats().addRacialBonuses(stat, value); }

    /**
     * Remove the bonus of the stat
     * @param character - where info holds
     */
    @Override
    public void remove(CharacterHolder character) { character.getStats().addRacialBonuses(stat, -value); }

    //==============================================Accessors=========================================================//

    @Override
    public String getName() { return "+" + value + " " + stat; }
    @Override
    public String getDescription() { return "Increases " + stat + " by " + value + " for this character."; }
}
