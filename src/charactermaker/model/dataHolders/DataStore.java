package charactermaker.model.dataHolders;

import charactermaker.model.autorization.OwnerChange;
import charactermaker.model.features.RacialFeature;
import charactermaker.model.features.RacialFeatureAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DataStore {
    private final Path path;
    private final Gson gson;
    private DataBase database;
    private final Object fileLock = new Object();

    public DataStore(String path) throws IOException {
        this.path = Paths.get(path);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class, 
                        (JsonSerializer<LocalDateTime>) 
                                (src, typeOfSrc, context) -> 
                                        new JsonPrimitive(src.toString())
                )
                .registerTypeAdapter(LocalDateTime.class, 
                        (JsonDeserializer<LocalDateTime>) 
                                (json, typeOfT, context) -> 
                                        LocalDateTime.parse(json.getAsString())
                )
                .registerTypeAdapter(RacialFeature.class, new RacialFeatureAdapter())
                .setPrettyPrinting()
                .create();
        load();
    }

    private void load() throws IOException {
        if (Files.exists(path)) {
            try (Reader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                Type t = new TypeToken<DataBase>(){}.getType();
                database = gson.fromJson(r, t);
                if (database == null) database = new DataBase();
            } catch (IOException e) {
                e.printStackTrace();
                database = new DataBase();
            }
        } else {
            database = new DataBase();
        }
    }


    public DataBase getDatabase() { return database; }


    public void save() throws IOException {
        synchronized (fileLock) {
            Path tmp = Paths.get(path.toString() + ".tmp");
            try (BufferedWriter writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                gson.toJson(database, writer);
            }
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        }
    }

    // В DataStore.java (или где у тебя load())
    private void normalizeOrphansOnLoad() {
        boolean changed = false;
        for (CharacterHolder c : database.characters) {
            if (c.getOwner() == null || c.getOwner().trim().isEmpty()) {
                c.setOwner("unassigned");
                // добавим мета-поля, если их нет (см. модель ниже)
                if (c.getHistory() == null) c.setHistory(new ArrayList<>());
                c.getHistory().add(new OwnerChange("system", "unassigned", LocalDateTime.now(), "normalized on load"));
                changed = true;
            }
        }
        if (changed) {
            try {
                // делаем бэкап перед сохранением изменений
                Path backup = Paths.get(path.toString() + ".bak");
                Files.copy(path, backup, StandardCopyOption.REPLACE_EXISTING);
                save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}