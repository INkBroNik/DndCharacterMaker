package charactermaker.enums;

import java.util.Optional;

public enum FeatureIds {
    STAT_CHOICE ("STAT_CHOCE"),
    STAT_BONUS  ("STAT_BONUS"),
    BASE_STAT   ("BASE_STAT");
    private final String id;
    private FeatureIds(String id) { this.id = id; }
    public String getId() { return id; }
    public static Optional<FeatureIds> from(String id) {
        for (FeatureIds v : values()) {
            if (v.id.equals(id)) { return Optional.of(v); }
        } return Optional.empty();
    }
}
