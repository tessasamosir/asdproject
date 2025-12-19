import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GameBoardPanel extends JPanel {
    private static final int GRID_SIZE = 8;
    private GameBoard board;
    private List<Player> players;
    private Image backgroundImage;
    private int cellSize = 90;

    public GameBoardPanel(GameBoard board, List<Player> players) {
        this.board = board;
        this.players = players;

        setPreferredSize(new Dimension(GRID_SIZE * cellSize, GRID_SIZE * cellSize));
        setMinimumSize(new Dimension(GRID_SIZE * cellSize, GRID_SIZE * cellSize));
        setOpaque(true);

        try {
            String imagePath = "C:\\Users\\echaa\\IdeaProjects\\asdproject\\images\\back.jpeg";
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int boardSize = GRID_SIZE * cellSize;

        g2d.setStroke(new BasicStroke(1.0f));
        g2d.setColor(new Color(255, 255, 255, 200));

        for (int i = 0; i <= GRID_SIZE; i++) {
            int p = i * cellSize;
            g2d.drawLine(0, p, boardSize, p);
            g2d.drawLine(p, 0, p, boardSize);
        }

        g2d.setStroke(new BasicStroke(3.0f));
        g2d.setColor(new Color(255, 255, 255, 220));

        for (Ladder ladder : board.getLadders()) {
            Point start = getPositionPoint(ladder.getStart());
            Point end = getPositionPoint(ladder.getEnd());
            g2d.drawLine(start.x, start.y, end.x, end.y);
            drawSnowflake(g2d, start.x, start.y, 12);
            drawSnowflake(g2d, end.x, end.y, 12);
        }

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                boolean ltr = row % 2 == 0;
                int actualCol = ltr ? col : (GRID_SIZE - 1 - col);
                int x = actualCol * cellSize;
                int y = (GRID_SIZE - 1 - row) * cellSize;
                int number = row * GRID_SIZE + col + 1;

                g2d.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.drawString(String.valueOf(number), x + 8, y + 20);
                g2d.setColor(Color.WHITE);
                g2d.drawString(String.valueOf(number), x + 6, y + 18);
            }
        }

        Map<Integer, List<Player>> positionMap = new HashMap<>();
        for (Player player : players) {
            if (player.getPosition() > 0) {
                positionMap.computeIfAbsent(player.getPosition(), k -> new ArrayList<>()).add(player);
            }
        }

        for (Map.Entry<Integer, List<Player>> entry : positionMap.entrySet()) {
            Point p = getPositionPoint(entry.getKey());
            List<Player> playersAtPosition = entry.getValue();
            int size = 40;

            if (playersAtPosition.size() == 1) {
                Player player = playersAtPosition.get(0);
                drawPlayer(g2d, player, p.x, p.y, size);
            } else {
                int radius = 25;
                double angleStep = 2 * Math.PI / playersAtPosition.size();
                for (int i = 0; i < playersAtPosition.size(); i++) {
                    double angle = i * angleStep;
                    int offsetX = (int)(Math.cos(angle) * radius);
                    int offsetY = (int)(Math.sin(angle) * radius);
                    drawPlayer(g2d, playersAtPosition.get(i), p.x + offsetX, p.y + offsetY, size);
                }
            }
        }

        g2d.dispose();
    }

    private void drawPlayer(Graphics2D g2d, Player player, int x, int y, int size) {
        g2d.setColor(player.getColor());
        g2d.fillOval(x - size / 2, y - size / 2, size, size);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String initial = player.getName().substring(0, 1).toUpperCase();
        g2d.drawString(initial, x - 4, y + 4);
    }


    private Point getPositionPoint(int position) {
        int index = position - 1;
        int row = index / GRID_SIZE;
        int col = index % GRID_SIZE;
        boolean ltr = row % 2 == 0;
        int actualCol = ltr ? col : (GRID_SIZE - 1 - col);

        int x = actualCol * cellSize + cellSize / 2;
        int y = (GRID_SIZE - 1 - row) * cellSize + cellSize / 2;
        return new Point(x, y);
    }

    private void drawSnowflake(Graphics2D g2d, int cx, int cy, int size) {
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(new Color(255, 255, 255, 230));

        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(i * 60);
            int x1 = cx + (int)(Math.cos(angle) * size);
            int y1 = cy + (int)(Math.sin(angle) * size);
            g2d.drawLine(cx, cy, x1, y1);

            int halfSize = size / 2;
            int x2 = cx + (int)(Math.cos(angle) * halfSize);
            int y2 = cy + (int)(Math.sin(angle) * halfSize);

            double angle1 = angle + Math.toRadians(30);
            double angle2 = angle - Math.toRadians(30);

            int branch1x = x2 + (int)(Math.cos(angle1) * (size / 3));
            int branch1y = y2 + (int)(Math.sin(angle1) * (size / 3));
            g2d.drawLine(x2, y2, branch1x, branch1y);

            int branch2x = x2 + (int)(Math.cos(angle2) * (size / 3));
            int branch2y = y2 + (int)(Math.sin(angle2) * (size / 3));
            g2d.drawLine(x2, y2, branch2x, branch2y);
        }
    }
}