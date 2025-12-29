package charactermaker;

import charactermaker.jforms.Set;
import javax.swing.JOptionPane;


/*
 * CharacterMaker - description
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
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        showMessege("Hello");
        String userChose = getChose();
        if (userChose.equalsIgnoreCase(USER_CHOSE[0])) new Set();
        if (userChose.equalsIgnoreCase(USER_CHOSE[2])) System.exit(0);
    }
    
    /**
     * Show the JOptionPane
     * @param text 
     */
    private static void showMessege(String text) {
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

    
    
//          charParam
//        this.setVisible(true);
//        this.setMinimumSize(new Dimension(400, 400));
//        this.setTitle(TITLE);
//        DefaultListModel<String> speciesModel = new DefaultListModel<>();
//        for (String item : DNDDataHolder.SPECIESSES) 
//        {speciesModel.addElement(item);}
//        spiecieList.setModel(speciesModel);
//        DefaultListModel<String> classModel = new DefaultListModel<>();
//        for (String item : DNDDataHolder.CLASSES) 
//        {classModel.addElement(item);}
//        classesList.setModel(classModel);
    
//          setParam    
//       
//    private final String TITLE = "Set of Paramaters";
//    private CharacterHolder character;
//    /**
//     * Creates new form ChoiseSetParameters
//     */
//    public SetParameters() {
//        initComponents();
//        setUp();
//    }
//
//    public SetParameters(CharacterHolder character) {
//        initComponents();
//        setUp();
//        this.character = character;
//        CardLayout card = (CardLayout) jPanel1.getLayout();
//        
//        jComboBox1.addActionListener((e) -> {
//            switch(jComboBox1.getSelectedItem().toString()){
//                case "RANDOM" -> card.show(jPanel1, "RANDOM");
//                case "POINT-BUY" -> card.show(jPanel1, "POINTBUY");
//            }
//        });
//        
//        
//        
//    }
    
//    
//    private void setUp() {
//        this.setVisible(true);
//        this.setMinimumSize(new Dimension(400, 400));
//        this.setTitle(TITLE);
//
//        JSpinner[] spinners = {
//            jSpinner1, jSpinner2, jSpinner3,jSpinner4,jSpinner5,jSpinner6
//        };
//        jSpinner1.addChangeListener(e -> updateValue(spinners));
//        jSpinner2.addChangeListener(e -> updateValue(spinners));
//        jSpinner3.addChangeListener(e -> updateValue(spinners));
//        jSpinner4.addChangeListener(e -> updateValue(spinners));
//        jSpinner5.addChangeListener(e -> updateValue(spinners));
//        jSpinner6.addChangeListener(e -> updateValue(spinners));
//    }
//    
//    private void updateValue(JSpinner[] sp){
//         int number = 0;
//        for (JSpinner s : sp) {
//            if(costFor((int) s.getValue()) == 2){
//                number += (int) s.getValue() + 1;
//            }
//            else if(costFor((int) s.getValue()) == 3){
//                number += (int) s.getValue() + 2;
//            }
//            else number += (int) s.getValue();
//        }
//    jLabel1.setText(String.valueOf(27 - number));
//    }
//    
//    private int costFor(int score) {
//        if (score <= 13) return 1;
//        else if (score <= 14) return 2;
//        else return 3;
//    }
    
}