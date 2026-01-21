package charactermaker.model;

import java.util.Arrays;
import java.util.List;

public class StandardArrayRule implements StatGenerationRule{
    @Override
    public List<Integer> generate() { return Arrays.asList(15,14,13,12,10,8); }

    @Override
    public String getName() { return "Standard Array"; }
}
