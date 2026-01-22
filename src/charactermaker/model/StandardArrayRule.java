package charactermaker.model;

import java.util.Arrays;
import java.util.List;

/**
 * {@link StandardArrayRule} == One of Rules that can be to fill up {@link Stats} of {@link CharacterHolder}
 * That one nearly the same to {@link DiceRollRule} but without the rolling dices.
 * There is only Standard array of pre-set numbers
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public class StandardArrayRule implements StatGenerationRule{

    /**
     * Return list of pre-set numbers
     * @return - list
     */
    @Override
    public List<Integer> generate() { return Arrays.asList(15,14,13,12,10,8); }

    /**
     * Accessor
     * @return Name of rule
     */
    @Override
    public String getName() { return "Standard Array"; }
}
