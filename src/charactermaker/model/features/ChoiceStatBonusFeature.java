package charactermaker.model.features;

import charactermaker.enums.FeatureIds;
import charactermaker.enums.Stat;
import charactermaker.model.UIN.Choice;
import charactermaker.model.UIN.StatChoice;
import charactermaker.model.dataHolders.CharacterHolder;

/**
 * {@link ChoiceStatBonusFeature} == Feature of {@link charactermaker.enums.Race}
 * that give choice of Stat racial bonuses for user
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public class ChoiceStatBonusFeature implements RacialFeature{
    private final String groupId;
    private final int bonus;
    private final int maxSelections;

    /**
     * Basic constructor
     * @param raceId - From what race goes choice
     * @param bonus - Plus what amount to stat
     * @param maxSelections - Number of max selections
     */
    public ChoiceStatBonusFeature
        (String raceId, int bonus, int maxSelections) {
        this.groupId = raceId + ":" + FeatureIds.STAT_CHOICE;
        this.bonus = bonus;
        this.maxSelections = maxSelections;
    }

    //================================================Accessors=======================================================//

    @Override
    public String getName() { return "Ability Score Increase"; }
    @Override
    public String getDescription() { return "Choose stats to increase"; }

    //================================================================================================================//

    /**
     * Create pending StatChoices for all stat
     * @param character - where holds info
     */
    @Override
    public void apply(CharacterHolder character) {
        for (Stat stat : Stat.values()) {
            Choice choice = StatChoice.delta(groupId, stat, +bonus, maxSelections, true);
            character.addPendingChoice(choice);
        }
    }

    /**
     * Delete all StatChoices for the Race
     * @param character - where holds info
     */
    @Override
    public void remove(CharacterHolder character) {
        character.removeChoicesByGroupPrefix(groupId);
    }
}