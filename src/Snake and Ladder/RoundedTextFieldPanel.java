import javax.swing.*;
import java.awt.*;

    class RoundedTextFieldPanel extends JPanel {
        private int radius;

        public RoundedTextFieldPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(255, 255, 255, 190));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(new Color(255, 255, 255, 160));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
