package charactermaker.enums;

import java.util.Map;

/**
 *
 * @author INkBr
 */
public enum Race {
    DRAGONBORN  ("Draganborn",
                "Proud and powerful with draconic ancestry.",
                Map.of(Stat.STR,2,Stat.CHA,1)),
    DWARF       ("Dwarf",
                "Tough and hardy, resistant to poison.",
                Map.of(Stat.CON,2)),
    ELF         ("Elf",
                "Graceful and perceptive, skilled with magic and bows.",
                Map.of(Stat.DEX,2)),
    GNOME       ("Gnome",
                "Curious and intelligent tinkerers.",
                Map.of(Stat.INT, 2)),
    HALF_ELF    ("Half-Elf",
                "Charismatic and versatile, inheriting both humans and elves.",
                Map.of(Stat.CHA,2)),
    HALFLING    ("Halfling",
                "Lucky and nimble, hard to hit.",
                Map.of(Stat.DEX,2)),
    HALF_ORK    ("Half-Ork",
                "Proud and powerful with draconic ancestry.",
                Map.of(Stat.STR,2,Stat.CON,1)),
    HUMAN       ("Human",
                "Versatile and adaptable, capable in any class.",
                Map.of(Stat.STR,1,Stat.DEX,1,Stat.CON,1,
                       Stat.INT,1,Stat.WIS,1,Stat.CHA,1)),
    ORC         ("Orc",
                "Fierce and relentless warriors known for raw power.",
                Map.of(Stat.STR,2,Stat.CON,1)),
    TIEFLING    ("Tiefling",
                "Fiend-touched, naturally magical.",
                Map.of(Stat.INT,1,Stat.CHA,2));

    private final String displayName, description;
    private final Map<Stat, Integer> bonuses;
    
    Race(String displayName, String description, Map<Stat, Integer> bonuses){
        this.displayName = displayName;
        this.description = description;
        this.bonuses = bonuses;
    }

    public static Race findByName(String name){
        for (Race race : Race.values()){
            if (race.getDisplayName().equals(name)){
                return race;
            }
        }
        return null;
    }

    public String               getDisplayName()    { return displayName;   }
    public String               getDescription()    { return description;   }
    public Map<Stat, Integer>   getBonuses()        { return bonuses;       }   
}
