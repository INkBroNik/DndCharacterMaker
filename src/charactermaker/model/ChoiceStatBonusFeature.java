package charactermaker.model;
 
/**
 * ChoiceStatBonusFeature.java - description
 *
 * @author YOUR NAME
 * @since 19 Dec 2025, 10:37:38 am
 */
public class ChoiceStatBonusFeature implements RacialFeature{
    private final String ID, NAME, DESC;
    private final int COUNT; // сколько выборов
    private final int BONUS_PER_CHOICE; // обычно 1
    
    public ChoiceStatBonusFeature
        (String id, String name, String desc, int count, int bonusPerChoice) {
        ID = id; NAME = name; DESC = desc; 
        COUNT = count; BONUS_PER_CHOICE = bonusPerChoice;
    }

    @Override
    public String getId() { return ID; }

    @Override
    public String getName() { return NAME; }

    @Override
    public String getDescription() { return DESC; }

    @Override
    public void apply(CharacterHolder character) {
    }

    @Override
    public void remove(CharacterHolder character) {
    }
}