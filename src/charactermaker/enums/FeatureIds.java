package charactermaker.enums;

import java.util.Optional;

/**
 * {@link FeatureIds} == Enum for storage the ID fir Feature
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public enum FeatureIds {
    STAT_CHOICE ("STAT_CHOCE"),
    STAT_BONUS  ("STAT_BONUS"),
    BASE_STAT   ("BASE_STAT");
    private final String id;

    /**
     * Basic constructor
     * @param id - ID of Feature
     */
    FeatureIds(String id) { this.id = id;   }

    /**
     * Accesser for id
     * @return - id
     */
    public String getId() { return id;      }

    /**
     * Finding the FeatureIds from id
     * @param id - id
     * @return - the FeatureIds
     */
    public static Optional<FeatureIds> from(String id) {
        for (FeatureIds v : values()) {
            if (v.id.equals(id)) { return Optional.of(v); }
        } return Optional.empty();
    }
}
