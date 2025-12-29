package charactermaker.model;

import charactermaker.enums.Race;

/*
 * Character - description
 * 
 * @author Nikita Padalka
 * @since 26 Oct 2025
*/
public class CharacterHolder {
    private String      name;
    private int         age;
    private int         level;
    private Stats       stats;
    private Race        race;
    
    public CharacterHolder
        (String name, int age, int level, Stats stats, Race race) {
        this.name  = name;
        this.age   = age;
        this.level = level;
        this.stats = stats;
        this.race  = race;
    }
    
    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }
    public int getLevel()               { return level; }
    public void setLevel(int level)     { this.level = level; }
    public Stats getStats()             { return stats; }
    public void setStats(Stats stats)   { this.stats = stats; }
    public int getMaxHP()               { return 10 + stats.getConMod(); }
    public Race getRace()               { return race; }
    public void setRace(Race race)      { this.race = race; }

    @Override
    public String toString(){
        return "Character: " + 
                "\n\tName:\t"       + name  + 
                "\n\tAge:\t"        + age   +
                "\n\tLevel:\t"      + level +
                "\n\tRace:\t"       + race.getDisplayName() +
                "\n\tStats:\tSTR:"  + stats.getSTR() +
                "\n\t\tDEX:"        + stats.getDEX() +
                "\n\t\tCON:"        + stats.getCON() +
                "\n\t\tINT:"        + stats.getINT() +
                "\n\t\tWIS:"        + stats.getWIS() +
                "\n\t\tCHA:"        + stats.getCHA();
    }
}
