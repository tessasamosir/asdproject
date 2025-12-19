package Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class MazeGenerator {
    private int rows, cols;
    private int[][] maze;
    private TerrainType[][] terrain;
    private Random rand;

    MazeGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
        this.terrain = new TerrainType[rows][cols];
        this.rand = new Random();
    }

    int[][] generate() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(maze[i], 1);
        }

        int startRow = rand.nextInt(rows / 2) * 2 + 1;
        int startCol = rand.nextInt(cols / 2) * 2 + 1;
        maze[startRow][startCol] = 0;

        List<int[]> walls = new ArrayList<>();
        addWalls(startRow, startCol, walls);

        while (!walls.isEmpty()) {
            int idx = rand.nextInt(walls.size());
            int[] wall = walls.remove(idx);
            int r = wall[0], c = wall[1];

            if (maze[r][c] == 1) {
                int count = countAdjacentCells(r, c);
                if (count == 1) {
                    maze[r][c] = 0;
                    addWalls(r, c, walls);
                }
            }
        }
        return maze;
    }

    TerrainType[][] generateTerrain() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 0) {
                    double r = rand.nextDouble();
                    if (r < 0.6) terrain[i][j] = TerrainType.GRASS;
                    else if (r < 0.85) terrain[i][j] = TerrainType.MUD;
                    else terrain[i][j] = TerrainType.WATER;
                } else {
                    terrain[i][j] = TerrainType.GRASS;
                }
            }
        }
        return terrain;
    }

    private void addWalls(int row, int col, List<int[]> walls) {
        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] dir : dirs) {
            int r = row + dir[0];
            int c = col + dir[1];
            if (r > 0 && r < rows - 1 && c > 0 && c < cols - 1 && maze[r][c] == 1) {
                walls.add(new int[]{r, c});
            }
        }
    }

    private int countAdjacentCells(int row, int col) {
        int count = 0;
        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] dir : dirs) {
            int r = row + dir[0];
            int c = col + dir[1];
            if (r >= 0 && r < rows && c >= 0 && c < cols && maze[r][c] == 0) {
                count++;
            }
        }
        return count;
    }
}

