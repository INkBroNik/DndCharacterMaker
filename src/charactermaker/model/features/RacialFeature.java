package charactermaker.model.features;

import charactermaker.model.dataHolders.CharacterHolder;

/**
 * {@link RacialFeature} == Interface is the {@link Feature} and state the rules to be Racial feature
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public interface RacialFeature extends Feature{
    String getName();
    String getDescription();
    void apply(CharacterHolder character);
    default void remove(CharacterHolder character){}
}