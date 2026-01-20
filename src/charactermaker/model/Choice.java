package charactermaker.model;

import java.util.Objects;
import java.util.function.Consumer;
/**
 * Представляет один вариант выбора для персонажа.
 * Например, выбор +1 к характеристике или выбор навыка.
 */
public class Choice {

    private final String groupId;
    private final String choiceId;
    private final String name;          // Название варианта (отображается в UI)
    private final String description;   // Подробное описание, что делает выбор
    private final int maxSelections;      // сколько можно выбрать из этой группы
    private final boolean exactRequired;
    private final Consumer<CharacterHolder> applyAction;      // Действие, которое выполняется при выборе
    private final Consumer<CharacterHolder> removeAction;

    /**
     * Конструктор
     * @param name название варианта
     * @param description описание варианта
     * @param applyAction действие, которое будет выполнено на CharacterHolder
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
        this.removeAction = Objects.requireNonNull(removeAction);
    }

    public String getGroupId()       { return groupId;          }
    public String getChoiceId()      { return choiceId; }
    public String getName()          { return name;              }
    public String getDescription()   { return description;       }
    public int getMaxSelections()    { return maxSelections;     }
    public boolean isExactRequired() { return exactRequired;     }

    /**
     * Применяет выбор к персонажу.
     * Обычно вызывается после того, как игрок сделал выбор в диалоге.
     */
    public void apply(CharacterHolder character) { applyAction.accept(character); }
    public void remove(CharacterHolder character) { if (removeAction != null) removeAction.accept(character); }

    @Override
    public String toString() { return name; } // Для отображения в списках/диалогах
}