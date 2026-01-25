package charactermaker.model.autorization;

import charactermaker.model.dataHolders.CharacterHolder;
import charactermaker.model.dataHolders.DataBase;
import charactermaker.model.dataHolders.DataStore;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharacterService {
    private final DataStore dataStore;

    public CharacterService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

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
}
