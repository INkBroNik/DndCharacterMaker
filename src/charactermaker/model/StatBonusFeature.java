package charactermaker.model;

import charactermaker.enums.Stat;

public record StatBonusFeature (Stat stat, int value) implements RacialFeature{

    @Override
    public void apply(CharacterHolder character) { character.getStats().addRacialBonuses(stat, value); }

    public String getName() { return "+" + value + " " + stat; }

    @Override
    public String getDescription() { return "Increases " + stat + " by " + value + " for this character."; }

    @Override
    public void remove(CharacterHolder character) { character.getStats().addRacialBonuses(stat, -value); }
}
