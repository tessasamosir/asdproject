import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class GameFrame extends JFrame {
    private static final int DICE_SIZE = 120;
    private GameBoard board;
    private List<Player> players;
    private int currentPlayerIndex;
    private GameBoardPanel boardPanel;
    private JPanel infoPanel;
    private JLabel currentPlayerLabel;
    private JLabel diceResultLabel;
    private JButton rollDiceButton;
    private Dice dice;
    private JTextArea logArea;
    private javax.swing.Timer animationTimer;
    private JLabel diceFaceLabel;
    private ImageIcon[] diceIcons;
    private List<ScoreCardPanel> scoreCards;
    private JPanel scoresPanel;
    private JLabel scoreTitle;

    private void updateScoreRanking(JPanel scoresPanel, List<ScoreCardPanel> cards) {
        cards.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        scoresPanel.removeAll();

        // 🔥 TAMBAHKAN JUDUL LAGI
        scoresPanel.add(scoreTitle);
        scoresPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        for (ScoreCardPanel card : cards) {
            scoresPanel.add(card);
            scoresPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        scoresPanel.revalidate();
        scoresPanel.repaint();
    }

    private ImageIcon loadDiceIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(
                DICE_SIZE, DICE_SIZE, Image.SCALE_SMOOTH
        );
        return new ImageIcon(img);
    }


    public GameFrame(List<Player> players) {
        this.players = players;
        this.board = new GameBoard();
        this.dice = new Dice();
        this.currentPlayerIndex = 0;

        int cellSize = 90;
        int boardSize = cellSize * 8;
        int rightPanelWidth = 300;

        setSize(boardSize + rightPanelWidth, boardSize + 40);
        setLocationRelativeTo(null);

        setTitle("Snake and Ladder Game - " + players.size() + " Players");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boardPanel = new GameBoardPanel(board, players);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        boardPanel.setOpaque(false);

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 8 * 90));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        RoundedPanel playerInfoPanel = new RoundedPanel(25, new Color(72, 149, 239));
        playerInfoPanel.setLayout(new BoxLayout(playerInfoPanel, BoxLayout.Y_AXIS));
        playerInfoPanel.setBackground(new Color(100, 149, 237));
        playerInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        currentPlayerLabel = new JLabel();
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        diceIcons = new ImageIcon[6];
        for (int i = 0; i < 6; i++) {
            diceIcons[i] = loadDiceIcon(
                    "C:\\Users\\echaa\\IdeaProjects\\untitled1\\images\\dice" + (i + 1) + ".png"
            );
        }


        diceFaceLabel = new JLabel(diceIcons[0], SwingConstants.CENTER);
        diceFaceLabel.setPreferredSize(new Dimension(DICE_SIZE, DICE_SIZE));
        diceFaceLabel.setMinimumSize(new Dimension(DICE_SIZE, DICE_SIZE));
        diceFaceLabel.setMaximumSize(new Dimension(DICE_SIZE, DICE_SIZE));
        diceFaceLabel.setOpaque(false);

        JPanel dicePanel = new JPanel(new GridBagLayout());
        dicePanel.setPreferredSize(new Dimension(120, 120));
        dicePanel.setMaximumSize(new Dimension(120, 120));
        dicePanel.setOpaque(false);

        dicePanel.add(diceFaceLabel);

        diceResultLabel = new JLabel("Press button to roll");
        diceResultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        diceResultLabel.setForeground(Color.WHITE);
        diceResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        diceResultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        diceResultLabel.setForeground(Color.WHITE);
        diceResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerInfoPanel.add(currentPlayerLabel);
        playerInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        playerInfoPanel.add(diceResultLabel);

        rollDiceButton = new RoundedButton("ROLL DICE", 30);
        rollDiceButton.setBackground(new Color(72, 149, 239));
        rollDiceButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollDiceButton.setPreferredSize(new Dimension(180, 45));
        rollDiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollDiceButton.addActionListener(e -> rollDice());

        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        rightPanel.add(dicePanel);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        rightPanel.add(rollDiceButton);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setOpaque(false);
        scoresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoresPanel.setMaximumSize(new Dimension(260, 200));

        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));
        scoresPanel.setBackground(new Color(255, 255, 255, 220));
        scoresPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        scoresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoresPanel.setMaximumSize(new Dimension(260, 160));

        scoreTitle = new JLabel("Player Scores");
        scoreTitle.setFont(new Font("Arial", Font.BOLD, 15));
        scoreTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreTitle.setForeground(Color.DARK_GRAY);

        scoresPanel.add(scoreTitle);
        scoresPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        scoresPanel.add(scoreTitle);
        scoresPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        scoreCards = new ArrayList<>();

        for (Player player : players) {
            ScoreCardPanel card = new ScoreCardPanel(player);
            scoreCards.add(card);
            scoresPanel.add(card);
            scoresPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        JPanel mainBg = new UnifiedBackgroundPanel(
                "C:\\Users\\echaa\\IdeaProjects\\untitled1\\images\\back_1.jpeg"
        );

        mainBg.add(boardPanel, BorderLayout.WEST);
        mainBg.add(rightPanel, BorderLayout.EAST);

        setContentPane(mainBg);

        rightPanel.add(scoresPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));




        logArea = new JTextArea(8, 22);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Game Log"));
        scrollPane.setMaximumSize(new Dimension(260, 140));

        rightPanel.add(scrollPane);
        rightPanel.add(Box.createVerticalGlue());

        updateCurrentPlayerLabel();
        setVisible(true);

        playBackgroundMusic();
    }

    private void updateCurrentPlayerLabel() {
        Player current = players.get(currentPlayerIndex);
        currentPlayerLabel.setText("Turn: " + current.getName());
        currentPlayerLabel.setForeground(current.getColor());
    }

    private void rollDice() {
        rollDiceButton.setEnabled(false);
        Player currentPlayer = players.get(currentPlayerIndex);

        AudioPlayer.playDiceSound();

        final int[] rollCount = {0};
        animationTimer = new javax.swing.Timer(80, null);

        animationTimer.addActionListener(e -> {
            rollCount[0]++;

            int randomIndex = new Random().nextInt(6);
            diceFaceLabel.setIcon(diceIcons[randomIndex]);
            diceResultLabel.setText("Rolling...");

            if (rollCount[0] >= 12) {
                animationTimer.stop();

                DiceResult result = dice.roll();
                diceFaceLabel.setIcon(diceIcons[result.getValue() - 1]);

                performDiceRoll(currentPlayer, result);
            }
        });

        animationTimer.start();
    }



    private void performDiceRoll(Player player, DiceResult result) {
        player.incrementRollCount();

        String colorText = result.isGreen() ? "GREEN " : "RED ";
        diceResultLabel.setText("Dice: " + result.getValue() + " (" + colorText + ")");

        addLog(player.getName() + " rolled dice: " + result.getValue() + " (" + colorText + ")");

        int oldPosition = player.getPosition();
        int newPosition = oldPosition;

        if (result.isGreen()) {
            newPosition += result.getValue();
            addLog("→ Move forward " + result.getValue() + " steps");
        } else {
            newPosition -= result.getValue();
            addLog("← Move backward " + result.getValue() + " steps");
        }

        if (newPosition < 0) newPosition = 0;
        if (newPosition > board.getTotalCells()) newPosition = board.getTotalCells();

        animatePlayerMovement(player, oldPosition, newPosition);
    }

    private void animatePlayerMovement(Player player, int from, int to) {
        javax.swing.Timer moveTimer = new javax.swing.Timer(300, null);
        final int[] currentPos = {from};
        final int step = (to > from) ? 1 : -1;

        moveTimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ((step > 0 && currentPos[0] >= to) || (step < 0 && currentPos[0] <= to)) {
                    moveTimer.stop();
                    player.setPosition(to);
                    checkSpecialTiles(player);
                    boardPanel.repaint();

                    if (player.getPosition() >= board.getTotalCells()) {
                        endGame(player);
                        return;
                    }

                    javax.swing.Timer nextPlayerTimer = new javax.swing.Timer(1500, evt -> {
                        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                        updateCurrentPlayerLabel();
                        diceResultLabel.setText("Press button to roll");
                        rollDiceButton.setEnabled(true);
                    });
                    nextPlayerTimer.setRepeats(false);
                    nextPlayerTimer.start();
                } else {
                    currentPos[0] += step;
                    player.setPosition(currentPos[0]);
                    boardPanel.repaint();
                }
            }
        });
        moveTimer.start();
    }

    private void checkSpecialTiles(Player player) {
        int position = player.getPosition();

        Ladder ladder = board.getLadderAt(position);
        if (ladder != null) {
            addLog("" + player.getName() + " climbed ladder " + ladder.getStart() + " → " + ladder.getEnd());
            player.setPosition(ladder.getEnd());
            player.addScore(50);
            flashMessage("LADDER!");
            AudioPlayer.playSuccessSound();
        }

        if (player.getRollCount() >= 2 && board.isPrime(position)) {
            addLog("Position " + position + " is a PRIME NUMBER!");
            Ladder nearestLadder = board.getNearestLadder(position);
            if (nearestLadder != null && nearestLadder.getStart() > position) {
                addLog(" Auto climb to ladder: " + nearestLadder.getStart() + " → " + nearestLadder.getEnd());
                player.setPosition(nearestLadder.getEnd());
                player.addScore(100);
                flashMessage("PRIME NUMBER!");
                AudioPlayer.playSuccessSound();
            }
        }

        Cell cell = board.getCell(position);
        if (cell != null && cell.getScoreBonus() > 0) {
            addLog("Score bonus: +" + cell.getScoreBonus());
            player.addScore(cell.getScoreBonus());
            flashMessage("BONUS!");
            AudioPlayer.playSuccessSound();
        }

        player.addScore(10);
        player.updateScoreLabel();

        for (ScoreCardPanel card : scoreCards) {
            card.updateScore();
        }
        updateScoreRanking(scoresPanel, scoreCards);

    }

    private void flashMessage(String message) {
        JLabel flashLabel = new JLabel(message);
        flashLabel.setFont(new Font("Arial", Font.BOLD, 48));
        flashLabel.setForeground(new Color(135, 206, 250));
        flashLabel.setBounds(boardPanel.getWidth()/2 - 150, boardPanel.getHeight()/2 - 50, 300, 100);
        flashLabel.setHorizontalAlignment(SwingConstants.CENTER);
        boardPanel.add(flashLabel);
        boardPanel.setComponentZOrder(flashLabel, 0);
        boardPanel.repaint();

        javax.swing.Timer timer = new javax.swing.Timer(1500, e -> {
            boardPanel.remove(flashLabel);
            boardPanel.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void addLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void endGame(Player winner) {
        rollDiceButton.setEnabled(false);
        addLog("\nGAME OVER!");
        addLog("WINNER: " + winner.getName());

        AudioPlayer.playWinSound();

        List<Player> sorted = new ArrayList<>(players);
        sorted.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        StringBuilder topScores = new StringBuilder("\n=== TOP SCORES ===\n");

        for (int i = 0; i < sorted.size(); i++) {
            topScores.append("Champion ").append(i + 1).append(": ").append(sorted.get(i).getName())
                    .append(" - ").append(sorted.get(i).getScore()).append(" points\n");
        }

        JOptionPane.showMessageDialog(this, topScores.toString(),
                "Game Over!", JOptionPane.INFORMATION_MESSAGE);

        AudioPlayer.stopBackgroundMusic();
    }

    private void playBackgroundMusic() {
        AudioPlayer.playBackgroundMusic();
    }
}