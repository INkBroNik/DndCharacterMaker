package charactermaker.enums;

/**
 * {@link Sex} == Enum that contain the sex for {@link charactermaker.model.CharacterHolder}
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public enum Sex {
    MALE    ("Male"),
    FEMALE  ("Female"),
    UNKNOWN ("Unknown");

    private final String displayName;

    /**
     * Basic constructor
     * @param displayName - Name of sex
     */
    Sex(String displayName)         { this.displayName = displayName;   }

    /**
     * Accessor
     * @return - Name
     */
    public String getDisplayName()  { return displayName;               }

    /**
     * Method for finding {@link Sex} by name
     * @param name - Name of {@link Sex}
     * @return - Sex
     */
    public static Sex findByName(String name){
        for (Sex sex : Sex.values()){ if (sex.getDisplayName().equals(name)){ return sex; } }
        return null;
    }
}
