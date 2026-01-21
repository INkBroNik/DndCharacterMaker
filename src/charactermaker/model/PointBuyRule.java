package charactermaker.model;

import charactermaker.enums.Stat;

import java.util.*;

public class PointBuyRule implements StatGenerationRule{

    private final int budget;
    private final Map<Integer, Integer> costTable;

    public PointBuyRule() { this(27);}

    public PointBuyRule(int budget) {
        this.budget = budget;
        costTable = new HashMap<>();
        costTable.put(8, 0);
        costTable.put(9, 1);
        costTable.put(10, 2);
        costTable.put(11, 3);
        costTable.put(12, 4);
        costTable.put(13, 5);
        costTable.put(14, 7);
        costTable.put(15, 9);
    }

    @Override
    public List<Integer> generate() { return new ArrayList<>(Collections.nCopies(6,8)); }

    @Override
    public String getName() { return "Point Buy"; }

    @Override
    public boolean isPointBuy() { return true; }

    public int getBudget() { return budget; }

    public int costOf(int value) { return costTable.getOrDefault(value, Integer.MAX_VALUE); }

    public int totalCost(Map<Stat, Integer> assigned) {
        int sum = 0;
        for (int v : assigned.values()) {
            int c = costOf(v);
            if (c == Integer.MAX_VALUE) throw new IllegalArgumentException("Invalid stat value for point buy: " + v);
            sum += c;
        }
        return sum;
    }
}
