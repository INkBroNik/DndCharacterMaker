package charactermaker.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum NameCulture {
    DRAGONBORN  (
                new String[] {  "Arjhan", "Balasar", "Donaar", "Ghesh", "Kriv",
                                "Medrash", "Mehen", "Nadarr", "Pandjed", "Torinn"       },
                new String[] {  "Akra", "Biri", "Daar", "Farideh", "Harann",
                                "Havilar", "Jheri", "Kava", "Korinn", "Mishann"         }
                ),
    DWARF       (
                new String[] {  "Thorin", "Borin", "Dain", "Gimli", "Rurik",
                                "Baldric", "Kazrik", "Orsik", "Vondal", "Harbek"        },
                new String[] {  "Dis", "Hilda", "Brunna", "Eldeth", "Vistra",
                                "Gunnloda", "Sannl", "Riswynn", "Torbera", "Audhild"    }
                ),
    ELF         (
                new String[] {  "Aelar", "Thalion", "Erevan", "Faelar", "Laucian",
                                "Soveliss", "Theren", "Arannis", "Mythrandir", "Calen"  },
                new String[] {  "Lia", "Sylva", "Naivara", "Aeris", "Elora",
                                "Keyleth", "Shalana", "Ilyana", "Sariel", "Yllairies"   }
                ),
    GNOME       (
                new String[] {  "Alston", "Boddynock", "Dimble", "Fonkin", "Gimble",
                                "Gruk", "Jebeddo", "Namfoodle", "Roondar", "Warryn"     },
                new String[] {  "Bimpnottin", "Breena", "Caramip", "Ellywick", "Loopmottin",
                                "Mardnab", "Nissa", "Roywyn", "Randa", "Zanna"          }
                ),
    HALF_ELF    (
                new String[] {  "Aelar", "Lucan", "Theren", "Marcus", "Erevan",
                                "Victor", "Faelar", "Julian", "Arannis", "Leon"         },
                new String[] {  "Lia", "Sophia", "Elora", "Anna", "Naivara",
                                "Isabella", "Sariel", "Clara", "Yllairies", "Lucia"     }
                ),
    HALFLING    (
                new String[] {  "Alton", "Milo", "Perrin", "Roric", "Theo",
                                "Finnan", "Lyle", "Tobin", "Wendel", "Ander"            },
                new String[] {  "Bree", "Callie", "Cora", "Jillian", "Lavinia",
                                "Merla", "Nedda", "Paela", "Seraphina", "Verna"         }
                ),
    HALF_ORK    (
                new String[] {  "Grom", "Thok", "Urzog", "Brakka", "Karg",
                                "Morg", "Durog", "Ragash", "Hruk", "Zogar"              },
                new String[] {  "Baggi", "Emen", "Engong", "Myev", "Ovak",
                                "Shautha", "Sutha", "Vola", "Yeskarra", "Zagga"         }
                ),
    HUMAN       (
                new String[] {  "John", "Arthur", "William", "Robert", "Thomas",
                                "Edward", "Henry", "Marcus", "Lucan", "Victor"          },
                new String[] {  "Anna", "Elizabeth", "Mary", "Catherine", "Helen",
                                "Isabella", "Margaret", "Lucia", "Sophia", "Clara"      }
                ),
    ORK         (
                new String[] {  "Grom", "Thok", "Urzog", "Brakka", "Karg",
                                "Morg", "Durog", "Ragash", "Hruk", "Zogar"              },
                new String[] {  "Baggi", "Emen", "Engong", "Myev", "Ovak",
                                "Shautha", "Sutha", "Vola", "Yeskarra", "Zagga"         }
                ),
    TIEFLING    (
                new String[] {  "Akmenos", "Barakas", "Damakos", "Ekemon", "Iados",
                                "Kairon", "Leucis", "Mordai", "Rimmon", "Zherak"        },
                new String[] {  "Agnieszka", "Anakis", "Bryseis", "Criella", "Damaia",
                                "Kallista", "Lerissa", "Makaria", "Orianna", "Phelaia"  }
                );

    private final String[] nameMale;
    private final String[] nameFemale;
    private final String[] allNames;

    NameCulture(String[] nameMale, String[] nameFemale){
        this.nameMale = nameMale;
        this.nameFemale = nameFemale;
        this.allNames = merge(nameMale, nameFemale);
    }
    public String randomName(Gender gender) {
        return switch (gender) {
            case MALE -> randomFrom(nameMale);
            case FEMALE -> randomFrom(nameFemale);
            case UNKNOWN -> randomFrom(allNames);
        };
    }
    private String randomFrom(String[] pool) { return pool[ThreadLocalRandom.current().nextInt(pool.length)]; }
    private static String[] merge(String[] a, String[] b) {
        String[] result = new String[a.length + b.length];
        System.arraycopy(a,0, result, 0, a.length);
        System.arraycopy(b,0, result, a.length, b.length);
        return result;
    }
}
