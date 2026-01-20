package charactermaker.model;

public interface Feature {
    void apply(CharacterHolder character);
    void remove(CharacterHolder character);
    String getName();
    String getDescription();
}
