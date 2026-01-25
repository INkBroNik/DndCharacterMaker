package charactermaker.jforms;

import charactermaker.model.autorization.CharacterService;
import charactermaker.model.autorization.UserSevice;
import charactermaker.model.dataHolders.CharacterHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3,2,5,5));
        panel.add(new JLabel("Username:"));
        panel.add(new JLabel("Password:"));
        panel.add(loginUserField);
        panel.add(loginPasswordField);

        JPanel buttonPanel = new JPanel(new GridLayout(1,2,5,5));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel);

        loginButton.addActionListener(e -> doLogin());
        registerButton.addActionListener(e -> doRegister());
        return panel;
    }

    private JPanel buildEditorPanel() {
        JPanel panel = new JPanel(new BorderLayout(5,5));
        characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        logoutButton.addActionListener(e -> { currentUser = null; showLogin(); });

        characterList.addListSelectionListener(e -> {
            CharacterHolder selectedValue = characterList.getSelectedValue();
            if (selectedValue != null) { details.setText(renderDetails(selectedValue)); }
            else  { details.setText(""); }
        });
        return panel;
    }

    private String renderDetails(CharacterHolder selectedValue) {
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
            currentUser = username;
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

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);
    private String currentUser;
    private final JTextField loginUserField  = new JTextField();
    private final JPasswordField loginPasswordField = new JPasswordField();
    private final DefaultListModel<CharacterHolder> listModel = new DefaultListModel<>();
    private final JList<CharacterHolder> characterList = new JList<>(listModel);
    private UserSevice userSevice;
    private CharacterService characterService;
}
