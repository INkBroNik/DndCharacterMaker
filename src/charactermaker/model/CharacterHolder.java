package charactermaker.model;

import charactermaker.enums.Gender;
import charactermaker.enums.Race;
import charactermaker.enums.Stat;

import java.util.*;

/**
 * Character - description
 *
 * @author Nikita Padalka
 * @since 26 Oct 2025
 */
public class CharacterHolder {
    private String name;
    private int age;
    private int level;
    private final Stats stats = new Stats();
    private Race race;
    private Gender gender;

    private final List<RacialFeature> appliedFeatures = new ArrayList<>();
    private final List<Choice> pendingChoices = new ArrayList<>();
    private final Map<String, Integer> appliedChoicesCount = new HashMap<>();
    private final List<Choice> appliedChoices = new ArrayList<>();

    // ---------------- basic getters/setters ----------------

    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    public int getAge()                     { return age; }
    public void setAge(int age)             { this.age = age; }

    public int getLevel()                   { return level; }
    public void setLevel(int level)         { this.level = level; }

    public Stats getStats()                 { return stats; }

    public Gender getGender()               { return gender; }
    public void setGender(Gender gender)    { this.gender = gender; }

    public Race getRace()                   { return race; }

    // ---------------- race application ----------------

    /**
     * Apply new race and clean the old info about old race.
     * newRace == null will remove current race.
     */
    public void applyRace(Race newRace) {
        // 1. Если раса уже была — корректно убираем её
        if (this.race != null) {

            String oldRaceId = this.race.getId();

            // 1.1 Откатываем ВСЕ применённые choices этой расы
            appliedChoices.removeIf(choice -> {
                if (choice.getSourceId().startsWith(oldRaceId)) {
                    choice.remove(this);
                    return true;
                }
                return false;
            });

            // 1.2 Убираем racial features
            for (RacialFeature feature : appliedFeatures) {
                feature.remove(this);
            }

            appliedFeatures.clear();

            // 1.3 Чистим служебные структуры
            pendingChoices.removeIf(c -> c.getSourceId().startsWith(oldRaceId));
            appliedChoicesCount.keySet().removeIf(k -> k.startsWith(oldRaceId));
        }

        // 2. Применяем новую расу
        this.race = newRace;

        for (RacialFeature feature : newRace.getFeatures()) {
            feature.apply(this);
            appliedFeatures.add(feature);
        }
    }

    // ---------------- pending choices API ----------------

    /**
     * Add a pending choice. Prevents nulls and duplicates by sourceId+name.
     */
    public void addPendingChoice(Choice choice) {
        if (choice == null) throw new IllegalArgumentException("choice == null");
        // avoid duplicate exact same choice object or same source+name
        boolean exists = pendingChoices.stream().anyMatch(
                c -> Objects.equals(c.getSourceId(), choice.getSourceId())
                        && Objects.equals(c.getName(), choice.getName())
        );
        if (!exists) pendingChoices.add(choice);
    }

    /**
     * Return unmodifiable view — UI can read but not mutate list directly.
     */
    public List<Choice> getPendingChoices() {
        return Collections.unmodifiableList(pendingChoices);
    }

    /**
     * Remove pending choice without applying (cancel).
     */
    public boolean cancelPendingChoice(Choice choice) {
        return pendingChoices.remove(choice);
    }

    /**
     * Resolve (apply) the choice using model validation and counters.
     * This method will throw IllegalStateException if limit exceeded.
     */
    public void resolveChoice(Choice choice, int maxForSource) {
        applyChoice(choice, maxForSource);
    }

    void removePendingChoiceBySource(String sourceId) {
        pendingChoices.removeIf(c -> c.getSourceId().equals(sourceId));
    }

    public void removeChoicesBySource(String sourceId) {
        // удаляем неподтверждённые выборы
        pendingChoices.removeIf(c -> c.getSourceId().equals(sourceId));

        // сбрасываем счётчик применённых выборов
        appliedChoicesCount.remove(sourceId);
    }
    // ---------------- choice counters & application ----------------

    /**
     * Check ability to apply choice by sourceId and max allowed.
     */
    public boolean canApplyChoice(String sourceId, int max) {
        return appliedChoicesCount.getOrDefault(sourceId, 0) < max;
    }

    /**
     * Apply the choice to the character.
     * This method updates counters and removes the choice from pending list.
     */
    public void applyChoice(Choice choice, int max) {
        if (choice == null) throw new IllegalArgumentException("choice == null");
        String id = choice.getSourceId();
        if (id == null) id = ""; // defensive

        if (!canApplyChoice(id, max)) {
            throw new IllegalStateException("Too many choices applied for: " + id);
        }

        // apply action (Choice should encapsulate its action)
        choice.apply(this);
        appliedChoices.add(choice);

        appliedChoicesCount.put(id, appliedChoicesCount.getOrDefault(id, 0) + 1);
        pendingChoices.remove(choice);
    }

    // ----------------------- Stats ----------------------

    public List<Stat> getUnassignedStats() {
        List<Stat> result = new ArrayList<>();
        for (Stat s : Stat.values()) {
            Integer v = this.getStats().getBase(s);
            if (v == null) result.add(s);
        }
        return result;
    }

    public boolean isBaseAssigned(Stat stat)        { return this.getStats().getBase(stat) != null; }
    public void setBaseStat(Stat stat, int value)   {
        if( isBaseAssigned(stat) ) { throw new IllegalStateException(stat + "is already assigned"); }
        this.getStats().addBaseStats(stat, value);
    }
    public void clearBaseStat(Stat stat)            { this.getStats().removeBase(stat);             }
    public void resetBaseStats() {for (Stat s : Stat.values()) { clearBaseStat(s); } }
    // ---------------- toString / utility ----------------

    @Override
    public String toString() {
        String raceName = (race == null) ? "None" : race.getDisplayName();
        String genderName = (gender == null) ? "Unknown" : gender.getDisplayName();
        return "Character: "    +
                "\n\tName:\t"   + name                      +
                "\n\tAge:\t"    + age                       +
                "\n\tGender:\t" + genderName                +
                "\n\tLevel:\t"  + level                     +
                "\n\tRace:\t"   + raceName                  +
                "\n\t"          + stats.toString();
    }

    // Optionally add getters for appliedFeatures map as unmodifiable view
    public List<RacialFeature> getAppliedFeatures() {
        return Collections.unmodifiableList(appliedFeatures);
    }

    // Optionally expose appliedChoicesCount copy for read-only inspection
    public Map<String, Integer> getAppliedChoicesCount() {
        return Collections.unmodifiableMap(appliedChoicesCount);
    }
}