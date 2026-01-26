package charactermaker.jforms;

import charactermaker.model.dataHolders.CharacterHolder;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CharacterCellRenderer extends JPanel implements ListCellRenderer<CharacterHolder> {
    private final JLabel iconLabel = new JLabel();
    private final JLabel titleLabel = new JLabel();
    private final JLabel subtitleLabel = new JLabel();

    public CharacterCellRenderer() {
        setLayout(new BorderLayout(8, 2));
        JPanel text = new JPanel(new GridLayout(0,1));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(Font.PLAIN, 12f));
        subtitleLabel.setForeground(Color.DARK_GRAY);

        text.add(titleLabel);
        text.add(subtitleLabel);

        add(iconLabel, BorderLayout.WEST);
        add(text, BorderLayout.CENTER);

        setBorder(new EmptyBorder(6,6,6,6));
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends CharacterHolder> list,
                                                  CharacterHolder value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            // title: Name (Lv X)
            titleLabel.setText(value.getName() + "  (Lv " + value.getLevel() + ")");

            // subtitle: Race • Class
            subtitleLabel.setText(value.getRace() + "  •  ");

            // tooltip
            String tooltip = value.toString();
            setToolTipText(tooltip);
        } else {
            titleLabel.setText("");
            subtitleLabel.setText("");
            iconLabel.setIcon(null);
            setToolTipText(null);
        }

        // selection colors
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
