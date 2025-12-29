package charactermaker.jforms;

import charactermaker.enums.Race;
import charactermaker.model.Stats;
import javax.swing.*;
import java.awt.*;
 
/**
 * Set.java - description
 *
 * @author YOUR NAME
 * @since 13 Dec 2025, 7:52:38 pm
 */
public class Set extends JFrame
{
    private final int TEXT_FIELD_WEIDTH = 100;
    private final int TEXT_FIELD_HIEGHT = 50;
    /**
     * Default constructor, set class properties
     */
    public Set() {
        set(); 
        Stats stats = new Stats();
        SetButton.addActionListener((e) -> {
            String value = (String)RACE.getSelectedItem();
            Race race = Race.findByName(value);
            if()
            stats.applyRacialBonuses(race);
            System.out.println(stats.toString());
        });
        
        mainItem.addActionListener(e -> cardLayout.show(cardsPanel, "MAIN"));
        paramItem.addActionListener(e -> cardLayout.show(cardsPanel, "PARAM"));

        setDefaultCloseOperation(Set.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setResizable(false);
        setVisible(true);
    }
    
    private void set(){      
        setLayout(new BorderLayout(10,10));
    
        
        add(button, BorderLayout.SOUTH);
        add(cardsPanel, BorderLayout.CENTER);  
        setJMenuBar(menuBar);
        
        main.setLayout(new GridLayout(3, 3, 5, 5));
        NAME.setPreferredSize(new Dimension(TEXT_FIELD_WEIDTH, TEXT_FIELD_HIEGHT));
        main.add(NAME);
        AGE.setPreferredSize(new Dimension(TEXT_FIELD_WEIDTH, TEXT_FIELD_HIEGHT));
        main.add(AGE);
        LEVEL.setPreferredSize(new Dimension(TEXT_FIELD_WEIDTH, TEXT_FIELD_HIEGHT));
        main.add(LEVEL);
        RACE.setPreferredSize(new Dimension(TEXT_FIELD_WEIDTH, TEXT_FIELD_HIEGHT));
        main.add(RACE);
        
        main.add(SetButton);

        fileMenu.add(mainItem);
        fileMenu.add(paramItem);
        menuBar.add(fileMenu);
        
        
        param.add(new JLabel("param"));
        cardsPanel.add(main, "MAIN");
        cardsPanel.add(param, "PARAM");
    }
    
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);
    
    private final JPanel main = new JPanel();
    private final JPanel param = new JPanel();
    
    private final JButton button = new JButton("HUY");
    private final JButton SetButton = new JButton("SET");
    
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fileMenu = new JMenu("STAGES");
    private final JMenuItem mainItem = new JMenuItem("MAIN");
    private final JMenuItem paramItem = new JMenuItem("PARAM");
    
    private final JTextField NAME = new JTextField();
    private final JTextField AGE = new JTextField();
    private final JTextField LEVEL = new JTextField();

    private final String[] raceArray = {
        Race.DRAGONBORN.getDisplayName(), Race.DWARF.getDisplayName(),
        Race.ELF.getDisplayName(), Race.GNOME.getDisplayName(),
        Race.HALF_ELF.getDisplayName(), Race.HALFLING.getDisplayName(),
        Race.HALF_ORK.getDisplayName(), Race.HUMAN.getDisplayName(),
        Race.ORC.getDisplayName(), Race.TIEFLING.getDisplayName()
    };
    private final JComboBox<String> RACE = new JComboBox<>(raceArray);
    
}