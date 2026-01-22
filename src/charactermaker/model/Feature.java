package charactermaker.model;

/**
 * {@link Feature} == Interface that set the rules to be a feature
 */
public interface Feature {
    void apply(CharacterHolder character);
    void remove(CharacterHolder character);
    String getName();
    String getDescription();
}
