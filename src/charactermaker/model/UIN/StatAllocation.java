package charactermaker.model.UIN;

import charactermaker.enums.Stat;
import charactermaker.model.dataHolders.CharacterHolder;

import java.util.*;

/**
 * {@link StatAllocation} == Class that work like a bride from UI and {@link CharacterHolder}.
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public class StatAllocation {

    private final EnumMap<Stat, Integer> assigned = new EnumMap<>(Stat.class);

    /**
     * Assigning the Stat-Value
     * @param stat - Stat
     * @param value - value of Stat
     */
    public void assign(Stat stat, Integer value) {
        Objects.requireNonNull(stat, "Stat");
        Objects.requireNonNull(value, "Value");
        if(assigned.containsKey(stat)) { throw new IllegalStateException("Stat " + " already assigned"); }
        assigned.put(stat, value);
    }

    /**
     * Clear the Stat-value
     * @param stat - Which stat
     */
    public void clear(Stat stat) { assigned.remove(stat); }

    /**
     * Chek of is Assigned the stat
     * @param stat - which stat
     * @return - boolean
     */
    public boolean isAssigned(Stat stat) { return assigned.containsKey(stat); }

    /**
     * Accessor
     * @param stat - which value by the stat
     * @return - value
     */
    public Integer get(Stat stat) { return assigned.get(stat); }

    /**
     * Return unmodifiable map of stat-value
     * @return - map of stat-value
     */
    public Map<Stat, Integer> asMap() { return Collections.unmodifiableMap(assigned); }

    /**
     * Return list of unassigned stats
     * @return - list of unassigned stats
     */
    public List<Stat> unassignedStats() {
        List<Stat> res = new ArrayList<>();
        for (Stat s : Stat.values()) if (!assigned.containsKey(s)) res.add(s);
        return res;
    }

    /**
     * Check that all stats has they value
     */
    public void validateComplete(){
        if (assigned.size() != Stat.values().length) {
            throw new IllegalStateException("Allocation incomplete: assigned " + assigned.size() + " stats");
        }
    }
}