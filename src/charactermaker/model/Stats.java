package charactermaker.model;
 
import charactermaker.enums.Race;
import charactermaker.enums.Stat;

import java.util.EnumMap;

/**
 * Stats.java - description
 *
 * @author YOUR NAME
 * @since 12 Dec 2025, 10:07:17 am
 */
public class Stats 
{
    private final EnumMap<Stat, Integer> racial = new EnumMap<>(Stat.class); // racial bonuses
    private final EnumMap<Stat, Integer> base = new EnumMap<>(Stat.class);
    private final int BASE = 10; // base value

    public Stats() {
        for (Stat s : Stat.values()){
            base.put(s, null);
            racial.put(s, 0);
        }
    }

    public  void        addRacialBonuses(Stat stat, int value)  {
        int old = racial.getOrDefault(stat, 0);
        int now = old + value;
        racial.put(stat, now);
    }
    public  void        addBaseStats(Stat stat, int value)      { base.put(stat, value); }
    public  void        removeBase(Stat stat)                   { base.put(stat, null); }
    public  int         getTotal(Stat stat)                         {
        int b = base.get(stat) == null ? 8 : base.get(stat);
        return racial.getOrDefault(stat, 0) + b; }
    public  Integer     getBase(Stat stat)                      { return base.get(stat); }
    public  Integer     getRacial(Stat stat)                    { return racial.get(stat); }
    private int         getMod(Stat stat)                       { return (int) Math.floor((getTotal(stat)-BASE)/2.0); }
   
    @Override
    public String toString(){
    return "\nStats:"   +
           "\n\tSTR:"   + getTotal(Stat.STR) +
           "\n\tDEX:"   + getTotal(Stat.DEX) +
           "\n\tCON:"   + getTotal(Stat.CON) +
           "\n\tINT:"   + getTotal(Stat.INT) +
           "\n\tWIS:"   + getTotal(Stat.WIS) +
           "\n\tCHA:"   + getTotal(Stat.CHA);
    }
}
