package charactermaker.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * {@link Dice} == Enum for rolling dice.
 *
 * @author Nikita Padalka
 * @since 21/01/2026
 */
public enum Dice {
    D2(2),
    D4(4),
    D6(6),
    D8(8),
    D10(10),
    D12(12),
    D20(20),
    D100(100);

    /// The number of sides of e dice
    private final int maxSides;

    /**
     * Basic constructor
     * @param maxSides - number of sides of dice
     */
    Dice(int maxSides)        { this.maxSides = maxSides; }

    /**
     * Roll the number
     * @return - rolled number
     */
    public int roll()         { return ThreadLocalRandom.current().nextInt(1, maxSides + 1); }

    /**
     * Roll some amount of dices
     * @param count - how much dices
     * @return - sum of rolled dices
     */
    public int roll(int count) {
        int sum = 0;
        for (int i = 0; i < count; i++) { sum += roll(); }
        return sum;
    }

    /**
     * Special method for rolling the stats for a character.
     * Rolled the numbers and throw the smallest number out
     * @return - sum of 3 other numbers
     */
    public int statRoll() {
        int sum = 0;
        int [] numbers = new int[4];
        for (int i = 0; i < 4; i++) { numbers[i] = roll(); }
        int minIndex = findMin(numbers);
        List<Integer> list = new ArrayList<>();
        for (int num : numbers) { list.add(num); }
        list.remove(minIndex);

        for (int i : list){ sum += i; }
        return sum;
    }

    /**
     * Private method for finding the smallest number from the array
     * @param array - array of numbers
     * @return - the index of smallest number in array
     */
    private int findMin(int[] array) {
        int min = array[0];
        int minIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
}
