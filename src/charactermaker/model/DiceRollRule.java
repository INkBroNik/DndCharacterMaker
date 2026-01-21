package charactermaker.model;

import charactermaker.enums.Dice;

import java.util.ArrayList;
import java.util.List;

public class DiceRollRule implements StatGenerationRule {
    @Override
    public List<Integer> generate() {
        List<Integer> rolls = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) { rolls.add(Dice.D6.statRoll()); }
        return rolls;
    }

    @Override
    public String getName() { return "4d6 Drop Lowest Roll"; }
}
