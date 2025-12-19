import javax.swing.*;
import java.awt.*;

public class ScoreCardPanel {
    class ScoreCardPanel extends RoundedPanel {
        private JLabel nameLabel;
        private JLabel scoreLabel;
        private Player player;

        public ScoreCardPanel(Player player) {
            super(20, Color.WHITE);
            this.player = player;

            setLayout(new BorderLayout());
            setBackground(new Color(255, 255, 255, 220));
            setMaximumSize(new Dimension(240, 45));
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            nameLabel = new JLabel(player.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
            nameLabel.setForeground(player.getColor());

            scoreLabel = new JLabel("0", SwingConstants.RIGHT);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 13));

            add(nameLabel, BorderLayout.WEST);
            add(scoreLabel, BorderLayout.EAST);
        }

        public int getScore() {
            return player.getScore();
        }

        public void updateScore() {
            scoreLabel.setText(String.valueOf(player.getScore()));
        }
    }
}
