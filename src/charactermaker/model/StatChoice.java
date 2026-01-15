package charactermaker.model;

import charactermaker.enums.Stat;

public class StatChoice extends Choice{
    private final Stat stat;   // STR, DEX, CON...
    private final int bonus;   // +1, +2 и т.д.

    public StatChoice(String sourceId, Stat stat, int bonus, int masSelections) {
        super( sourceId, stat.name() + " +" + bonus,
                "Increase " + stat.name() + " by " + bonus,
                masSelections, true,
                c -> c.getStats().addRacialBonuses(stat, bonus),
                c -> c.getStats().addRacialBonuses(stat, -bonus));
        this.stat = stat;
        this.bonus = bonus;
    }
    public Stat getStat() { return stat;    }
    public int getBonus() { return bonus;   }
}
