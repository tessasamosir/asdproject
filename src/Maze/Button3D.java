package Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

class Button3D extends JButton {
    private Color topColor;
    private Color baseColor;
    private boolean isPressed = false;

    public Button3D(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.topColor = baseColor.brighter();

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Verdana", Font.BOLD, 14));
        setForeground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offset = isPressed ? 4 : 0;
        int shadowDepth = isPressed ? 2 : 6;

        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(4, 4 + shadowDepth, getWidth() - 8, getHeight() - 8, 15, 15);

        g2.setColor(baseColor.darker());
        g2.fillRoundRect(0, offset + shadowDepth - 2, getWidth(), getHeight() - shadowDepth, 15, 15);

        GradientPaint gradient = new GradientPaint(
                0, offset, topColor,
                0, getHeight() - shadowDepth + offset, baseColor
        );
        g2.setPaint(gradient);
        g2.fillRoundRect(0, offset, getWidth(), getHeight() - shadowDepth, 15, 15);

        g2.setColor(new Color(255, 255, 255, 100));
        g2.fillRoundRect(5, offset + 5, getWidth() - 10, (getHeight() - shadowDepth) / 3, 10, 10);

        g2.dispose();
        super.paintComponent(g);
    }
}

