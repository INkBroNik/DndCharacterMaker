package charactermaker.model.UIN;

import charactermaker.model.dataHolders.CharacterHolder;

import java.util.Objects;
import java.util.function.Consumer;
/**
 * {@link Choice} == Class that represent one variant for {@link CharacterHolder}
 * Ex: +1 to stat
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public class Choice {

    private final String groupId;
    private final String choiceId;
    private final String name;
    private final String description;
    private final int maxSelections;
    private final boolean exactRequired;
    private final Consumer<CharacterHolder> applyAction;
    private final Consumer<CharacterHolder> removeAction;

    //============================================Constructor=========================================================//

    /**
     * Basic constructor
     * @param name - name of variant
     * @param description - description of variant
     * @param applyAction - action that would be done to {@link CharacterHolder}
     */
    public Choice(
            String groupId,String localChoiceId, String name, String description,
            int maxSelections, boolean exactRequired,
            Consumer<CharacterHolder> applyAction,
            Consumer<CharacterHolder> removeAction
    ) {
        this.groupId = Objects.requireNonNull(groupId);
        this.choiceId = groupId + ":" + Objects.requireNonNull(localChoiceId);
        this.name = Objects.requireNonNull(name);
        this.description = description == null ? "" : description;
        this.maxSelections = Math.max(1, maxSelections);
        this.exactRequired = exactRequired;
        this.applyAction = Objects.requireNonNull(applyAction);
        this.removeAction = removeAction;
    }

    //==============================================Accessors=========================================================//

    public String getGroupId()       { return groupId;          }
    public String getChoiceId()      { return choiceId;         }
    public String getName()          { return name;             }
    public String getDescription()   { return description;      }
    public int getMaxSelections()    { return maxSelections;    }
    public boolean isExactRequired() { return exactRequired;    }

    /**
     * Apply choice to {@link CharacterHolder}
     * @param character - where apply the choice
     */
    public void apply(CharacterHolder character) { applyAction.accept(character); }
    /**
     * Remove choice to {@link CharacterHolder}
     * @param character - where remove the choice
     */
    public void remove(CharacterHolder character) { if (removeAction != null) removeAction.accept(character); }

    //=========================================ToString/Utilites======================================================//

    /**
     * Override of ToString method
     * @return - name of the variant
     */
    @Override
    public String toString()        { return name;              }
}