package Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

// Kelas utama untuk menjalankan game dengan welcome screen
public class MazeGame extends JFrame {
    private int[][] maze;
    private TerrainType[][] terrain;
    private Position start;
    private List<Position> destinations;
    private MazePanel mazePanel;

    // GUI controls
    private JComboBox<String> algorithmBox;
    private JComboBox<String> destinationBox;
    private Button3D startButton;
    private Button3D resetButton;
    private Button3D newMazeButton;
    private JTextArea infoArea;
    private BackgroundSound bgSound;

    public MazeGame() {
        setTitle("Maze Adventure - Peta Pencarian Jalan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Tampilkan welcome screen dulu
        WelcomePanel welcomePanel = new WelcomePanel(() -> {
            // Setelah klik START, hapus welcome dan tampilkan game
            getContentPane().removeAll();
            initializeGame();
            revalidate();
            repaint();
        });

        add(welcomePanel);
        setVisible(true);
    }

    private void initializeGame() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(165, 214, 167));

        bgSound = new BackgroundSound();
        bgSound.play("SoundMaze/Mario Bros.wav", true);

        MazeGenerator generator = new MazeGenerator(25, 35);
        maze = generator.generate();
        terrain = generator.generateTerrain();

        setupPositions();

        mazePanel = new MazePanel(maze, terrain);
        mazePanel.setStart(start);
        mazePanel.setDestinations(destinations);

        JScrollPane scrollPane = new JScrollPane(mazePanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        controlPanel.setBackground(new Color(66, 66, 66));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel algoLabel = new JLabel("Algoritma:");
        algoLabel.setForeground(Color.WHITE);
        algoLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        controlPanel.add(algoLabel);

        algorithmBox = new JComboBox<>(new String[]{
                "BFS - Breadth First Search",
                "DFS - Depth First Search",
                "A* - A-Star Algorithm"
        });
        algorithmBox.setFont(new Font("Verdana", Font.PLAIN, 13));
        algorithmBox.setPreferredSize(new Dimension(220, 35));
        controlPanel.add(algorithmBox);

        controlPanel.add(Box.createHorizontalStrut(20));

        JLabel destLabel = new JLabel("Tujuan:");
        destLabel.setForeground(Color.WHITE);
        destLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        controlPanel.add(destLabel);

        destinationBox = new JComboBox<>(new String[]{
                "Istana (Merah)",
                "Gua Penyihir (Ungu)",
                "Harta Karun (Emas)"
        });
        destinationBox.setFont(new Font("Verdana", Font.PLAIN, 13));
        destinationBox.setPreferredSize(new Dimension(180, 35));
        controlPanel.add(destinationBox);

        controlPanel.add(Box.createHorizontalStrut(20));

        startButton = new Button3D("MULAI PENCARIAN", new Color(76, 175, 80));
        startButton.setPreferredSize(new Dimension(200, 45));
        startButton.addActionListener(e -> startSearch());
        controlPanel.add(startButton);

        resetButton = new Button3D("RESET", new Color(255, 152, 0));
        resetButton.setPreferredSize(new Dimension(120, 45));
        resetButton.addActionListener(e -> resetMaze());
        controlPanel.add(resetButton);

        newMazeButton = new Button3D("MAZE BARU", new Color(33, 150, 243));
        newMazeButton.setPreferredSize(new Dimension(150, 45));
        newMazeButton.addActionListener(e -> generateNewMaze());
        controlPanel.add(newMazeButton);

        add(controlPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(250, 250, 250));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, new Color(66, 66, 66)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel infoTitle = new JLabel("INFORMASI PENCARIAN");
        infoTitle.setFont(new Font("Verdana", Font.BOLD, 14));
        infoTitle.setForeground(new Color(66, 66, 66));
        infoPanel.add(infoTitle, BorderLayout.NORTH);

        infoArea = new JTextArea(5, 60);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        infoArea.setBackground(new Color(245, 245, 245));
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoArea.setText(
                "CARA BERMAIN:\n" +
                        "1. Pilih algoritma pencarian (BFS, DFS, atau A*)\n" +
                        "2. Pilih destinasi: Istana, Gua Penyihir, atau Harta Karun\n" +
                        "3. Klik MULAI PENCARIAN untuk melihat visualisasi\n\n" +
                        "TERRAIN: Rumput (1 detik), Lumpur (5 detik), Air (10 detik)"
        );

        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setBorder(BorderFactory.createEmptyBorder());
        infoPanel.add(infoScroll, BorderLayout.CENTER);

        add(infoPanel, BorderLayout.SOUTH);

        pack();
    }

    private void setupPositions() {
        destinations = new ArrayList<>();
        Random rand = new Random();

        start = findValidPosition(rand, 1, 5, 1, 5);
        destinations.add(findValidPosition(rand, 1, 5, maze[0].length - 6, maze[0].length - 2));
        destinations.add(findValidPosition(rand, maze.length - 6, maze.length - 2, 1, 5));
        destinations.add(findValidPosition(rand, maze.length - 6, maze.length - 2, maze[0].length - 6, maze[0].length - 2));
    }

    private Position findValidPosition(Random rand, int minRow, int maxRow, int minCol, int maxCol) {
        Position pos;
        do {
            int row = rand.nextInt(maxRow - minRow) + minRow;
            int col = rand.nextInt(maxCol - minCol) + minCol;
            pos = new Position(row, col);
        } while (maze[pos.row][pos.col] != 0);
        return pos;
    }

    private void startSearch() {
        int algoIndex = algorithmBox.getSelectedIndex();
        int destIndex = destinationBox.getSelectedIndex();

        Position goal = destinations.get(destIndex);
        mazePanel.setCurrentGoal(goal);

        PathFinder finder;
        String algoName;
        String algoDesc;

        switch (algoIndex) {
            case 0 -> {
                finder = new BFSFinder(maze, terrain, start, goal);
                algoName = "BFS (Breadth-First Search)";
                algoDesc = "Menjelajah level demi level, menjamin path terpendek";
            }
            case 1 -> {
                finder = new DFSFinder(maze, terrain, start, goal);
                algoName = "DFS (Depth-First Search)";
                algoDesc = "Menjelajah sedalam mungkin terlebih dahulu";
            }
            case 2 -> {
                finder = new AStarFinder(maze, terrain, start, goal);
                algoName = "A* (A-Star)";
                algoDesc = "Menggunakan heuristic dan mempertimbangkan terrain cost";
            }
            default -> { return; }
        }

        long startTime = System.currentTimeMillis();
        boolean found = finder.findPath();
        long endTime = System.currentTimeMillis();

        if (found) {
            List<Position> path = finder.getPath();
            Set<Position> visited = finder.getVisited();
            int totalCost = finder.getTotalCost();

            mazePanel.setPath(path, visited);
            mazePanel.animatePath();

            String[] destNames = {"Istana", "Gua Penyihir", "Harta Karun"};

            infoArea.setText(String.format(
                    "PENCARIAN BERHASIL!\n\n" +
                            "Algoritma: %s\n" +
                            "   %s\n\n" +
                            "Destinasi: %s\n" +
                            "Panjang Path: %d langkah\n" +
                            "Sel Dikunjungi: %d sel\n" +
                            "Waktu Algoritma: %d ms\n\n" +
                            "WAKTU PERJALANAN:\n" +
                            "Total: %d detik\n" +
                            "Efisiensi: %.2f%%",
                    algoName, algoDesc, destNames[destIndex], path.size(),
                    visited.size(), endTime - startTime,
                    totalCost, (path.size() * 100.0 / visited.size())
            ));
        } else {
            infoArea.setText(
                    "PATH TIDAK DITEMUKAN!\n\n" +
                            "Tidak ada jalur yang tersedia ke destinasi tersebut.\n" +
                            "Coba generate maze baru atau pilih destinasi lain."
            );
        }
    }

    private void resetMaze() {
        mazePanel.setPath(new ArrayList<>(), new HashSet<>());
        mazePanel.setStart(start);
        mazePanel.repaint();

        infoArea.setText(
                "MAZE DIRESET!\n\n" +
                        "Maze telah dikembalikan ke kondisi awal.\n" +
                        "Silakan pilih algoritma dan destinasi untuk memulai pencarian baru."
        );
    }

    private void generateNewMaze() {
        MazeGenerator generator = new MazeGenerator(25, 35);
        maze = generator.generate();
        terrain = generator.generateTerrain();
        setupPositions();

        mazePanel = new MazePanel(maze, terrain);
        mazePanel.setStart(start);
        mazePanel.setDestinations(destinations);

        // Update viewport ke panel baru
        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JScrollPane sp) {
                sp.setViewportView(mazePanel);
                break;
            }
        }

        infoArea.setText(
                "MAZE BARU TELAH DIBUAT!\n\n" +
                        "Maze baru dengan layout berbeda telah di-generate.\n" +
                        "Terrain dan posisi destinasi juga telah diperbarui.\n" +
                        "Selamat menjelajah!"
        );
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new MazeGame());
    }
}