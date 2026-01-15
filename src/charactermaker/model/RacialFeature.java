package charactermaker.model;
 
/**
 * RacialFeature.java - description
 *
 * @author YOUR NAME
 * @since 17 Dec 2025, 9:01:43 am
 */
public interface RacialFeature extends Feature{
    String getName();
    String getDescription();
    void apply(CharacterHolder character);
    default void remove(CharacterHolder character){}
}