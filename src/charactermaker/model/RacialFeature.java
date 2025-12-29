package charactermaker.model;
 
/**
 * RacialFeature.java - description
 *
 * @author YOUR NAME
 * @since 17 Dec 2025, 9:01:43 am
 */
public interface RacialFeature {
    String getName();

    // применить к персонажу (вызывается при создании/при смене расы)
    void apply(CharacterHolder character);

    // отменить (если нужно удалить/сменить расу)
    void remove(CharacterHolder character);
}