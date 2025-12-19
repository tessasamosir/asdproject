import javax.swing.*;
import java.awt.*;

    class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            setBackground(bg);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(255, 255, 255, 200));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(
                    1, 1,
                    getWidth() - 3,
                    getHeight() - 3,
                    radius,
                    radius
            );

            g2.dispose();
            super.paintComponent(g);
        }

    }
