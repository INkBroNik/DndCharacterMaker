package charactermaker.model.dataHolders;

import charactermaker.enums.Sex;
import charactermaker.enums.Race;
import charactermaker.enums.Stat;
import charactermaker.model.UIN.Choice;
import charactermaker.model.features.RacialFeature;
import charactermaker.model.UIN.StatAllocation;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link CharacterHolder} == Class that holds the data for Character
 *
 * @author Nikita Padalka
 * @since 26/01/2026
 */
public class CharacterHolder {
    private String name;
    private int age;
    private int level;
    private final Stats stats = new Stats();
    private Race race;
    private Sex sex;

    private final List<RacialFeature> appliedFeatures = new ArrayList<>();
    private final List<Choice> pendingChoices = new ArrayList<>();
    private final Map<String, Integer> appliedChoicesCount = new HashMap<>();
    private final List<Choice> appliedChoices = new ArrayList<>();

    private static final Logger LOGGER = Logger.getLogger(CharacterHolder.class.getName());
    //============================================Basic getters/setters===============================================//

    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    public int getAge()                     { return age; }
    public void setAge(int age)             { this.age = age; }

    public int getLevel()                   { return level; }
    public void setLevel(int level)         { this.level = level; }

    public Stats getStats()                 { return stats; }

    public Sex getGender()                  { return sex; }
    public void setGender(Sex sex)          { this.sex = sex; }

    public Race getRace()                   { return race; }

    public List<RacialFeature> getAppliedFeatures() { return Collections.unmodifiableList(appliedFeatures); }
    public Map<String, Integer> getAppliedChoicesCount() { return Collections.unmodifiableMap(appliedChoicesCount); }
    //==============================================Race application==================================================//

    /**
     * Apply new race and clean the old info about old race.
     * @param newRace - New race
     */
    public void applyRace(Race newRace) {
        if (this.race != null) {
            String oldRaceId = this.race.getId();

            removeChoicesByGroupPrefix(oldRaceId);

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

    //=============================================Pending choices API================================================//

    /**
     * Add a pending choice. Prevents nulls and duplicates by sourceId+name.
     * @param choice - Pending choice
     */
    public void addPendingChoice(Choice choice) {
        Objects.requireNonNull(choice, "choice == null");
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
     * @param choice - Choice to cancel
     */
    public boolean cancelPendingChoice(Choice choice) {
        return pendingChoices.remove(choice);
    }

    /**
     * Resolve (apply) the choice using model validation and counters.
     * This method will throw IllegalStateException if limit exceeded.
     * @param choice - choice
     */
    public void resolveChoice(Choice choice) {
        Objects.requireNonNull(choice, "choice == null");
        applyChoice(choice, choice.getMaxSelections());
    }

    /**
     * Remove pending choice by the GroupId exact
     * @param groupId - Group ID to remove
     */
    void removePendingChoiceByGroupExact(String groupId) {
        Objects.requireNonNull(groupId, "groupId == null");
        pendingChoices.removeIf(c -> groupId.equals(c.getGroupId()));
    }

    /**
     * Remove pending choice by the group prefix id
     * @param groupPrefix - Group prefix id to remove
     */
    public void removePendingChoicesByGroupPrefix(String groupPrefix) {
        Objects.requireNonNull(groupPrefix, "groupPrefix == null");
        pendingChoices.removeIf( c -> {
            String gid = c.getGroupId();
            return gid.equals(groupPrefix) || gid.startsWith(groupPrefix + ":");
        });
    }
    //========================================Choice counters & application===========================================//

    /**
     * Check ability to apply choice by sourceId and max allowed.
     * @param sourceId - Source Id
     * @param max - Number of max selections
     */
    public boolean canApplyChoice(String sourceId, int max) {
        return appliedChoicesCount.getOrDefault(sourceId, 0) < max;
    }

    /**
     * Apply the choice to the character.
     * This method updates counters and removes the choice from pending list.
     * @param choice - Choice to apply
     * @param max - Number of max selections
     */
    public void applyChoice(Choice choice, int max) {
        Objects.requireNonNull(choice, "choice == null");
        String gid = choice.getGroupId();
        String cid = choice.getChoiceId();
        if (appliedChoices.stream().anyMatch(c -> c.getChoiceId().equals(cid))) {
            throw new IllegalStateException("Choice already applied: " + cid);
        }

        int used = appliedChoicesCount.getOrDefault(gid, 0);
        if (used >= max) { throw new IllegalStateException("Too many choices for group " + gid); }

        choice.apply(this);

        appliedChoices.add(choice);
        appliedChoicesCount.put(gid, used + 1);
        pendingChoices.removeIf(p -> p.getChoiceId().equals(cid));
    }

    /**
     * Remove choice by group prefix.
     * Clean up the pending and applied choices
     * @param groupPrefix - Group Id Prefix
     */
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

        pendingChoices.removeIf(c -> {
            String gid = c.getGroupId();
            return gid.equals(groupPrefix) || gid.startsWith(groupPrefix + ":");
        });

        appliedChoicesCount.keySet().removeIf(k -> k.equals(groupPrefix) || k.startsWith(groupPrefix + ":"));
    }

    //====================================================Stats=======================================================//

    /**
     * Accessor to base part of Stats that do not assign yet
     * @return -Unassigned Stats
     */
    public List<Stat> getUnassignedStats() {
        List<Stat> result = new ArrayList<>();
        for (Stat s : Stat.values()) {
            Integer v = this.getStats().getBase(s);
            if (v == null) result.add(s);
        }
        return result;
    }

    /**
     * Check for the base part of Stat is assigned
     * @param stat - Stat which base to check
     * @return - boolean
     */
    public boolean isBaseAssigned(Stat stat)        { return this.getStats().getBase(stat) != null; }

    /**
     * Set the base Stats
     * @param stat - Stat which base need set
     * @param value - number of base stat
     */
    public void setBaseStat(Stat stat, int value)   {
        if( isBaseAssigned(stat) ) { throw new IllegalStateException(stat + " is already assigned"); }
        this.getStats().setBase(stat, value);
    }

    /**
     * Clear the base part of Stat
     * @param stat - Stat which base need to clear
     */
    public void clearBaseStat(Stat stat)            { this.getStats().removeBase(stat);             }

    /**
     * Clear all base parts of Stats
     */
    public void resetBaseStats() {for (Stat s : Stat.values()) { clearBaseStat(s); } }

    //=====================================================Allocation=================================================//
    public void applyAllocation(StatAllocation allocation, boolean force) {
        Objects.requireNonNull(allocation, "allocation == null");
        allocation.validateComplete();

        boolean anyAssigned = false;
        for (Stat s : Stat.values()) { if (this.isBaseAssigned(s)) { anyAssigned = true; break; } }
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
                try { this.setBaseStat(stat, value); } catch (RuntimeException ex) { throw ex; }
            }
        } catch (RuntimeException ex) {
            // rollback to old
            for (Stat s : Stat.values()) {
                Integer v = old.get(s);
                if (v == null) { this.getStats().removeBase(s); } else { this.getStats().setBase(s, v); }
            }
            throw ex;
        }
    }

    //===================================================toString / utility===========================================//

    /**
     * Override ToString method
     * @return - All info about character
     */
    @Override
    public String toString() {
        String raceName = (race == null) ? "None" : race.getDisplayName();
        String genderName = (sex == null) ? "Unknown" : sex.getDisplayName();
        return "Character: "    +
                "\n\tName:\t"   + name                      +
                "\n\tAge:\t"    + age                       +
                "\n\tSex:\t"    + genderName                +
                "\n\tLevel:\t"  + level                     +
                "\n\tRace:\t"   + raceName                  +
                "\n"            + stats;
    }
}