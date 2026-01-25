package charactermaker.model.autorization;

import charactermaker.model.dataHolders.DataBase;
import charactermaker.model.dataHolders.DataStore;

import java.util.Base64;
import java.util.Optional;

public class UserSevice {
    private final DataStore store;

    public UserSevice(DataStore store) {
        this.store = store;
    }

    public boolean register(String username, String password) {
        if (username == null || password == null || password.isBlank() || username.isBlank()) return false;
        DataBase dataBase = store.getDatabase();
        if (dataBase.users.stream().anyMatch(user -> user.getUserName().equals(username))) return false;
        try {
            byte[] salt = Security.generateSalt();
            String hash = Security.hashSHA256(password, salt);
            dataBase.users.add(new User(username, hash, Base64.getEncoder().encodeToString(salt)));
            store.save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean autheticate(String username, String password){
        try {
            Optional<User> optionalUser = store.getDatabase().users.stream()
                                                .filter(user -> user.getUserName().equals(username)).findFirst();
            if (optionalUser.isEmpty()) return false;
            User user = optionalUser.get();
            byte[] salt = Base64.getDecoder().decode(user.getSalt());
            String hash = Security.hashSHA256(password, salt);
            return user.getPasswordHash().equals(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
