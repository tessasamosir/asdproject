package Maze;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


class MazePanel extends JPanel {
    private int[][] maze;
    private TerrainType[][] terrain;
    private Position start;
    private List<Position> destinations;
    private Position currentGoal;
    private List<Position> path;
    private Set<Position> visited;
    private int cellSize = 25;
    private Position playerPos;
    private boolean showPath = false;
    private int animationFrame = 0;

    private final Color WALL_COLOR = new Color(78, 52, 46);
    private final Color VISITED_COLOR = new Color(100, 181, 246, 120);
    private final Color PATH_COLOR = new Color(255, 235, 59);

    MazePanel(int[][] maze, TerrainType[][] terrain) {
        this.maze = maze;
        this.terrain = terrain;
        this.destinations = new ArrayList<>();
        this.path = new ArrayList<>();
        this.visited = new HashSet<>();
        setPreferredSize(new Dimension(maze[0].length * cellSize + 200, maze.length * cellSize + 100));
        setBackground(new Color(129, 199, 132));

        Timer animTimer = new Timer(100, e -> {
            animationFrame++;
            repaint();
        });
        animTimer.start();
    }

    void setStart(Position start) {
        this.start = start;
        this.playerPos = start;
    }

    void setDestinations(List<Position> destinations) {
        this.destinations = destinations;
    }

    void setCurrentGoal(Position goal) {
        this.currentGoal = goal;
    }

    void setPath(List<Position> path, Set<Position> visited) {
        this.path = path;
        this.visited = visited;
        this.showPath = true;
        repaint();
    }

    void animatePath() {
        if (path.isEmpty()) return;
        Timer timer = new Timer(150, new ActionListener() {
            int index = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < path.size()) {
                    playerPos = path.get(index);
                    index++;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offsetX = 50;
        int offsetY = 50;

        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRoundRect(offsetX + 5, offsetY + 5, maze[0].length * cellSize, maze.length * cellSize, 20, 20);

        g2.setColor(new Color(62, 39, 35));
        g2.setStroke(new BasicStroke(8));
        g2.drawRoundRect(offsetX - 4, offsetY - 4, maze[0].length * cellSize + 8, maze.length * cellSize + 8, 20, 20);

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                int x = offsetX + j * cellSize;
                int y = offsetY + i * cellSize;

                if (maze[i][j] == 1) {
                    drawWall(g2, x, y);
                } else {
                    drawTerrain(g2, x, y, terrain[i][j]);
                }
            }
        }

        if (showPath) {
            g2.setColor(VISITED_COLOR);
            for (Position pos : visited) {
                int x = offsetX + pos.col * cellSize;
                int y = offsetY + pos.row * cellSize;
                g2.fillOval(x + 4, y + 4, cellSize - 8, cellSize - 8);
            }

            g2.setColor(PATH_COLOR);
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < path.size() - 1; i++) {
                Position p1 = path.get(i);
                Position p2 = path.get(i + 1);

                int x1 = offsetX + p1.col * cellSize + cellSize / 2;
                int y1 = offsetY + p1.row * cellSize + cellSize / 2;
                int x2 = offsetX + p2.col * cellSize + cellSize / 2;
                int y2 = offsetY + p2.row * cellSize + cellSize / 2;

                g2.drawLine(x1, y1, x2, y2);

                double angle = Math.atan2(y2 - y1, x2 - x1);
                int arrowSize = 8;
                int[] xPoints = {x2,
                        x2 - (int) (arrowSize * Math.cos(angle - Math.PI / 6)),
                        x2 - (int) (arrowSize * Math.cos(angle + Math.PI / 6))};
                int[] yPoints = {y2,
                        y2 - (int) (arrowSize * Math.sin(angle - Math.PI / 6)),
                        y2 - (int) (arrowSize * Math.sin(angle + Math.PI / 6))};
                g2.fillPolygon(xPoints, yPoints, 3);
            }
        }

        if (start != null) {
            int x = offsetX + start.col * cellSize;
            int y = offsetY + start.row * cellSize;
            drawFlag(g2, x + cellSize / 2, y + cellSize / 2, Color.GREEN);
        }

        for (int i = 0; i < destinations.size(); i++) {
            Position dest = destinations.get(i);
            int x = offsetX + dest.col * cellSize + cellSize / 2;
            int y = offsetY + dest.row * cellSize + cellSize / 2;

            switch (i) {
                case 0 -> drawCastle(g2, x, y);
                case 1 -> drawCave(g2, x, y);
                case 2 -> drawTreasure(g2, x, y);
            }
        }

        if (playerPos != null) {
            int x = offsetX + playerPos.col * cellSize + cellSize / 2;
            int y = offsetY + playerPos.row * cellSize + cellSize / 2;
            drawPlayer(g2, x, y);
        }

        drawLegend(g2, offsetX + maze[0].length * cellSize + 20, offsetY);
        drawCompass(g2, offsetX + maze[0].length * cellSize + 20, offsetY + maze.length * cellSize - 120);

        g2.dispose();
    }

    private void drawWall(Graphics2D g2, int x, int y) {
        GradientPaint gradient = new GradientPaint(x, y, new Color(78, 52, 46), x, y + cellSize, new Color(62, 39, 35));
        g2.setPaint(gradient);
        g2.fillRect(x, y, cellSize, cellSize);

        Random rand = new Random((long) x * 1000 + y);
        if (rand.nextFloat() > 0.5) {
            drawTree(g2, x + cellSize / 2, y + cellSize / 2, rand.nextInt(3));
        }
    }

    private void drawTree(Graphics2D g2, int x, int y, int variant) {
        g2.setColor(new Color(101, 67, 33));
        g2.fillRect(x - 2, y, 4, 8);

        g2.setColor(new Color(56, 142, 60));
        switch (variant) {
            case 0 -> g2.fillOval(x - 6, y - 8, 12, 12);
            case 1 -> {
                int[] xp = {x, x - 7, x + 7};
                int[] yp = {y - 10, y + 2, y + 2};
                g2.fillPolygon(xp, yp, 3);
            }
            case 2 -> {
                g2.fillOval(x - 5, y - 6, 10, 10);
                g2.fillOval(x - 3, y - 9, 6, 6);
            }
        }

        g2.setColor(new Color(139, 195, 74, 150));
        g2.fillOval(x - 3, y - 8, 4, 4);
    }

    private void drawTerrain(Graphics2D g2, int x, int y, TerrainType type) {
        g2.setColor(type.color);
        g2.fillRect(x, y, cellSize, cellSize);

        Random rand = new Random((long) x * 1000 + y);
        g2.setColor(type.color.darker());

        switch (type) {
            case GRASS -> {
                for (int i = 0; i < 3; i++) {
                    int gx = x + rand.nextInt(cellSize);
                    int gy = y + rand.nextInt(cellSize);
                    g2.drawLine(gx, gy, gx, gy + 2);
                }
            }
            case MUD -> {
                for (int i = 0; i < 2; i++) {
                    int mx = x + rand.nextInt(cellSize - 4);
                    int my = y + rand.nextInt(cellSize - 4);
                    g2.fillOval(mx, my, 4, 3);
                }
            }
            case WATER -> {
                g2.setColor(new Color(255, 255, 255, 100));
                int waveY = y + cellSize / 2 + (int) (Math.sin(animationFrame * 0.3 + x) * 2);
                g2.drawLine(x + 2, waveY, x + cellSize - 2, waveY);
            }
        }

        g2.setColor(type.color.darker());
        g2.drawRect(x, y, cellSize, cellSize);
    }

    private void drawFlag(Graphics2D g2, int x, int y, Color color) {
        g2.setColor(new Color(90, 90, 90));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x, y, x, y - 20);

        int[] xPoints = {x, x + 18, x + 18, x};
        int[] yPoints = {y - 20, y - 18, y - 10, y - 12};
        g2.setColor(color);
        g2.fillPolygon(xPoints, yPoints, 4);

        g2.setColor(color.darker());
        g2.drawPolygon(xPoints, yPoints, 4);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Verdana", Font.BOLD, 7));
        g2.drawString("S", x + 4, y - 13);
    }

    private void drawCastle(Graphics2D g2, int x, int y) {
        float bounce = (float) (Math.sin(animationFrame * 0.2) * 2);
        y += bounce;

        g2.setColor(new Color(120, 120, 120));
        g2.fillRect(x - 12, y - 8, 24, 16);

        g2.fillRect(x - 14, y - 12, 6, 12);
        g2.fillRect(x + 8, y - 12, 6, 12);

        g2.setColor(new Color(244, 67, 54));
        int[] xp1 = {x - 14, x - 8, x - 11};
        int[] yp1 = {y - 12, y - 12, y - 18};
        g2.fillPolygon(xp1, yp1, 3);

        int[] xp2 = {x + 8, x + 14, x + 11};
        int[] yp2 = {y - 12, y - 12, y - 18};
        g2.fillPolygon(xp2, yp2, 3);

        g2.setColor(new Color(62, 39, 35));
        g2.fillRect(x - 4, y + 2, 8, 6);

        g2.setColor(new Color(255, 235, 59));
        g2.fillRect(x - 8, y - 4, 4, 4);
        g2.fillRect(x + 4, y - 4, 4, 4);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Verdana", Font.BOLD, 8));
        g2.drawString("1", x - 2, y + 20);
    }

    private void drawCave(Graphics2D g2, int x, int y) {
        float bounce = (float) (Math.sin(animationFrame * 0.2 + 1) * 2);
        y += bounce;

        g2.setColor(new Color(90, 90, 90));
        int[] xPoints = {x, x - 16, x + 16};
        int[] yPoints = {y - 15, y + 8, y + 8};
        g2.fillPolygon(xPoints, yPoints, 3);

        g2.setColor(new Color(30, 30, 30));
        g2.fillArc(x - 10, y - 2, 20, 16, 0, 180);

        g2.setColor(new Color(120, 120, 120));
        for (int i = -6; i <= 6; i += 6) {
            int[] xp = {x + i - 2, x + i + 2, x + i};
            int[] yp = {y - 2, y - 2, y + 2};
            g2.fillPolygon(xp, yp, 3);
        }

        float alpha = 0.3f + (float) (Math.sin(animationFrame * 0.3) * 0.2);
        g2.setColor(new Color(156, 39, 176, (int) (alpha * 255)));
        g2.fillOval(x - 6, y + 2, 12, 8);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Verdana", Font.BOLD, 8));
        g2.drawString("3", x - 2, y + 20);
    }

    private void drawPlayer(Graphics2D g2, int x, int y) {
        int bounce = (int) (Math.sin(animationFrame * 0.3) * 2);
        y += bounce;

        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillOval(x - 10, y + 8, 20, 8);

        GradientPaint gradient = new GradientPaint(x, y - 10, new Color(33, 150, 243), x, y + 5, new Color(25, 118, 210));
        g2.setPaint(gradient);

        int[] xPoints = {x - 8, x + 8, x + 6, x - 6};
        int[] yPoints = {y - 5, y - 5, y + 5, y + 5};
        g2.fillPolygon(xPoints, yPoints, 4);

        g2.setColor(new Color(100, 200, 255));
        g2.fillRect(x - 5, y - 3, 10, 4);

        g2.setColor(Color.BLACK);
        g2.fillOval(x - 7, y + 3, 4, 4);
        g2.fillOval(x + 3, y + 3, 4, 4);

        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillOval(x - 6, y - 8, 4, 4);
    }

    private void drawLegend(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillRoundRect(x, y, 150, 280, 15, 15);

        g2.setColor(new Color(62, 39, 35));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, 150, 280, 15, 15);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Verdana", Font.BOLD, 14));
        g2.drawString("LEGENDA", x + 40, y + 25);

        g2.setFont(new Font("Verdana", Font.PLAIN, 10));
        int yOffset = y + 45;

        g2.setColor(TerrainType.GRASS.color);
        g2.fillRect(x + 10, yOffset, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawString("Rumput (1s)", x + 35, yOffset + 15);

        yOffset += 25;
        g2.setColor(TerrainType.MUD.color);
        g2.fillRect(x + 10, yOffset, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawString("Lumpur (5s)", x + 35, yOffset + 15);

        yOffset += 25;
        g2.setColor(TerrainType.WATER.color);
        g2.fillRect(x + 10, yOffset, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawString("Air (10s)", x + 35, yOffset + 15);

        yOffset += 25;
        g2.setColor(WALL_COLOR);
        g2.fillRect(x + 10, yOffset, 20, 20);
        drawTree(g2, x + 20, yOffset + 10, 0);
        g2.setColor(Color.BLACK);
        g2.drawString("Hutan", x + 35, yOffset + 15);

        yOffset += 30;
        g2.drawLine(x + 10, yOffset, x + 140, yOffset);

        yOffset += 15;
        g2.setFont(new Font("Verdana", Font.BOLD, 11));
        g2.drawString("DESTINASI:", x + 10, yOffset);

        yOffset += 25;
        drawCastle(g2, x + 20, yOffset);
        g2.setFont(new Font("Verdana", Font.PLAIN, 10));
        g2.drawString("Istana", x + 35, yOffset + 5);

        yOffset += 30;
        drawCave(g2, x + 20, yOffset);
        g2.drawString("Gua Penyihir", x + 35, yOffset + 5);

        yOffset += 30;
        drawTreasure(g2, x + 20, yOffset);
        g2.drawString("Harta Karun", x + 35, yOffset + 5);

        yOffset += 30;
        g2.drawLine(x + 10, yOffset, x + 140, yOffset);

        yOffset += 20;
        g2.setColor(VISITED_COLOR);
        g2.fillOval(x + 10, yOffset, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawString("Dikunjungi", x + 35, yOffset + 15);

        yOffset += 25;
        g2.setColor(PATH_COLOR);
        g2.fillRect(x + 10, yOffset + 5, 20, 10);
        g2.setColor(Color.BLACK);
        g2.drawString("Rute", x + 35, yOffset + 15);
    }

    private void drawCompass(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillOval(x, y, 80, 80);

        g2.setColor(new Color(62, 39, 35));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(x, y, 80, 80);

        int cx = x + 40;
        int cy = y + 40;

        int[] xPoints = {cx, cx - 8, cx, cx + 8};
        int[] yPoints = {cy - 25, cy + 5, cy, cy + 5};
        g2.setColor(new Color(244, 67, 54));
        g2.fillPolygon(xPoints, yPoints, 4);

        int[] xPoints2 = {cx, cx - 8, cx, cx + 8};
        int[] yPoints2 = {cy + 25, cy - 5, cy, cy - 5};
        g2.setColor(new Color(200, 200, 200));
        g2.fillPolygon(xPoints2, yPoints2, 4);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Verdana", Font.BOLD, 12));
        g2.drawString("U", cx - 4, cy - 28);
        g2.drawString("S", cx - 4, cy + 35);
        g2.drawString("T", cx + 25, cy + 5);
        g2.drawString("B", cx - 32, cy + 5);
    }

    // drawTreasure is referenced but not defined in your original snippet.
    // Implement it to avoid compile errors.
    private void drawTreasure(Graphics2D g2, int x, int y) {
        float bounce = (float) (Math.sin(animationFrame * 0.25 + 2) * 2);
        y += bounce;

        g2.setColor(new Color(212, 175, 55)); // gold
        g2.fillArc(x - 10, y - 8, 20, 16, 0, 180);

        g2.setColor(new Color(184, 134, 11));
        g2.drawArc(x - 10, y - 8, 20, 16, 0, 180);

        g2.setColor(new Color(255, 215, 0));
        g2.fillOval(x - 3, y - 4, 6, 4);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Verdana", Font.BOLD, 8));
        g2.drawString("2", x - 2, y + 20);
    }
}
