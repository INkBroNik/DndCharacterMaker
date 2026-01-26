package charactermaker.jforms;

import charactermaker.model.autorization.CharacterService;
import charactermaker.model.autorization.UserSevice;
import charactermaker.model.dataHolders.CharacterHolder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class CreateAndShowWindow extends JFrame {
    public CreateAndShowWindow(UserSevice userSevice, CharacterService characterService, CountDownLatch latch) {
        this.userSevice = userSevice;
        this.characterService = characterService;
        set();
        setDefaultCloseOperation(SetWindow.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                latch.countDown();
            }
        });
        setLocationRelativeTo(null);
        pack();
        setResizable(false);
        setVisible(true);
        showLogin();
    }

    private void showLogin()    { cardLayout.show(cardsPanel, "LOGIN");  }
    private void showEditor()   { cardLayout.show(cardsPanel, "EDITOR"); }

    public void set(){
        setLayout(new BorderLayout());
        cardsPanel.add(buildLoginPanel(), "LOGIN");
        cardsPanel.add(buildEditorPanel(), "EDITOR");
        add(cardsPanel, BorderLayout.CENTER);
        orphans.addActionListener(ev -> showOrphanManager());
        admin.setEnabled(false);
        admin.add(orphans);
        menuBar.add(admin);
        setJMenuBar(menuBar);
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3,2,5,5));
        panel.add(new JLabel("Username:"));
        loginUserField.setText("");
        panel.add(loginUserField);
        panel.add(new JLabel("Password:"));
        loginPasswordField.setText("");
        panel.add(loginPasswordField);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener(e -> doLogin());
        registerButton.addActionListener(e -> doRegister());
        return panel;
    }

    private JPanel buildEditorPanel() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        characterList.setCellRenderer(new CharacterCellRenderer());
        characterList.setFixedCellHeight(64);
        JScrollPane scrollPane = new JScrollPane(characterList);
        panel.add(scrollPane, BorderLayout.CENTER);
        JPanel rightPanel = new JPanel(new GridLayout(2,2,5,5));
        JPanel topPanel = new JPanel(new GridLayout(2,2,5,5));
        JButton addButton = new JButton("Add Character");
        JButton editButton = new JButton("Edit Character");
        JButton deleteButton = new JButton("Delete Character");
        JButton logoutButton = new JButton("Logout");
        topPanel.add(addButton); topPanel.add(editButton); topPanel.add(deleteButton); topPanel.add(logoutButton);
        rightPanel.add(topPanel);

        JTextArea details = new JTextArea(10, 30);
        details.setEditable(false);
        rightPanel.add(new JScrollPane(details));
        panel.add(rightPanel, BorderLayout.EAST);

        addButton.addActionListener(e -> onAdd());
        editButton.addActionListener(e -> onEdit());
        deleteButton.addActionListener(e -> onDelete());
        logoutButton.addActionListener(e -> { currentUser = null; updateMenuForUser(null); showLogin(); });

        characterList.addListSelectionListener(e -> {
            CharacterHolder selectedValue = characterList.getSelectedValue();
            if (selectedValue != null) { details.setText(renderDetails(selectedValue)); }
            else  { details.setText(""); }
        });
        characterList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = characterList.locationToIndex(e.getPoint());
                if (idx < 0) return;
                characterList.setSelectedIndex(idx);
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    onEdit(); // двойной клик → редактирование
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu menu = new JPopupMenu();
                    JMenuItem edit = new JMenuItem("Edit");
                    JMenuItem del = new JMenuItem("Delete");
                    edit.addActionListener(a -> onEdit());
                    del.addActionListener(a -> onDelete());
                    menu.add(edit); menu.add(del); menu.addSeparator();
                    menu.show(characterList, e.getX(), e.getY());
                }
            }
        });

        return panel;
    }

    private boolean mathces(CharacterHolder c, String q) {
        return c.getName().toLowerCase().contains(q)
                || c.getRace().getDisplayName().contains(q)
                || String.valueOf(c.getLevel()).contains(q);
    }

    private String renderDetails(CharacterHolder selectedValue) {
        if(selectedValue == null) {
            return "";
        }
        return selectedValue.toString();
    }

    private void onAdd() {
        CharacterHolder characterHolder = characterService.addCharacter(currentUser);
        new SetWindow(characterHolder);
        listModel.addElement(characterHolder);
        characterList.setSelectedValue(characterHolder, true);
    }
    private void onEdit() {
        CharacterHolder selectedValue = characterList.getSelectedValue();
        if (selectedValue == null) return;
        CharacterHolder copy = selectedValue.copy();
        new SetWindow(copy);
        characterService.updateCharacter(copy);
        refreshCharacterList();
        characterList.setSelectedValue(copy, true);
    }
    private void onDelete() {
        CharacterHolder selectedValue = characterList.getSelectedValue();
        if (selectedValue == null) return;
        int yn = JOptionPane.showConfirmDialog(
                this, "Delete " + selectedValue.getName(),
                "Confirm Delete", JOptionPane.YES_NO_OPTION
        );
        if (yn == JOptionPane.YES_OPTION) {
            characterService.deleteCharacter(selectedValue.getId());
            refreshCharacterList();
        }
    }
    private void doLogin() {
        String username = loginUserField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        if(userSevice.autheticate(username, password)) {
            if (userSevice.isAdmin(username)) showOrphanManager();
            currentUser = username;
            updateMenuForUser(currentUser);
            refreshCharacterList();
            showEditor();
        } else {
            JOptionPane.showMessageDialog(
                    this, "Invalid username or password",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void doRegister() {
        String username = loginUserField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        if(username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Enter username and password",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if(userSevice.register(username, password)) {
            JOptionPane.showMessageDialog(
                    this, "Registered. You can login now.",
                    "OK", JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this, "Registration failed (maybe user exists)",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshCharacterList() {
        listModel.clear();
        for(CharacterHolder c : characterService.getCharactersFor(currentUser)) listModel.addElement(c);
    }

    private void showOrphanManager() {
        java.util.List<CharacterHolder> orphans = characterService.getOrphans();
        if (orphans.isEmpty() || !currentUser.equals("Author")) {
            JOptionPane.showMessageDialog(
                    this, "No unassigned character found.",
                    "Orphan Manager", JOptionPane.INFORMATION_MESSAGE
            );
            return;
        } // ничего не делать

        // Таблица
        String[] cols = {"ID", "Name", "Created", "Owner"};
        Object[][] data = new Object[orphans.size()][cols.length];
        for (int i = 0; i < orphans.size(); i++) {
            CharacterHolder c = orphans.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getCreatedAt();
            data[i][3] = c.getOwner();
        }
        JTable table = new JTable(data, cols);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        JPanel panel = new JPanel(new BorderLayout(6,6));
        panel.add(new JLabel("Unassigned characters — you can claim, assign or delete"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton claimBtn = new JButton("Claim selected to me");
        JButton assignBtn = new JButton("Assign to...");
        JButton sysBtn = new JButton("Assign to 'system'");
        JButton delBtn = new JButton("Delete selected");
        JButton exportBtn = new JButton("Export selected JSON");
        btns.add(claimBtn); btns.add(assignBtn); btns.add(sysBtn); btns.add(delBtn); btns.add(exportBtn);
        panel.add(btns, BorderLayout.SOUTH);

        JDialog dlg = new JDialog(this, "Orphan Manager", true);
        dlg.setContentPane(panel);
        dlg.setSize(900, 400);
        dlg.setLocationRelativeTo(this);

        claimBtn.addActionListener(e -> {
            int[] sel = table.getSelectedRows();
            if (sel.length == 0) { JOptionPane.showMessageDialog(dlg, "Select rows"); return; }
            int success = 0, conflict = 0;
            for (int r : sel) {
                long id = ((Number) table.getValueAt(r, 0)).longValue();
                int res = characterService.claimOrphan(id, currentUser);
                if (res == 0) success++; else if (res == 1) conflict++;
            }
            JOptionPane.showMessageDialog(dlg, String.format("Claimed: %d, Conflicts: %d", success, conflict));
            refreshCharacterList();
            dlg.dispose();
        });

        assignBtn.addActionListener(e -> {
            String to = JOptionPane.showInputDialog(dlg, "Assign to username:");
            if (to == null || to.isBlank()) return;
            int[] sel = table.getSelectedRows();
            int count = 0;
            for (int r : sel) {
                long id = ((Number) table.getValueAt(r, 0)).longValue();
                // force-assign (bypass claim): implement method if needed; for simplicity use claimOrphan then assignOrphansTo for selection
                int res = characterService.claimOrphan(id, to);
                if (res == 0) count++;
                else {
                    // if conflict, try force-assign
                    // skip for now
                }
            }
            JOptionPane.showMessageDialog(dlg, "Assigned: " + count);
            refreshCharacterList();
            dlg.dispose();
        });

        sysBtn.addActionListener(e -> {
            int[] sel = table.getSelectedRows();
            int count = 0;
            for (int r : sel) {
                long id = ((Number) table.getValueAt(r, 0)).longValue();
                int res = characterService.claimOrphan(id, "system");
                if (res == 0) count++;
            }
            JOptionPane.showMessageDialog(dlg, "Assigned to system: " + count);
            refreshCharacterList();
            dlg.dispose();
        });

        delBtn.addActionListener(e -> {
            int[] sel = table.getSelectedRows();
            if (sel.length == 0) return;
            int yn = JOptionPane.showConfirmDialog(dlg, "Delete selected permanently?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (yn != JOptionPane.YES_OPTION) return;
            int removed = 0;
            for (int i = sel.length - 1; i >= 0; i--) { // удаляем с конца
                int r = sel[i];
                long id = ((Number) table.getValueAt(r, 0)).longValue();
                if (characterService.deleteCharacter(id)) removed++;
            }
            JOptionPane.showMessageDialog(dlg, "Removed: " + removed);
            refreshCharacterList();
            dlg.dispose();
        });

        exportBtn.addActionListener(e -> {
            int[] sel = table.getSelectedRows();
            if (sel.length == 0) return;
            java.util.List<CharacterHolder> toExport = new ArrayList<>();
            for (int r : sel) {
                long id = ((Number) table.getValueAt(r, 0)).longValue();
                for (CharacterHolder c : characterService.getAllCharacters()) {
                    if (c.getId() == id) { toExport.add(c); break; }
                }
            }
            // просто сохранить выбранные в файл
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(dlg) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                    new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(toExport, fw);
                    JOptionPane.showMessageDialog(dlg, "Exported");
                } catch (Exception ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(dlg, "Failed"); }
            }
        });

        dlg.setVisible(true);
    }

    private void updateMenuForUser(String username){
        boolean adminMode = userSevice.isAdmin(username);
        admin.setEnabled(adminMode);
        menuBar.revalidate();
        menuBar.repaint();
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);
    private String currentUser;
    private final JTextField loginUserField  = new JTextField();
    private final JPasswordField loginPasswordField = new JPasswordField();
    private final DefaultListModel<CharacterHolder> listModel = new DefaultListModel<>();
    private final JList<CharacterHolder> characterList = new JList<>(listModel);
    private final JTextField searchField = new JTextField(20);
    private UserSevice userSevice;
    private CharacterService characterService;
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu admin = new JMenu("Admin");
    private final JMenuItem orphans = new JMenuItem("Orphans Manager");
}