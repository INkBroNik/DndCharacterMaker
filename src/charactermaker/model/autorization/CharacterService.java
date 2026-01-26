package charactermaker.model.autorization;

import charactermaker.model.dataHolders.CharacterHolder;
import charactermaker.model.dataHolders.DataBase;
import charactermaker.model.dataHolders.DataStore;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CharacterService {
    private final DataStore dataStore;

    public CharacterService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public List<CharacterHolder> getAllCharacters() { return dataStore.getDatabase().characters; }

    public CharacterHolder addCharacter(String owner) {
        DataBase dataBase = dataStore.getDatabase();
        long id = dataBase.characters.stream().mapToLong(c -> c.getId()).max().orElse(0L) + 1;
        CharacterHolder character = new CharacterHolder();
        character.setId(id);
        character.setOwner(owner);
        dataBase.characters.add(character);
        try{ dataStore.save(); } catch (IOException e) { e.printStackTrace(); }
        return character;
    }

    public boolean updateCharacter(CharacterHolder updatedCharacter) {
        DataBase dataBase = dataStore.getDatabase();
        for (int i = 0; i < dataBase.characters.size(); i++) {
            if (dataBase.characters.get(i).getId() == updatedCharacter.getId()) {
                dataBase.characters.set(i, updatedCharacter);
                try{ dataStore.save(); } catch (IOException e) { e.printStackTrace(); }
                return true;
            }
        }
        return false;
    }

    public boolean deleteCharacter(long id) {
    DataBase dataBase = dataStore.getDatabase();
    boolean removed = dataBase.characters.removeIf(c -> c.getId() == id);
        if (removed) { try { dataStore.save(); } catch (IOException e) { e.printStackTrace(); } }
        return removed;
    }

    public List<CharacterHolder> getCharactersFor(String owner) {
        List<CharacterHolder> out = new ArrayList<>();
        for (CharacterHolder character : dataStore.getDatabase().characters) {
            if  (character.getOwner().equals(owner)) { out.add(character); }
        }
        return out;
    }

    public List<CharacterHolder> getOrphans() {
        List<CharacterHolder> out = new ArrayList<>();
        for (CharacterHolder character : dataStore.getDatabase().characters) {
            if(
               character.getOwner() == null             ||
               character.getOwner().trim().isEmpty()    ||
               "unassigned".equals(character.getOwner())
            ) out.add(character);
        }
        return out;
    }

    public int purgeOrphans() {
        int removed = 0;
        synchronized (dataStore) {
            DataBase dataBase = dataStore.getDatabase();
            int before = dataBase.characters.size();
            dataBase.characters.removeIf(c -> c.getOwner().trim().isEmpty()      ||
                                                             c.getOwner() == null               ||
                                                            "unassigned".equals(c.getOwner())
            );
            removed = before - dataBase.characters.size();
            try {dataStore.save(); } catch (IOException e) { e.printStackTrace(); }
        }
        auditLog("Purge orphans");
        return removed;
    }

    public int assignOrphansTo(String username) {
        int count = 0;
        synchronized (dataStore) {
            DataBase dataBase = dataStore.getDatabase();
            for (CharacterHolder character : dataBase.characters) {
                if (character.getOwner() ==  null ||
                    character.getOwner().trim().isEmpty() ||
                    "unassigned".equals(character.getOwner())
                ){
                    String old = character.getOwner();
                    character.setOwner(username);
                    if (character.getHistory() == null) character.setHistory(new ArrayList<>());
                    character.getHistory().add(new OwnerChange(old, username, LocalDateTime.now(), "bulk assign"));
                    count++;
                }
            }
            try { dataStore.save(); } catch (IOException e) { e.printStackTrace(); }
        }
        auditLog(String.format("Assigned %d orphans to %s", count, username));
        return count;
    }

    public int claimOrphan(long id, String owner) {
        synchronized (dataStore){
            DataBase dataBase = dataStore.getDatabase();
            for (CharacterHolder character : dataBase.characters) {
                if (character.getId() == id) {
                    if (
                        character.getOwner() != null            &&
                        !character.getOwner().trim().isEmpty()  &&
                        !"unassigned".equals(character.getOwner())
                    ) return 1;
                    String old =  character.getOwner();
                    character.setOwner(owner);
                    if(character.getHistory() == null) character.setHistory(new ArrayList<>());
                    character.getHistory().add(new OwnerChange(old, owner, LocalDateTime.now(), "claimed via UI"));
                    try{ dataStore.save(); } catch (IOException e) { e.printStackTrace(); }
                    auditLog(String.format("User %s claimed char %d (oldOwner=%s)", owner, id, old));
                    return 0;
                }
            }
        }
        return -1;
    }

    private void auditLog(String message) {
        try {
            Path audit = Paths.get("audit.log");
            String entry = LocalDateTime.now() + " | " + message + System.lineSeparator();
            Files.write(
                    audit, entry.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) { e.printStackTrace(); }
    }
}
