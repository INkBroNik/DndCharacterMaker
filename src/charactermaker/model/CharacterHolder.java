package charactermaker.model;

import charactermaker.enums.Gender;
import charactermaker.enums.Race;
import charactermaker.enums.Stat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger LOGGER = Logger.getLogger(CharacterHolder.class.getName());
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
            removeChoicesByGroupPrefix(oldRaceId);

            // 1.2 Убираем racial features
            for (RacialFeature feature : appliedFeatures) {
                try {
                    feature.remove(this);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Could not remove feature " + feature.getName(), e);
                }
            }

            appliedFeatures.clear();
        }

        if (newRace == null) {
            this.race = null;
            return;
        }

        // 2. Применяем новую расу
        List<RacialFeature> appliedNow =  new ArrayList<>();
        try{
            for (RacialFeature feature : newRace.getFeatures()) {
                feature.apply(this);
                appliedNow.add(feature);
            }
        } catch (RuntimeException e){
            for (int i = appliedNow.size(); i >= 0; i--) {
                try { appliedNow.get(i).remove(this); } catch (Exception ignored){
                    LOGGER.log(Level.WARNING, "Could not remove feature " + appliedNow.get(i).getName(), ignored);
                }
            }
            throw e;
        }
        this.race = newRace;
        appliedFeatures.addAll(appliedNow);
    }

    // ---------------- pending choices API ----------------

    /**
     * Add a pending choice. Prevents nulls and duplicates by sourceId+name.
     */
    public void addPendingChoice(Choice choice) {
        Objects.requireNonNull(choice, "choice == null");
        // avoid duplicate exact same choice object by choiceid
        boolean exists = pendingChoices.stream().anyMatch(
                c -> Objects.equals(c.getChoiceId(), choice.getChoiceId()));
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
    public void resolveChoice(Choice choice) {
        Objects.requireNonNull(choice, "choice == null");
        applyChoice(choice, choice.getMaxSelections());
    }

    void removePendingChoiceByGroupExact(String groupId) {
        Objects.requireNonNull(groupId, "groupId == null");
        pendingChoices.removeIf(c -> groupId.equals(c.getGroupId()));
    }

    public void removePendingChoicesByGroupPrefix(String groupPrefix) {
        Objects.requireNonNull(groupPrefix, "groupPrefix == null");
        pendingChoices.removeIf( c -> {
            String gid = c.getGroupId();
            return gid.equals(groupPrefix) || gid.startsWith(groupPrefix + ":");
        });
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
        Objects.requireNonNull(choice, "choice == null");
        String gid = choice.getGroupId();
        String cid = choice.getChoiceId();
        // защита: не применять один и тот же choice дважды
        if (appliedChoices.stream().anyMatch(c -> c.getChoiceId().equals(cid))) {
            throw new IllegalStateException("Choice already applied: " + cid);
        }

        // проверка лимита группы
        int used = appliedChoicesCount.getOrDefault(gid, 0);
        if (used >= max) {
            throw new IllegalStateException("Too many choices for group " + gid);
        }

        // apply — здесь apply может бросить (например, setBaseStat итд.)
        choice.apply(this);

        // если всё успешно — фиксируем
        appliedChoices.add(choice);
        appliedChoicesCount.put(gid, used + 1);
        pendingChoices.removeIf(p -> p.getChoiceId().equals(cid));
    }

    public void removeChoicesByGroupPrefix(String groupPrefix) {
        Objects.requireNonNull(groupPrefix, "groupPrefix == null");
        if(appliedChoices != null){
            Iterator<Choice> it = appliedChoices.iterator();
            while (it.hasNext()) {
                Choice choice = it.next();
                String gid = choice.getGroupId();
                if(gid.equals(groupPrefix) || gid.startsWith(groupPrefix + ":")){
                    try { choice.remove(this); } catch (Exception e) { e.printStackTrace(); }
                    it.remove();
                }
            }
        }

        // 2) Удалить pending choices из модели
        pendingChoices.removeIf(c -> {
            String gid = c.getGroupId();
            return gid.equals(groupPrefix) || gid.startsWith(groupPrefix + ":");
        });

        // 3) Удалить счётчики применённых по группе
        appliedChoicesCount.keySet().removeIf(k -> k.equals(groupPrefix) || k.startsWith(groupPrefix + ":"));
    }
    // ------------------------------------------- Stats ------------------------------------------------------------

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
        if( isBaseAssigned(stat) ) { throw new IllegalStateException(stat + " is already assigned"); }
        this.getStats().setBase(stat, value);
    }
    public void clearBaseStat(Stat stat)            { this.getStats().removeBase(stat);             }
    public void resetBaseStats() {for (Stat s : Stat.values()) { clearBaseStat(s); } }

    //--------------------------------Allocation----------------------------

    public void applyAllocation(StatAllocation allocation, boolean force) {
        Objects.requireNonNull(allocation, "allocation == null");
        allocation.validateComplete();

        boolean anyAssigned = false;
        for (Stat s : Stat.values()) {
            if (this.isBaseAssigned(s)) { anyAssigned = true; break; }
        }
        if (anyAssigned && !force) {
            throw new IllegalStateException("Some base stats already assigned; use force=true to overwrite");
        }

        EnumMap<Stat, Integer> old = new EnumMap<>(Stat.class);
        try{
            for(Stat s : Stat.values()){
                Integer prev = this.getStats().getBase(s);
                old.put(s, prev);
            }
            // set new ones
            for (Map.Entry<Stat, Integer> e : allocation.asMap().entrySet()) {
                Stat stat = e.getKey();
                Integer value = e.getValue();
                // use CharacterHolder.setBaseStat if you have higher-level checks; fallback to Stats.setBase
                try {
                    this.setBaseStat(stat, value); // recommended method on CharacterHolder
                } catch (RuntimeException ex) {
                    // if setBaseStat throws (e.g., assigned and no force) rethrow and rollback below
                    throw ex;
                }
            }
        } catch (RuntimeException ex) {
            // rollback to old
            for (Stat s : Stat.values()) {
                Integer v = old.get(s);
                if (v == null) {
                    this.getStats().removeBase(s); // clear
                } else {
                    this.getStats().setBase(s, v);
                }
            }
            throw ex;
        }
    }

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
                "\t"          + stats.toString();
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