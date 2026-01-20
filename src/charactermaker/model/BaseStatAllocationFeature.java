package charactermaker.model;

import charactermaker.enums.Stat;

import java.util.Objects;

public class BaseStatAllocationFeature implements Feature{

    private final String groupId;
    private final boolean exactRequired;
    private final int points;

    public BaseStatAllocationFeature(int points, boolean exactRequired) {
        this.points = Math.max(points, 1);
        this.exactRequired = exactRequired;
        this.groupId = FeatureIds.BASE_STAT.getId();
    }

    @Override
    public void apply(CharacterHolder character) {
        Objects.requireNonNull(character);
        for (Stat stat : Stat.values()) {
            if(character.isBaseAssigned(stat)) continue;

            Choice c = new Choice(
                    groupId,
                    stat.name(),
                    stat.name(),
                    "+1 " + stat.getName(),
                    points,
                    true,
                    ch -> ch.setBaseStat(stat, 1),
                    ch -> ch.clearBaseStat(stat)
            );
            character.addPendingChoice(c);
        }
    }

    @Override
    public void remove(CharacterHolder character) {
        character.removePendingChoicesByGroupPrefix(groupId);
    }
    @Override
    public String getName() {
        return "Base stat allocation feature";
    }
    @Override
    public String getDescription() {
        return "Distribute " + points + " base stat points among stats.";
    }
}
