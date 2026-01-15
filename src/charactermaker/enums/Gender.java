package charactermaker.enums;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    UNKNOWN("Unknown");

    private final String displayName;

    Gender(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }

    public static Gender findByName(String name){
        for (Gender gender : Gender.values()){
            if (gender.getDisplayName().equals(name)){
                return gender;
            }
        }
        return null;
    }
}
