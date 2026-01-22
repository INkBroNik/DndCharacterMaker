package charactermaker;

import charactermaker.jforms.SetWindow;
import javax.swing.JOptionPane;

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
    "Create a Character", "EXIT"
    };
    
    /**
     * Main method for the project
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            showMessage("Hello");
            String userChose = getChose("What you want to do?");
            if (userChose.equalsIgnoreCase(USER_CHOSE[0])) new SetWindow();
            if (userChose.equalsIgnoreCase(USER_CHOSE[1])) System.exit(0);
        });
    }
    
    /**
     * Show the JOptionPane
     * @param text 
     */
    private static void showMessage(String text) {
        JOptionPane.showMessageDialog(null, text, TITLE, JOptionPane.PLAIN_MESSAGE);
    }
    
    /**
     * Get a chose from user
     * @return chose in String
     */
    private static String getChose(String text) {
        Object chose = JOptionPane.showInputDialog
        (null, text, TITLE, JOptionPane.PLAIN_MESSAGE,
        null, USER_CHOSE, USER_CHOSE[0]);
        return chose.toString();
    }
}