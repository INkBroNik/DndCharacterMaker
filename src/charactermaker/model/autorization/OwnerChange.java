package charactermaker.model.autorization;

import java.time.LocalDateTime;

public class OwnerChange {
    public String oldOwner;
    public String newOwner;
    public LocalDateTime when;
    public String reason; // optional
    public OwnerChange() {}
    public OwnerChange(String oldOwner, String newOwner, LocalDateTime when, String reason) {
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
        this.when = when;
        this.reason = reason;
    }
}
