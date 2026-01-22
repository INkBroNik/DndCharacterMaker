package charactermaker.jforms;

import charactermaker.enums.Sex;
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
    private final Dimension dimension;
    private final GridLayout gridLayout;
    private static final CharacterHolder CHARACTER = new CharacterHolder();

    /**
     * Default constructor, set class properties
     */
    public SetWindow() {
        int width = 200;
        int height = 50;
        dimension = new Dimension(width, height);
        gridLayout = new GridLayout(3,3,5,5);
        set();
        baseSetButton.addActionListener((e) -> {
            Race race = Race.findByName((String) raceComboBox.getSelectedItem());
            Sex sex = Sex.findByName((String) sexComboBox.getSelectedItem());
            String name = null;
            int age = 0;
            int level = 1;
            try {
                name = nameField.getText();
                age = Integer.parseInt(ageField.getText());

                CHARACTER.applyRace(race); CHARACTER.setName(name); CHARACTER.setAge(age);
                CHARACTER.setLevel(level); CHARACTER.setGender(sex);
                if (!CHARACTER.getPendingChoices().isEmpty()) {
                    showChoiceDialog(CHARACTER, CHARACTER.getPendingChoices(), 2, true);
                }

                System.out.println(CHARACTER);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog
                        (this, "ERORR!\n Please reenter the fields!",
                                "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        randomNameButton.addActionListener((e) -> {
            Race race = Race.findByName((String) raceComboBox.getSelectedItem());
            Sex sex = Sex.findByName((String) sexComboBox.getSelectedItem());
            nameField.setText(race.randomName(sex));
        });

        rollButon.addActionListener(e -> {
            cleanTheBaseStats();
            StatAllocation allocation = new StatAllocation();
            StatGenerationRule rule = new DiceRollRule();
            List<Integer> rolledValues = rule.generate();
            showStatSelection(rolledValues, allocation, randomOutput);
        });

        randomResetButton.addActionListener(e -> {
            CHARACTER.resetBaseStats();
            randomOutput.setText("Base stats been re-set");
        });

        preSetSetButton.addActionListener(e -> {
            cleanTheBaseStats();
            StatAllocation allocation = new StatAllocation();
            StatGenerationRule rule = new StandardArrayRule();
            List<Integer> preSetValues = rule.generate();
            showStatSelection(preSetValues, allocation, preSetOutput);
        });

        preSetResetButton.addActionListener(e -> {
            CHARACTER.resetBaseStats();
            preSetOutput.setText("Base stats been re-set");
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
                JOptionPane.showMessageDialog
                        (this,
                                "Invalid stat values: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE
                        );
                return;
            }

            try{
                CHARACTER.applyAllocation(allocation, false);
            } catch(IllegalArgumentException ex){
                int res = JOptionPane.showConfirmDialog
                        (this, "Some base stats already set. Overwrite?",
                                "Confirm overwrite", JOptionPane.YES_NO_OPTION
                        );
                if(res == JOptionPane.YES_OPTION){
                    try{
                        CHARACTER.applyAllocation(allocation, true);
                    } catch (RuntimeException ex2) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Failed to apply allocation: " + ex2.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                } else {
                    return;
                }
            } catch(RuntimeException ex){
                JOptionPane.showMessageDialog(
                        this, "Failed to apply allocation: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            JOptionPane.showMessageDialog(this, "Applied base stats.");
            System.out.println(CHARACTER);
        });

        resetButton.addActionListener(e -> {
           for (JSpinner spinner : spinnerMap.values()) spinner.setValue(8);
           onSpinnerChanged(null);
        });

        baseItem.addActionListener(e -> cardLayout.show(cardsPanel, "BASE"));
        randomSubItem.addActionListener(e -> cardLayout.show(cardsPanel, "RANDOM"));
        preSetSubItem.addActionListener(e -> cardLayout.show(cardsPanel, "PRE-SET"));
        pointBuySubItem.addActionListener(e -> cardLayout.show(cardsPanel, "POINT-BUY"));

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
                Stat chosen = showStatSelectionDialog(this, CHARACTER, allocation ,value);
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
            CHARACTER.applyAllocation(allocation, false);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Failed to apply allocation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println(CHARACTER.toString());
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
        for (Stat s : Stat.values()) { if(CHARACTER.isBaseAssigned(s)) CHARACTER.clearBaseStat(s); }
    }
    /**
     * SetWindow the window
     */
    private void set(){
        //------------------------------------------------Main Frame--------------------------------------------------//
        setLayout(new BorderLayout(10,10));
        add(cardsPanel, BorderLayout.CENTER);
        setJMenuBar(menuBar);
        //------------------------------------------------Base Panel--------------------------------------------------//
        basePanel.setLayout(gridLayout);
        nameField.setPreferredSize(dimension);
        basePanel.add(nameField);
        ageField.setPreferredSize(dimension);
        basePanel.add(ageField);
        sexComboBox.setPreferredSize(dimension);
        basePanel.add(sexComboBox);
        raceComboBox.setPreferredSize(dimension);
        basePanel.add(raceComboBox);
        baseSetButton.setPreferredSize(dimension);
        basePanel.add(baseSetButton);
        randomNameButton.setPreferredSize(dimension);
        basePanel.add(randomNameButton);
        //-----------------------------------------------Random Panel-------------------------------------------------//
        randomParamPanel.setLayout(gridLayout);
        randomOutput.setPreferredSize(dimension);
        randomParamPanel.add(randomOutput);
        rollButon.setPreferredSize(dimension);
        randomParamPanel.add(rollButon);
        randomResetButton.setPreferredSize(dimension);
        randomParamPanel.add(randomResetButton);
        //-----------------------------------------------Pre-set Panel------------------------------------------------//
        preSetParamPanel.setLayout(gridLayout);
        preSetOutput.setPreferredSize(dimension);
        preSetParamPanel.add(preSetOutput);
        preSetSetButton.setPreferredSize(dimension);
        preSetParamPanel.add(preSetSetButton);
        preSetResetButton.setPreferredSize(dimension);
        preSetParamPanel.add(preSetResetButton);
        //----------------------------------------------Point-Buy Panel-----------------------------------------------//
        pointBuyParamPanel.setLayout(new BorderLayout(5,5));
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
        pointBuyParamPanel.add(grid, BorderLayout.CENTER);
        pointBuyParamPanel.add(buttonPanel, BorderLayout.SOUTH);
        //-------------------------------------------------Menu Bar---------------------------------------------------//
        stages.add(baseItem);
        paramStages.add(randomSubItem);
        paramStages.add(pointBuySubItem);
        paramStages.add(preSetSubItem);
        stages.add(paramStages);
        menuBar.add(stages);
        //------------------------------------------------Card Panel--------------------------------------------------//
        cardsPanel.add(basePanel, "BASE");
        cardsPanel.add(randomParamPanel, "RANDOM");
        cardsPanel.add(preSetParamPanel, "PRE-SET");
        cardsPanel.add(pointBuyParamPanel, "POINT-BUY");
    }
    //=============================================Card Panel Group===================================================//
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);
    //==================================================Panels========================================================//
    private final JPanel basePanel = new JPanel();
    private final JPanel randomParamPanel = new JPanel();
    private final JPanel preSetParamPanel = new JPanel();
    private final JPanel pointBuyParamPanel = new JPanel();
    //==================================================JMenu=========================================================//
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu stages = new JMenu("STAGES");
    private final JMenu paramStages = new JMenu("PARAM");
    private final JMenuItem baseItem = new JMenuItem("MAIN");
    private final JMenuItem preSetSubItem = new JMenuItem("PRE-SET");
    private final JMenuItem randomSubItem = new JMenuItem("RANDOM");
    private final JMenuItem pointBuySubItem = new JMenuItem("POINT-BUY");
    //================================================Base Panel======================================================//
    private final JButton baseSetButton = new JButton("SET");
    private final JButton randomNameButton = new JButton("RANDOM NAME");
    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final String[] sexArray = {
        Sex.MALE.getDisplayName(),
        Sex.FEMALE.getDisplayName(),
        Sex.UNKNOWN.getDisplayName()
    };
    private final JComboBox<String> sexComboBox = new JComboBox<>(sexArray);
    private final String[] raceArray = {
        Race.DRAGONBORN.getDisplayName(), Race.DWARF.getDisplayName(),
        Race.ELF.getDisplayName(), Race.GNOME.getDisplayName(),
        Race.HALF_ELF.getDisplayName(), Race.HALFLING.getDisplayName(),
        Race.HALF_ORK.getDisplayName(), Race.HUMAN.getDisplayName(),
        Race.ORC.getDisplayName(), Race.TIEFLING.getDisplayName()
    };
    private final JComboBox<String> raceComboBox = new JComboBox<>(raceArray);
    //===============================================Random Panel=====================================================//
    private final JButton rollButon = new JButton("ROLL");
    private final JTextField randomOutput = new JTextField();
    private final JButton randomResetButton = new JButton("RESET");
    //==============================================Pre-set Panel=====================================================//
    private final JTextField preSetOutput = new JTextField();
    private final JButton preSetResetButton = new JButton("RESET");
    private final JButton preSetSetButton = new JButton("SET");
    //=============================================Point-Buy Panel====================================================//
    private final PointBuyRule pointBuyRule = new PointBuyRule();
    private final EnumMap<Stat, JSpinner> spinnerMap= new EnumMap<>(Stat.class);
    private final JLabel remainingLabel = new JLabel();
    private final JButton applyButton = new JButton("APPLY");
    private final JButton resetButton = new JButton("RESET");
}