package charactermaker.model;

import charactermaker.enums.Stat;

public class BaseStatAllocationFeature implements Feature{

    private final int points;

    public BaseStatAllocationFeature(int points){
        this.points = points;
    }

    @Override
    public void apply(CharacterHolder character) {
        for (Stat stat : Stat.values()) {
            character.addPendingChoice(new Choice(
                    "BASE_STAT",
                    stat.name(),
                    "+1 " + stat,
                    points,
                    true,
                    ch -> ch.getStats().addBaseStats(stat, 1),
                    ch -> ch.getStats().addBaseStats(stat, -1)
            ));
        }
    }

    @Override
    public void remove(CharacterHolder character) {
        character.removeChoicesBySource("BASE_STAT");
    }
}
