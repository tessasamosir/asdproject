package Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Kelas WelcomePanel untuk layar pembuka game
class WelcomePanel extends JPanel {
    private float titleY = -100;
    private float titleAlpha = 0f;
    private float buttonY = 600;
    private float buttonAlpha = 0f;
    private int particleFrame = 0;
    private List<Particle> particles = new ArrayList<>();
    private Timer animTimer;
    private JButton startBtn;
    private List<FloatingBlock> blocks = new ArrayList<>();

    // Inner class untuk partikel animasi
    class Particle {
        float x, y, vx, vy;
        Color color;
        int life;

        Particle(float x, float y) {
            this.x = x;
            this.y = y;
            this.vx = (float)(Math.random() - 0.5) * 2;
            this.vy = (float)(Math.random() - 0.5) * 2;
            this.color = new Color(
                    100 + (int)(Math.random() * 155),
                    100 + (int)(Math.random() * 155),
                    100 + (int)(Math.random() * 155)
            );
            this.life = 100;
        }

        void update() {
            x += vx;
            y += vy;
            life--;
        }

        boolean isDead() {
            return life <= 0;
        }
    }

    // Inner class untuk block yang melayang
    class FloatingBlock {
        float x, y, size, rotation, rotSpeed, floatOffset;
        Color color;

        FloatingBlock(float x, float y, float size, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.rotation = (float)(Math.random() * 360);
            this.rotSpeed = (float)(Math.random() * 2 - 1);
            this.floatOffset = (float)(Math.random() * Math.PI * 2);
        }

        void update(int frame) {
            rotation += rotSpeed;
            y = y + (float)Math.sin(frame * 0.05 + floatOffset) * 0.5f;
        }
    }

    public WelcomePanel(Runnable onStart) {
        setLayout(null);
        setBackground(new Color(135, 206, 235));

        // Inisialisasi floating blocks
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            float x = rand.nextFloat() * 800;
            float y = rand.nextFloat() * 600;
            float size = 30 + rand.nextFloat() * 40;
            Color[] blockColors = {
                    new Color(139, 195, 74),
                    new Color(141, 110, 99),
                    new Color(158, 158, 158),
                    new Color(121, 85, 72)
            };
            blocks.add(new FloatingBlock(x, y, size, blockColors[rand.nextInt(blockColors.length)]));
        }

        // Tombol start dengan style custom
        startBtn = new JButton("START ADVENTURE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(60, 120, 60));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(100, 200, 100));
                } else {
                    g2.setColor(new Color(80, 160, 80));
                }

                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(150, 255, 150, 100));
                g2.fillRect(4, 4, getWidth() - 8, getHeight() / 3);

                g2.setColor(new Color(40, 80, 40));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.drawRect(2, 2, getWidth() - 5, getHeight() - 5);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        startBtn.setFont(new Font("Verdana", Font.BOLD, 24));
        startBtn.setForeground(Color.WHITE);
        startBtn.setBounds(300, 400, 400, 80);
        startBtn.setFocusPainted(false);
        startBtn.setContentAreaFilled(false);
        startBtn.setBorderPainted(false);
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startBtn.setVisible(false);

        startBtn.addActionListener(e -> {
            animTimer.stop();
            onStart.run();
        });

        add(startBtn);

        // Timer untuk animasi
        animTimer = new Timer(16, e -> {
            particleFrame++;

            if (titleY < 150) {
                titleY += 3;
                titleAlpha = Math.min(1f, titleAlpha + 0.02f);
            }

            if (titleY >= 140) {
                if (buttonY > 400) {
                    buttonY -= 5;
                    buttonAlpha = Math.min(1f, buttonAlpha + 0.03f);
                } else {
                    startBtn.setVisible(true);
                }
            }

            for (FloatingBlock block : blocks) {
                block.update(particleFrame);
            }

            if (particleFrame % 10 == 0) {
                particles.add(new Particle(
                        (float)(Math.random() * getWidth()),
                        (float)(Math.random() * getHeight())
                ));
            }

            particles.removeIf(Particle::isDead);
            for (Particle p : particles) {
                p.update();
            }

            repaint();
        });

        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Gradient sky background
        GradientPaint sky = new GradientPaint(
                0, 0, new Color(135, 206, 235),
                0, getHeight(), new Color(176, 224, 230)
        );
        g2.setPaint(sky);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Gambar floating blocks dengan efek 3D
        for (FloatingBlock block : blocks) {
            g2.translate(block.x, block.y);
            g2.rotate(Math.toRadians(block.rotation));

            g2.setColor(block.color);
            g2.fillRect(-(int)block.size/2, -(int)block.size/2, (int)block.size, (int)block.size);

            // Top face
            g2.setColor(block.color.brighter());
            int[] xp = {-(int)block.size/2, 0, (int)block.size/2, 0};
            int[] yp = {-(int)block.size/2, -(int)block.size/2 - 10, -(int)block.size/2, -(int)block.size/2 + 10};
            g2.fillPolygon(xp, yp, 4);

            // Side face
            g2.setColor(block.color.darker());
            int[] xp2 = {(int)block.size/2, (int)block.size/2 + 10, (int)block.size/2 + 10, (int)block.size/2};
            int[] yp2 = {-(int)block.size/2, -(int)block.size/2 + 10, (int)block.size/2 + 10, (int)block.size/2};
            g2.fillPolygon(xp2, yp2, 4);

            g2.rotate(-Math.toRadians(block.rotation));
            g2.translate(-block.x, -block.y);
        }

        // Gambar partikel
        for (Particle p : particles) {
            int alpha = Math.min(255, p.life * 2);
            g2.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), alpha));
            g2.fillOval((int)p.x, (int)p.y, 4, 4);
        }

        // Gambar title dengan animasi
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, titleAlpha));

        // Shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.setFont(new Font("Verdana", Font.BOLD, 72));
        String title = "MAZE ADVENTURE";
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, (getWidth() - titleWidth) / 2 + 4, (int)titleY + 4);

        // Main title dengan gradient
        GradientPaint titleGradient = new GradientPaint(
                0, titleY - 50, new Color(255, 215, 0),
                0, titleY + 20, new Color(218, 165, 32)
        );
        g2.setPaint(titleGradient);
        g2.drawString(title, (getWidth() - titleWidth) / 2, (int)titleY);

        // Outline
        g2.setColor(new Color(139, 69, 19));
        g2.setStroke(new BasicStroke(3));
        for (int dx = -2; dx <= 2; dx += 4) {
            for (int dy = -2; dy <= 2; dy += 4) {
                if (dx != 0 || dy != 0) {
                    g2.drawString(title, (getWidth() - titleWidth) / 2 + dx, (int)titleY + dy);
                }
            }
        }

        // Subtitle
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, titleAlpha * 0.8f));
        g2.setFont(new Font("Verdana", Font.PLAIN, 24));
        g2.setColor(new Color(70, 70, 70));
        String subtitle = "Explore • Discover • Conquer";
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.drawString(subtitle, (getWidth() - subtitleWidth) / 2, (int)titleY + 50);

        // Info text
        if (buttonAlpha > 0.5f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, buttonAlpha));
            g2.setFont(new Font("Courier New", Font.PLAIN, 18));
            g2.setColor(new Color(100, 100, 100));
            String info = "Gunakan algoritma BFS, DFS, atau A* untuk menemukan jalur terbaik";
            int infoWidth = g2.getFontMetrics().stringWidth(info);
            g2.drawString(info, (getWidth() - infoWidth) / 2, 500);
        }

        g2.dispose();
    }
}
