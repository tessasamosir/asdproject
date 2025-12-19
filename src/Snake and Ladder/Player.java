import javax.swing.*;
import java.awt.*;

class Player {
    private String name;
    private int position;
    private int score;
    private int rollCount;
    private Color color;
    private JLabel scoreLabel;
    private int playerIndex;

    public Player(String name, int index, String imagePath) {
        this.name = name;
        this.position = 0;
        this.score = 0;
        this.rollCount = 0;
        this.playerIndex = index;

        Color[] colors = {
                new Color(135, 206, 250),
                new Color(255, 182, 193),
                new Color(255, 255, 255),
                new Color(139, 69, 19),
                new Color(255, 215, 0),
                new Color(147, 112, 219),
                new Color(50, 205, 50),
                new Color(255, 140, 0)
        };
        this.color = colors[index % colors.length];
    }

    public String getName() { return name; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public int getScore() { return score; }
    public void addScore(int points) { this.score += points; }
    public int getRollCount() { return rollCount; }
    public void incrementRollCount() { this.rollCount++; }
    public Color getColor() { return color; }
    public int getPlayerIndex() { return playerIndex; }

    public void setScoreLabel(JLabel label) { this.scoreLabel = label; }
    public void updateScoreLabel() {
        if (scoreLabel != null) {
            scoreLabel.setText(name + ": " + score);
        }
    }
}
