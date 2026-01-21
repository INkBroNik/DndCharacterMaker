package charactermaker.model;

import charactermaker.enums.FeatureIds;
import charactermaker.enums.Stat;

/**
 * ChoiceStatBonusFeature.java - description
 *
 * @author YOUR NAME
 * @since 19 Dec 2025, 10:37:38 am
 */
public class ChoiceStatBonusFeature implements RacialFeature{
    private final String groupId;
    private final int bonus;
    private final int maxSelections;
    
    public ChoiceStatBonusFeature
        (String raceId, int bonus, int maxSelections) {
        this.groupId = raceId + ":" + FeatureIds.STAT_CHOICE;
        this.bonus = bonus;
        this.maxSelections = maxSelections;
    }

    @Override
    public String getName() { return "Ability Score Increase"; }

    @Override
    public String getDescription() { return "Choose stats to increase"; }

    @Override
    public void apply(CharacterHolder character) {
        // Создаём StatChoice для всех возможных статов
        for (Stat stat : Stat.values()) {
                character.addPendingChoice(new Choice(
                        groupId, stat.name(), stat.getName() + "+" + bonus,
                        "Increase " + stat.getName() + " by " + bonus,
                        maxSelections, true,
                        c -> c.getStats().addRacialBonuses(stat, bonus),
                        c -> c.getStats().addRacialBonuses(stat, -bonus)
                ));
        }
    }

    @Override
    public void remove(CharacterHolder character) {
        // Удаляем все StatChoice, которые были добавлены этим RacialFeature
        character.removeChoicesByGroupPrefix(groupId);
    }
}