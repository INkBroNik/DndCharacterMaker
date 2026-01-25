package charactermaker;

import charactermaker.jforms.CreateAndShowWindow;
import charactermaker.jforms.SetWindow;
import charactermaker.model.autorization.CharacterService;
import charactermaker.model.autorization.UserSevice;
import charactermaker.model.dataHolders.CharacterHolder;
import charactermaker.model.dataHolders.DataBase;
import charactermaker.model.dataHolders.DataStore;

import javax.swing.JOptionPane;
import java.util.concurrent.CountDownLatch;

/**
 * CharacterMaker - Final project for CS40S.
 * Maker of characters on D&D base system.
 * Can create, edit(Not now), and save Characters(Not now).
 * For now, it's very simple(Only Stats, Race, Name, Age, Level).
 * 
 * @author Nikita Padalka
 * @since 26 Oct 2025
*/
public class CharacterMaker
{
    private static final String TITLE = "CharacterMaker";
    private static final String[] USER_CHOSE = {
    "Create a Character", "View the Characters", "EXIT"
    };
    
    /**
     * Main method for the project
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CharacterHolder character = new CharacterHolder();
        CountDownLatch latch = new CountDownLatch(1);
        javax.swing.SwingUtilities.invokeLater(() -> {
            showMessage("Hello");
            try{
                DataStore dataStore = new DataStore("data.json");
                UserSevice userSevice = new UserSevice(dataStore);
                CharacterService characterService = new CharacterService(dataStore);
                new CreateAndShowWindow(userSevice, characterService, latch);
            } catch (Exception e) { e.printStackTrace(); }
        });
        try {
            latch.await();
        } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println("huy");
    }
    
    /**
     * Show the JOptionPane
     * @param text 
     */
    private static void showMessage(String text) {
        JOptionPane.showMessageDialog(null, text, TITLE, JOptionPane.PLAIN_MESSAGE);
    }
}