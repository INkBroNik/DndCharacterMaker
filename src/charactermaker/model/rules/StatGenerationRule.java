package charactermaker.model.rules;

import java.util.List;

public interface StatGenerationRule {
    List<Integer> generate();
    String getName();
    default boolean isPointBuy() { return false; }
}
