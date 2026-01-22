package charactermaker.enums;

/**
 * {@link Stat} == Enum that contain the stat
 *
 * @author Nikita
 * @since 21/01/2026
 */
public enum Stat { 
    STR("STR"), DEX("DEX"), CON("CON"),
    INT("INT"), WIS("WIS"), CHA("CHA");

    private final String displayName;

    /**
     * Basic constructor
     * @param name - Name of Stat
     */
    Stat(String name)       { displayName = name; }

    /**
     * Accessor
     * @return - Name of Stat
     */
    public String getName() { return displayName; }
}
