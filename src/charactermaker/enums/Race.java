package charactermaker.enums;

import charactermaker.model.ChoiceStatBonusFeature;
import charactermaker.model.RacialFeature;
import charactermaker.model.StatBonusFeature;
import java.util.List;

/**
 * {@link Race} == Enum that has races and race features.
 *
 * @author Nikita
 * @since 21/01/2026
 */
public enum Race {

    //================================================================================================================//

    DRAGONBORN  ("Dragonborn",
                "Proud and powerful with draconic ancestry.",
                "DRAGONBORN", NameCulture.DRAGONBORN,
                new StatBonusFeature(Stat.STR, 2),
                new StatBonusFeature(Stat.CHA, 1)
                ),
    DWARF       ("Dwarf",
                "Tough and hardy, resistant to poison.",
                "DWARF", NameCulture.DWARF,
                new StatBonusFeature(Stat.CON, 2)
                ),
    ELF         ("Elf",
                "Graceful and perceptive, skilled with magic and bows.",
                "ELF", NameCulture.ELF,
                new StatBonusFeature(Stat.DEX, 2)
                ),
    GNOME       ("Gnome",
                "Curious and intelligent tinkerers.",
                "GNOME", NameCulture.GNOME,
                new StatBonusFeature(Stat.INT, 2)
                ),
    HALF_ELF    ("Half-Elf",
                "Charismatic and versatile, inheriting both humans and elves.",
                "HALF_ELF", NameCulture.HALF_ELF,
                new StatBonusFeature(Stat.CHA, 2),
                new ChoiceStatBonusFeature("HALF_ELF:" + FeatureIds.STAT_CHOICE, 1, 2)),
    HALFLING    ("Halfling",
                "Lucky and nimble, hard to hit.",
                "HALFLING", NameCulture.HALFLING,
                new StatBonusFeature(Stat.DEX, 2)
                ),
    HALF_ORK    ("Half-Ork",
                "Proud and powerful with draconic ancestry.",
                "HALF_ORC", NameCulture.ORK,
                new StatBonusFeature(Stat.STR, 2),
                new StatBonusFeature(Stat.CON, 1)
                ),
    HUMAN       ("Human",
                "Versatile and adaptable, capable in any class.",
                "HUMAN", NameCulture.HUMAN,
                new StatBonusFeature(Stat.STR, 1),
                new StatBonusFeature(Stat.DEX, 1),
                new StatBonusFeature(Stat.CON, 1),
                new StatBonusFeature(Stat.INT, 1),
                new StatBonusFeature(Stat.WIS, 1),
                new StatBonusFeature(Stat.CHA, 1)
                ),
    ORC         ("Orc",
                "Fierce and relentless warriors known for raw power.",
                "ORC", NameCulture.ORK,
                new StatBonusFeature(Stat.STR, 2),
                new StatBonusFeature(Stat.CON, 1)
                ),
    TIEFLING    ("Tiefling",
                "Fiend-touched, naturally magical.",
                "TIEFLING", NameCulture.TIEFLING,
                new StatBonusFeature(Stat.INT, 1),
                new StatBonusFeature(Stat.CHA, 2)
                );

    //================================================================================================================//

    private final String displayName, description;
    private final List<RacialFeature> features;
    private final String id;
    private final NameCulture culture;

    /**
     * Basic constructor
     * @param displayName - Name of the Race
     * @param description - Description of race
     * @param id - ID of race
     * @param culture - Names for races
     * @param features - Features that Race has
     */
    Race(String displayName, String description, String id, NameCulture culture, RacialFeature... features){
        this.displayName = displayName;
        this.description = description;
        this.features = List.of(features);
        this.id = id;
        this.culture = culture;
    }

    /**
     * Method for finding race from name
     * @param name - Name of race
     * @return - Race
     */
    public static Race findByName(String name){
        for (Race race : Race.values()){ if (race.getDisplayName().equals(name)){ return race; } }
        return null;
    }

    //==============================================Accessors=========================================================//

    public String                   getDisplayName()            { return displayName;                }
    public String                   getDescription()            { return description;                }
    public List<RacialFeature>      getFeatures()               { return features;                   }
    public String                   getId()                     { return id;                         }

    //================================================================================================================//

    /**
     * Method that return random name.
     * @param sex - sex of {@link charactermaker.model.CharacterHolder}
     * @return - Random name
     */
    public String                   randomName(Sex sex)         { return culture.randomName(sex);    }
}