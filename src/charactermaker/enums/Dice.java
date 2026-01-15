package charactermaker.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Dice {
    D2(2),
    D4(4),
    D6(6),
    D8(8),
    D10(10),
    D12(12),
    D20(20),
    D100(100);

    private final int maxSides;
//    private final int minNumber;
    Dice(int maxSides){
        this.maxSides = maxSides;
//        minNumber = 1;
    }

    public int roll(){ return ThreadLocalRandom.current().nextInt(1, maxSides + 1); }
    public int roll(int count){
        int sum = 0;
        for (int i = 0; i < count; i++) { sum += ThreadLocalRandom.current().nextInt(1, maxSides + 1); }
        return sum;
    }
    public int statRoll(){
        int sum = 0;
        int [] numbers = new int[4];
        for (int i = 0; i < 4; i++) {
            numbers[i] = roll();
        }
        int minIndex = findMin(numbers);
        List<Integer> list = new ArrayList<>();
        for (int num : numbers) {
            list.add(num);
        }
        list.remove(minIndex);

        for (int i : list){ sum += i; }
        return sum;
    }
    private int findMin(int[] array){
        int min = array[0];
        int minIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                minIndex = i;// Обновляем, если нашли меньшее
            }
        }
        return minIndex;
    }
}
