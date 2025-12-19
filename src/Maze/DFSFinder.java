package Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

// DFS Algorithm dengan terrain cost
class DFSFinder extends PathFinder {
    DFSFinder(int[][] maze, TerrainType[][] terrain, Position start, Position goal) {
        super(maze, terrain, start, goal);
    }

    @Override
    boolean findPath() {
        boolean found = dfs(start);
        if (found) calculateTotalCost();
        return found;
    }

    private boolean dfs(Position current) {
        visited.add(current);
        path.add(current);

        if (current.equals(goal)) return true;

        for (int[] dir : getDirections()) {
            int newRow = current.row + dir[0];
            int newCol = current.col + dir[1];
            Position newPos = new Position(newRow, newCol);

            if (isValid(newRow, newCol) && !visited.contains(newPos)) {
                if (dfs(newPos)) return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }
}