package charactermaker.jforms;

import charactermaker.enums.Gender;
import charactermaker.enums.Race;
import charactermaker.enums.Stat;
import charactermaker.model.*;
import charactermaker.model.Choice;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * SetWindow.java - Class for all UI
 *
 * @author Nikita Padalka
 * @since 13 Dec 2025, 7:52:38 pm
 */
public class SetWindow extends JFrame
{
    private final int WEIDTH;
    private final int HIGHT;
    private final Dimension dimension;
    private final GridLayout gridLayout;
    private final CharacterHolder character = new CharacterHolder();

    /**
     * Default constructor, set class properties
     */
    public SetWindow() {
        WEIDTH = 200;
        HIGHT = 50;
        dimension = new Dimension(WEIDTH, HIGHT);
        gridLayout = new GridLayout(3,3,5,5);
        set();
        SET_BUTTON.addActionListener((e) -> {
            Race race = Race.findByName((String) RACE.getSelectedItem());
            Gender gender = Gender.findByName((String) GENDER.getSelectedItem());
            String name = null;
            int age = 0;
            int level = 1;
            try {
                name = NAME.getText();
                age = Integer.parseInt(AGE.getText());

                character.applyRace(race); character.setName(name); character.setAge(age);
                character.setLevel(level); character.setGender(gender);
                if (!character.getPendingChoices().isEmpty()) {
                    showChoiceDialog(character, character.getPendingChoices(), 2, true);
                }

                System.out.println(character);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog
                        (this, "ERORR!\n Please reenter the fields!",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        RANDOM_NAME.addActionListener((e) -> {
            Race race = Race.findByName((String) RACE.getSelectedItem());
            Gender gender = Gender.findByName((String) GENDER.getSelectedItem());
            NAME.setText(race.randomName(gender));
        });

        ROLL.addActionListener( e -> {
            cleanTheBaseStats();
            StatAllocation allocation = new StatAllocation();
            StatGenerationRule rule = new DiceRollRule();
            List<Integer> rolledValues = rule.generate();
            showStatSelection(rolledValues, allocation, RANDOM_OUTPUT);
        });

        RANDOM_RESET.addActionListener(e -> {
            character.resetBaseStats();
            RANDOM_OUTPUT.setText("Base stats been re-set");
        });

        PRE_SET_SET.addActionListener(e -> {
            cleanTheBaseStats();
            StatAllocation allocation = new StatAllocation();
            StatGenerationRule rule = new StandardArrayRule();
            List<Integer> preSetValues = rule.generate();
            showStatSelection(preSetValues, allocation, PRE_SET_OUTPUT);
        });

        PRE_SET_RESET.addActionListener(e -> {
            character.resetBaseStats();
            PRE_SET_OUTPUT.setText("Base stats been re-set");
        });

        applyButton.addActionListener(e -> {
            cleanTheBaseStats();
            StatAllocation allocation = new StatAllocation();
            for(Map.Entry<Stat, JSpinner> entry : spinnerMap.entrySet()) {
                allocation.assign(entry.getKey(), (Integer) entry.getValue().getValue());
            }

            try{
                int cost = pointBuyRule.totalCost(allocation.asMap());
                if(cost > pointBuyRule.getBudget()){
                    JOptionPane.showMessageDialog(this,
                            "Too many points used: " + cost + " > " + pointBuyRule.getBudget(),
                            "Budget exceeded", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch(IllegalArgumentException ex){
                JOptionPane.showMessageDialog(this, "Invalid stat values: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try{
                character.applyAllocation(allocation, false);
            } catch(IllegalArgumentException ex){
                int res = JOptionPane.showConfirmDialog(this, "Some base stats already set. Overwrite?", "Confirm overwrite", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION){
                    try{
                        character.applyAllocation(allocation, true);
                    } catch (RuntimeException ex2) {
                        JOptionPane.showMessageDialog(this, "Failed to apply allocation: " + ex2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    return;
                }
            } catch(RuntimeException ex){
                JOptionPane.showMessageDialog(this, "Failed to apply allocation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Applied base stats.");
            System.out.println(character);
        });

        resetButton.addActionListener(e -> {
           for (JSpinner spinner : spinnerMap.values()) spinner.setValue(8);
           onSpinnerChanged(null);
        });

        BASE_ITEM.addActionListener(e -> CARD_LAYOUT.show(CARDS_PANEL, "BASE"));
        RANDOM_SUB_ITEM.addActionListener(e -> CARD_LAYOUT.show(CARDS_PANEL, "RANDOM"));
        PRE_SET_SUB_ITEM.addActionListener(e -> CARD_LAYOUT.show(CARDS_PANEL, "PRE-SET"));
        POINT_BUY_SUB_ITEM.addActionListener(e -> CARD_LAYOUT.show(CARDS_PANEL, "POINT-BUY"));

        setDefaultCloseOperation(SetWindow.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setResizable(false);
        setVisible(true);
    }

    /**
     * Show Choice dialog for user to choice the stats bonuses
     * @param character - Character that holds information about itself.
     * @param pendingChoices - List of pending choices to choose.
     * @param maxSelections - Max number of selection bonuses.
     * @param requireExact - check for submitting the exact max number of bonuses
     */
    private void showChoiceDialog
    (CharacterHolder character, List<Choice> pendingChoices, int maxSelections, boolean requireExact) {
        if (pendingChoices == null || pendingChoices.isEmpty()) return;

        // Диалог модальный
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                "Make your choices", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel panel = new JPanel(new BorderLayout(8,8));
        panel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JLabel info = new JLabel(requireExact
                ? "Select exactly " + maxSelections + " options:"
                : "Select up to " + maxSelections + " options:");
        panel.add(info, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        List<JCheckBox> boxes = new ArrayList<>();
        for (Choice c : pendingChoices) {
            JCheckBox cb = new JCheckBox(c.getName() + " — " + c.getDescription());
            boxes.add(cb);
            listPanel.add(cb);
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setPreferredSize(new Dimension(380, Math.min(200, pendingChoices.size() * 30)));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        ok.setEnabled(false); // по умолчанию

        bottom.add(cancel);
        bottom.add(ok);
        panel.add(bottom, BorderLayout.SOUTH);

        // Слушатель подсчёта выбранных чекбоксов
        Runnable updateOk = () -> {
            long count = boxes.stream().filter(AbstractButton::isSelected).count();
            if (requireExact) ok.setEnabled(count == maxSelections);
            else ok.setEnabled(count > 0 && count <= maxSelections);
        };

        // Когда состояние чекбокса меняется — обновляем OK
        for (JCheckBox b : boxes) {
            b.addItemListener(e -> {
                // update in EDT
                SwingUtilities.invokeLater(updateOk);
            });
        }

        ok.addActionListener(e -> {
            // Собираем выбранные Choice
            List<Choice> toApply = new ArrayList<>();
            for (int i = 0; i < boxes.size(); i++) {
                if (boxes.get(i).isSelected()) {
                    toApply.add(pendingChoices.get(i));
                }
            }

            // Применяем через модель, последовательно
            // character.applyChoice(...) бросит IllegalStateException если лимит исчерпан
            try {
                for (Choice choice : toApply) {
                    // Здесь мы передаём "maxSelections" как лимит для данной группы sourceId;
                    // модель сама проверит конкретную логику (и уменьшит счётчик).
                    character.applyChoice(choice, maxSelections);
                }
                dialog.dispose();
//                refreshStatsUI(); // обновляем UI после применения
            } catch (IllegalStateException ex) {
                // Если модель запретила — покажем и не закроем диалог
                JOptionPane.showMessageDialog
                        (dialog, ex.getMessage(), "Invalid choice", JOptionPane.WARNING_MESSAGE);
            }
        });

        cancel.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private Stat showStatSelectionDialog
            (Window parent, CharacterHolder character, StatAllocation allocation,int value){
        EnumSet<Stat> taken = EnumSet.noneOf(Stat.class);

        for (Stat s : Stat.values()) {
            if(character.isBaseAssigned(s)) taken.add(s);
        }
        taken.addAll(allocation.asMap().keySet());

        EnumSet<Stat> available = EnumSet.allOf(Stat.class);
        available.removeAll(taken);

        if(available.isEmpty()) return null;

        List<Stat> options = new ArrayList<>();
        for (Stat s : Stat.values()) { if(available.contains(s)) options.add(s); }

        if(options.isEmpty()) return null;

        String message = "Assign value " + value + " to which stat?";
        Stat[] arr = options.toArray(new Stat[0]);

        return (Stat) JOptionPane.showInputDialog(
                parent, message, "Assign base stat",
                JOptionPane.QUESTION_MESSAGE, null, arr, arr[0]
        );
    }

    private void showStatSelection(List<Integer> values, StatAllocation allocation, JTextField output){
        output.setText("Roll: " + values.toString());
        for (int value : values){
            while (true) {
                Stat chosen = showStatSelectionDialog(this, character, allocation ,value);
                if(chosen == null){
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Cancel stat distribution?\nAll assigned base stats will be cleared.",
                            "Confirm cancel",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        // ❗ СБРОС ВСЕХ base-статов
                        for (Stat s : Stat.values()) allocation.clear(s);
                        output.setText("Base stats been re-set");
                        return; // полностью выходим из распределения
                    } else {
                        // пользователь передумал — показываем диалог снова
                        continue;
                    }
                }
                try {
                    allocation.assign(chosen, value);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            }
        }
        try{
            character.applyAllocation(allocation, false);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to apply allocation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println(character.toString());
    }

    private void onSpinnerChanged(ChangeEvent e) {
        EnumMap<Stat, Integer> assigned = new  EnumMap<>(Stat.class);
        for(Map.Entry<Stat, JSpinner> entry : spinnerMap.entrySet()) {
            assigned.put(entry.getKey(), (Integer)entry.getValue().getValue());
        }

        int used;
        try{
            used = pointBuyRule.totalCost(assigned);
        } catch (IllegalArgumentException ex) {
            remainingLabel.setText("Invalid value");
            remainingLabel.setForeground(Color.RED);
            applyButton.setEnabled(false);
            return;
        }

        int remaining = pointBuyRule.getBudget() - used;
        remainingLabel.setText("Remaining: " + remaining);
        remainingLabel.setForeground(remaining < 0  ? Color.RED : Color.GREEN);
        applyButton.setEnabled(remaining >= 0);

        for(Map.Entry<Stat, JSpinner> entry : spinnerMap.entrySet()) {
            int v = (Integer)entry.getValue().getValue();
            int cost = pointBuyRule.costOf(v);
            entry.getValue().setToolTipText("Value " + v + " - cost: " + (cost == Integer.MAX_VALUE ? "N/A" : cost));
        }
    }

    private void cleanTheBaseStats() {
        for (Stat s : Stat.values()) { if(character.isBaseAssigned(s)) character.clearBaseStat(s); }
    }
    /**
     * SetWindow the window
     */
    private void set(){
        //------------------------------------------------Main Frame--------------------------------------------------//
        setLayout(new BorderLayout(10,10));
        add(SUBMIT, BorderLayout.SOUTH);
        add(CARDS_PANEL, BorderLayout.CENTER);
        setJMenuBar(MENU_BAR);
        //------------------------------------------------Base Panel--------------------------------------------------//
        BASE.setLayout(gridLayout);
        NAME.setPreferredSize(dimension);
        BASE.add(NAME);
        AGE.setPreferredSize(dimension);
        BASE.add(AGE);
        GENDER.setPreferredSize(dimension);
        BASE.add(GENDER);
        RACE.setPreferredSize(dimension);
        BASE.add(RACE);
        SET_BUTTON.setPreferredSize(dimension);
        BASE.add(SET_BUTTON);
        RANDOM_NAME.setPreferredSize(dimension);
        BASE.add(RANDOM_NAME);
        //-----------------------------------------------Random Panel-------------------------------------------------//
        RANDOM_PARAM.setLayout(gridLayout);
        RANDOM_OUTPUT.setPreferredSize(dimension);
        RANDOM_PARAM.add(RANDOM_OUTPUT);
        ROLL.setPreferredSize(dimension);
        RANDOM_PARAM.add(ROLL);
        RANDOM_RESET.setPreferredSize(dimension);
        RANDOM_PARAM.add(RANDOM_RESET);
        //-----------------------------------------------Pre-set Panel------------------------------------------------//
        PRE_SET_PARAM.setLayout(gridLayout);
        PRE_SET_OUTPUT.setPreferredSize(dimension);
        PRE_SET_PARAM.add(PRE_SET_OUTPUT);
        PRE_SET_SET.setPreferredSize(dimension);
        PRE_SET_PARAM.add(PRE_SET_SET);
        PRE_SET_RESET.setPreferredSize(dimension);
        PRE_SET_PARAM.add(PRE_SET_RESET);
        //----------------------------------------------Point-Buy Panel-----------------------------------------------//
        POIT_BUY_PARAM.setLayout(new BorderLayout(5,5));
        JPanel grid = new JPanel(new GridLayout(Stat.values().length,3,5,5));
        ChangeListener listener = this::onSpinnerChanged;
        for (Stat s : Stat.values()){
            SpinnerNumberModel model = new SpinnerNumberModel(8,8,15,1);
            JSpinner spinner = new JSpinner(model);
            ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setColumns(2);
            spinner.setToolTipText("Cost: " + pointBuyRule.costOf(8));
            spinner.addChangeListener(listener);
            spinnerMap.put(s, spinner);
            grid.add(new JLabel(s.getName()));
            grid.add(spinner);
        }

        JPanel buttonPanel = new JPanel(new GridLayout(1,2,5,5));
        buttonPanel.add(remainingLabel);
        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);
        POIT_BUY_PARAM.add(grid, BorderLayout.CENTER);
        POIT_BUY_PARAM.add(buttonPanel, BorderLayout.SOUTH);
        //-------------------------------------------------Menu Bar---------------------------------------------------//
        STAGES.add(BASE_ITEM);
        PARAM_STAGES.add(RANDOM_SUB_ITEM);
        PARAM_STAGES.add(POINT_BUY_SUB_ITEM);
        PARAM_STAGES.add(PRE_SET_SUB_ITEM);
        STAGES.add(PARAM_STAGES);
        MENU_BAR.add(STAGES);
        //------------------------------------------------Card Panel--------------------------------------------------//
        CARDS_PANEL.add(BASE, "BASE");
        CARDS_PANEL.add(RANDOM_PARAM, "RANDOM");
        CARDS_PANEL.add(PRE_SET_PARAM, "PRE-SET");
        CARDS_PANEL.add(POIT_BUY_PARAM, "POINT-BUY");
    }
    //=============================================Card Panel Group===================================================//
    private final CardLayout CARD_LAYOUT = new CardLayout();
    private final JPanel CARDS_PANEL = new JPanel(CARD_LAYOUT);
    //==================================================Panels========================================================//
    private final JPanel BASE = new JPanel();
    private final JPanel RANDOM_PARAM = new JPanel();
    private final JPanel PRE_SET_PARAM = new JPanel();
    private final JPanel POIT_BUY_PARAM = new JPanel();
    //================================================Card Panel======================================================//
    private final JButton SUBMIT = new JButton("SUBMIT");
    //==================================================JMenu=========================================================//
    private final JMenuBar MENU_BAR = new JMenuBar();
    private final JMenu STAGES = new JMenu("STAGES");
    private final JMenu PARAM_STAGES = new JMenu("PARAM");
    private final JMenuItem BASE_ITEM = new JMenuItem("MAIN");
    private final JMenuItem PRE_SET_SUB_ITEM = new JMenuItem("PRE-SET");
    private final JMenuItem RANDOM_SUB_ITEM = new JMenuItem("RANDOM");
    private final JMenuItem POINT_BUY_SUB_ITEM = new JMenuItem("POINT-BUY");
    //================================================Base Panel======================================================//
    private final JButton SET_BUTTON = new JButton("SET");
    private final JButton RANDOM_NAME = new JButton("RANDOM NAME");
    private final JTextField NAME = new JTextField();
    private final JTextField AGE = new JTextField();
    private final String[] genderArray = {
        Gender.MALE.getDisplayName(),
        Gender.FEMALE.getDisplayName(),
        Gender.UNKNOWN.getDisplayName()
    };
    private final JComboBox<String> GENDER = new JComboBox<>(genderArray);
    private final String[] raceArray = {
        Race.DRAGONBORN.getDisplayName(), Race.DWARF.getDisplayName(),
        Race.ELF.getDisplayName(), Race.GNOME.getDisplayName(),
        Race.HALF_ELF.getDisplayName(), Race.HALFLING.getDisplayName(),
        Race.HALF_ORK.getDisplayName(), Race.HUMAN.getDisplayName(),
        Race.ORC.getDisplayName(), Race.TIEFLING.getDisplayName()
    };
    private final JComboBox<String> RACE = new JComboBox<>(raceArray);
    //===============================================Random Panel=====================================================//
    private final JButton ROLL = new JButton("ROLL");
    private final JTextField RANDOM_OUTPUT = new JTextField();
    private final JButton RANDOM_RESET = new JButton("RESET");
    //==============================================Pre-set Panel=====================================================//
    private final JTextField PRE_SET_OUTPUT = new JTextField();
    private final JButton PRE_SET_RESET = new JButton("RESET");
    private final JButton PRE_SET_SET = new JButton("SET");
    //=============================================Point-Buy Panel====================================================//
    private final PointBuyRule pointBuyRule = new PointBuyRule();
    private final EnumMap<Stat, JSpinner> spinnerMap= new EnumMap<>(Stat.class);
    private final JLabel remainingLabel = new JLabel();
    private final JButton applyButton = new JButton("APPLY");
    private final JButton resetButton = new JButton("RESET");
}