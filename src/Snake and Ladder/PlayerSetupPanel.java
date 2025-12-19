import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

class PlayerSetupPanel extends JPanel {
    private List<ScoreCardPanel> scoreCards;
    private int playerNumber;
    private JLabel numberLabel;
    private JTextField nameField;
    private JButton removeButton;
    private String imagePath;
    private Color playerColor;
    private GameSetupFrame parentFrame;

    public PlayerSetupPanel(int number, String defaultName, String imagePath, Color color, GameSetupFrame parent) {
        this.playerNumber = number;
        this.imagePath = imagePath;
        this.playerColor = color;
        this.parentFrame = parent;

        setLayout(new BorderLayout(10, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));


        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        numberLabel = new JLabel("Player " + number);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        numberLabel.setForeground(playerColor);
        numberLabel.setPreferredSize(new Dimension(100, 30));

        leftPanel.add(numberLabel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        nameField = new JTextField("Enter player name");
        nameField.setFont(new Font("Arial", Font.ITALIC, 14));
        nameField.setForeground(Color.GRAY);
        nameField.setPreferredSize(new Dimension(200, 30));

        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Enter player name")) {
                    nameField.setText("");
                    nameField.setFont(new Font("Arial", Font.PLAIN, 14));
                    nameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().trim().isEmpty()) {
                    nameField.setText("Enter player name");
                    nameField.setFont(new Font("Arial", Font.ITALIC, 14));
                    nameField.setForeground(Color.GRAY);
                }
            }
        });


        RoundedTextFieldPanel roundedField = new RoundedTextFieldPanel(18);
        roundedField.setPreferredSize(new Dimension(260, 36));

        roundedField.add(nameField, BorderLayout.CENTER);
        centerPanel.add(roundedField, BorderLayout.CENTER);

        removeButton = new RoundedButton("X", 15);
        removeButton.setBackground(new Color(220, 53, 69));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFont(new Font("Arial", Font.BOLD, 16));
        removeButton.setPreferredSize(new Dimension(40, 30));
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(e -> parentFrame.removePlayer(this));


        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(removeButton, BorderLayout.EAST);
    }

    public void updatePlayerNumber(int newNumber) {
        this.playerNumber = newNumber;
        numberLabel.setText("Player " + newNumber);
    }

    public String getPlayerName() {
        return nameField.getText();
    }

    public String getImagePath() {
        return imagePath;
    }
}