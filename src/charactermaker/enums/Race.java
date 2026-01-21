package charactermaker.enums;

import charactermaker.model.ChoiceStatBonusFeature;
import charactermaker.model.RacialFeature;
import charactermaker.model.StatBonusFeature;
import java.util.List;

/**
 *
 * @author INkBr
 */
public enum Race {
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

    private final String displayName, description;
    private final List<RacialFeature> features;
    private final String id;
    private final NameCulture culture;
    
    Race(String displayName, String description, String id, NameCulture culture, RacialFeature... features){
        this.displayName = displayName;
        this.description = description;
        this.features = List.of(features);
        this.id = id;
        this.culture = culture;
    }

    public static Race findByName(String name){
        for (Race race : Race.values()){ if (race.getDisplayName().equals(name)){ return race; } }
        return null;
    }

    public String                   getDisplayName()            { return displayName;                }
    public String                   getDescription()            { return description;                }
    public List<RacialFeature>      getFeatures()               { return features;                   }
    public String                   getId()                     { return id;                         }
    public String                   randomName(Gender gender)   { return culture.randomName(gender); }
}