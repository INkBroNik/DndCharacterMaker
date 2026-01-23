package charactermaker.model.rules;

import charactermaker.enums.Stat;
import charactermaker.model.dataHolders.CharacterHolder;
import charactermaker.model.dataHolders.Stats;

import java.util.*;

/**
 * {@link PointBuyRule} == One of Rules that can be to fill up {@link Stats} of {@link CharacterHolder}
 * That one set the rules for buy stats for points
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public class PointBuyRule implements StatGenerationRule{

    private final int budget;
    private final Map<Integer, Integer> costTable;

    //=============================================Constructors=======================================================//

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

    //================================================================================================================//

    /**
     * Generate the array
     * @return - array
     */
    @Override
    public List<Integer> generate() { return new ArrayList<>(Collections.nCopies(6,8)); }

    //==================================================Accessors=====================================================//

    @Override
    public String getName()         { return "Point Buy"; }
    public int getBudget()          { return budget; }

    /**
     * Check that rule is Point Buy
     * @return - true
     */
    @Override
    public boolean isPointBuy()     { return true; }

    /**
     * Return cost of value
     * @param value - value to get a cost
     * @return - cost
     */
    public int costOf(int value)    { return costTable.getOrDefault(value, Integer.MAX_VALUE); }

    /**
     * Get total cost of the values
     * @param assigned - values of all assigned Stats
     * @return - cost of all values
     */
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
