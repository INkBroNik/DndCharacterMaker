package charactermaker.model;

import charactermaker.enums.Dice;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DiceRollRule} == One of Rules that can be to fill up {@link Stats} of {@link CharacterHolder}
 * That one use {@link Dice} for rolling sum of 4 dices without the smallest number, 6 time to all stats
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public class DiceRollRule implements StatGenerationRule {

    /**
     * Generate the List if integers for fill up
     * @return - List of integers
     */
    @Override
    public List<Integer> generate() {
        List<Integer> rolls = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) { rolls.add(Dice.D6.statRoll()); }
        return rolls;
    }

    /**
     * Accessor to name of the rule
     * @return - Name
     */
    @Override
    public String getName() { return "4d6 Drop Lowest Roll"; }
}
