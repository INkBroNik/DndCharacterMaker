package charactermaker;

import charactermaker.jforms.Set;
import javax.swing.JOptionPane;


/**
 * CharacterMaker - Final project for CS40S.
 * Maker of characters on D&D base system.
 * Can create, edit, and save Characters.
 * For now, it's very simple(Only Stats, Race, Name, Age, Level).
 * 
 * @author Nikita Padalka
 * @since 26 Oct 2025
*/
public class CharacterMaker
{
    private static final String TITLE = "CharacterMaker";
    private static final String[] USER_CHOSE = {
    "Create a Character", "Generate random Character", "EXIT"
    };
    
    /**
     * Main method for the project
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            showMessage("Hello");
            String userChose = getChose();
            if (userChose.equalsIgnoreCase(USER_CHOSE[0])) new Set();
            if (userChose.equalsIgnoreCase(USER_CHOSE[2])) System.exit(0);
        });
    }
    
    /**
     * Show the JOptionPane
     * @param text 
     */
    private static void showMessage(String text) {
        JOptionPane.showMessageDialog
        (null, text, TITLE, JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Get a chose from user
     * @return chose in String
     */
    private static String getChose() {
        Object chose = JOptionPane.showInputDialog
        (null, "", TITLE, JOptionPane.PLAIN_MESSAGE,
        null, USER_CHOSE, USER_CHOSE[0]);
        return chose.toString();
    }
}