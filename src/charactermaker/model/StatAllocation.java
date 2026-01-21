package charactermaker.model;

import charactermaker.enums.Stat;

import java.util.*;

public class StatAllocation {

    private final EnumMap<Stat, Integer> assigned = new EnumMap<>(Stat.class);

    public void assign(Stat stat, Integer value) {
        Objects.requireNonNull(stat, "Stat");
        Objects.requireNonNull(value, "Value");
        if(assigned.containsKey(stat)) { throw new IllegalStateException("Stat " + " already assigned"); }
        assigned.put(stat, value);
    }

    public void clear(Stat stat) { assigned.remove(stat); }
    public boolean isAssigned(Stat stat) { return assigned.containsKey(stat); }
    public Integer get(Stat stat) { return assigned.get(stat); }
    public Map<Stat, Integer> asMap() { return Collections.unmodifiableMap(assigned); }
    public List<Stat> unassignedStats() {
        List<Stat> res = new ArrayList<>();
        for (Stat s : Stat.values()) if (!assigned.containsKey(s)) res.add(s);
        return res;
    }
    public void validateComplete(){
        if (assigned.size() != Stat.values().length) {
            throw new IllegalStateException("Allocation incomplete: assigned " + assigned.size() + " stats");
        }
    }
}