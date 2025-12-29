package charactermaker.enums;

/**
 *
 * @author INkBr
 */
public enum Stat { 
    STR("STR"), DEX("DEX"), CON("CON"), INT("INT"), WIS("WIS"), CHA("CHA"); 

    private final String displayName;
    
    Stat(String name)       { displayName = name; }
    public String getName() { return displayName; }
}
