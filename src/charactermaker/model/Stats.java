package charactermaker.model;
 
import charactermaker.enums.Stat;
import java.util.EnumMap;

/**
 * {@link Stats}- Class that holds Stats of the {@link CharacterHolder} and all that linked to it
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public class Stats 
{
    private final EnumMap<Stat, Integer> racial = new EnumMap<>(Stat.class); // racial bonuses
    private final EnumMap<Stat, Integer> base = new EnumMap<>(Stat.class);
    private final int BASE = 10; // base value

    /**
     * Basic constructor
     */
    public Stats() { for (Stat s : Stat.values()){ base.put(s, null); racial.put(s, 0); } }

    //================================================================================================================//

    /**
     * Add racial bonuses
     * @param stat - which stat
     * @param value - bonus
     */
    public  void        addRacialBonuses(Stat stat, int value)  {
        int old = racial.getOrDefault(stat, 0);
        int now = old + value;
        racial.put(stat, now);
    }

    /**
     * Set the base Stat
     * @param stat - which stat
     * @param value - value of the stat
     */
    public  void        setBase(Stat stat, int value)      { base.put(stat, value); }

    /**
     * Remove the base by the stat
     * @param stat - which stat
     */
    public  void        removeBase(Stat stat)                   { base.put(stat, null); }

    /**
     * Return total value of Stat
     * @param stat - which stat
     * @return - total value
     */
    public  int         getTotal(Stat stat)                         {
        int b = base.get(stat) == null ? 8 : base.get(stat);
        return racial.getOrDefault(stat, 0) + b;
    }

    //==============================================Accessors=========================================================//

    public  Integer     getBase(Stat stat)                      { return base.get(stat); }
    public  Integer     getRacial(Stat stat)                    { return racial.get(stat); }
    private int         getMod(Stat stat)                       { return (int) Math.floor((getTotal(stat)-BASE)/2.0); }

    //==========================================ToString/Utilites=====================================================//

    @Override
    public String toString(){
    return "\tStats:"   +
           "\n\t\t\tSTR:"   + getTotal(Stat.STR) +
           "\n\t\t\tDEX:"   + getTotal(Stat.DEX) +
           "\n\t\t\tCON:"   + getTotal(Stat.CON) +
           "\n\t\t\tINT:"   + getTotal(Stat.INT) +
           "\n\t\t\tWIS:"   + getTotal(Stat.WIS) +
           "\n\t\t\tCHA:"   + getTotal(Stat.CHA);
    }
}
