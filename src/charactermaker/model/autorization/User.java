package charactermaker.model.autorization;

public class User {
    private String userName;
    private String passwordHash;
    private String salt;

    public User(String userName, String passwordHash, String salt) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.salt = salt;
    }

    public String getUserName()     { return userName;      }
    public String getPasswordHash() { return passwordHash;  }
    public String getSalt()         { return salt;          }
}

