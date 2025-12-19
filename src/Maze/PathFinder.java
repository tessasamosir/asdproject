package Maze;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Abstract class untuk algoritma pencarian
abstract class PathFinder {
    protected int[][] maze;
    protected TerrainType[][] terrain;
    protected Position start;
    protected Position goal;
    protected List<Position> path;
    protected Set<Position> visited;
    protected int totalCost;

    PathFinder(int[][] maze, TerrainType[][] terrain, Position start, Position goal) {
        this.maze = maze;
        this.terrain = terrain;
        this.start = start;
        this.goal = goal;
        this.path = new ArrayList<>();
        this.visited = new HashSet<>();
        this.totalCost = 0;
    }

    abstract boolean findPath();

    List<Position> getPath() { return path; }
    Set<Position> getVisited() { return visited; }
    int getTotalCost() { return totalCost; }

    protected boolean isValid(int row, int col) {
        return row >= 0 && row < maze.length && col >= 0 && col < maze[0].length && maze[row][col] == 0;
    }

    protected int[][] getDirections() {
        return new int[][]{{-1,0}, {1,0}, {0,-1}, {0,1}};
    }

    protected void calculateTotalCost() {
        totalCost = 0;
        for (Position pos : path) {
            totalCost += terrain[pos.row][pos.col].cost;
        }
    }
}