import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class GameSetupFrame extends JFrame {
    private List<PlayerSetupPanel> playerPanels;
    private JPanel playerListPanel;
    private JButton addPlayerButton;
    private JButton startButton;
    private int playerCount = 0;

    private Color[] playerColors = {
            new Color(135, 206, 250),
            new Color(255, 182, 193),
            new Color(36, 22, 160),
            new Color(139, 69, 19),
            new Color(255, 215, 0),
            new Color(147, 112, 219),
            new Color(50, 205, 50),
            new Color(255, 140, 0)
    };

    public GameSetupFrame() {
        setTitle("Snake and Ladder - Setup");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        FullBackgroundPanel mainPanel =
                new FullBackgroundPanel("C:\\Users\\echaa\\IdeaProjects\\untitled1\\images\\back.jpeg");

        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        setContentPane(mainPanel);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);


        JLabel titleLabel = new JLabel("SNAKE AND LADDER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(135, 206, 250)); // biru muda
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Add Players to Start");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(135, 206, 250)); // abu terang
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(subtitleLabel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        playerListPanel.setOpaque(false);


        JScrollPane scrollPane = new JScrollPane(playerListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);


        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);


        addPlayerButton = new RoundedButton("+ ADD PLAYER", 25);
        addPlayerButton.setBackground(new Color(72, 149, 239));

        addPlayerButton.setFont(new Font("Arial", Font.BOLD, 16));
        addPlayerButton.setBackground(new Color(100, 149, 237));
        addPlayerButton.setForeground(Color.WHITE);
        addPlayerButton.setFocusPainted(false);
        addPlayerButton.setPreferredSize(new Dimension(180, 50));
        addPlayerButton.addActionListener(e -> addPlayer());

        startButton = new RoundedButton("START GAME", 25);
        startButton.setBackground(new Color(30, 90, 180));
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(180, 50));
        startButton.setEnabled(false);
        startButton.addActionListener(e -> startGame());

        buttonPanel.add(addPlayerButton);
        buttonPanel.add(startButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);


        playerPanels = new ArrayList<>();

        addPlayer();
        addPlayer();

        setVisible(true);
    }