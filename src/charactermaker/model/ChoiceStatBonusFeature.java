package charactermaker.model;

import charactermaker.enums.Stat;

/**
 * ChoiceStatBonusFeature.java - description
 *
 * @author YOUR NAME
 * @since 19 Dec 2025, 10:37:38 am
 */
public class ChoiceStatBonusFeature implements RacialFeature{
    private final String sourceId;
    private final int bonus;
    private final int maxSelections;
    
    public ChoiceStatBonusFeature
        (String sourceId, int bonus, int maxSelections) {
        this.sourceId = sourceId;
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
                character.addPendingChoice(new StatChoice(sourceId, stat, bonus, maxSelections));
        }
    }

    @Override
    public void remove(CharacterHolder character) {
        // Удаляем все StatChoice, которые были добавлены этим RacialFeature
        character.removePendingChoiceBySource(sourceId);
    }
}