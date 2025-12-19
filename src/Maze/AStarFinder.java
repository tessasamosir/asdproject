package Maze;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class AStarFinder extends PathFinder {
    AStarFinder(int[][] maze, TerrainType[][] terrain, Position start, Position goal) {
        super(maze, terrain, start, goal);
    }

    @Override
    boolean findPath() {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Position, Integer> gScore = new HashMap<>();

        Node startNode = new Node(start, null, 0, heuristic(start));
        openSet.offer(startNode);
        gScore.put(start, 0);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (visited.contains(current.pos)) continue;
            visited.add(current.pos);

            if (current.pos.equals(goal)) {
                reconstructPath(current);
                calculateTotalCost();
                return true;
            }

            for (int[] dir : getDirections()) {
                int newRow = current.pos.row + dir[0];
                int newCol = current.pos.col + dir[1];
                Position newPos = new Position(newRow, newCol);

                if (isValid(newRow, newCol)) {
                    int terrainCost = terrain[newRow][newCol].cost;
                    int tentativeG = current.g + terrainCost;

                    if (tentativeG < gScore.getOrDefault(newPos, Integer.MAX_VALUE)) {
                        gScore.put(newPos, tentativeG);
                        int h = heuristic(newPos);
                        Node newNode = new Node(newPos, current, tentativeG, h);
                        openSet.offer(newNode);
                    }
                }
            }
        }
        return false;
    }

    private int heuristic(Position pos) {
        return Math.abs(pos.row - goal.row) + Math.abs(pos.col - goal.col);
    }

    private void reconstructPath(Node node) {
        while (node != null) {
            path.add(0, node.pos);
            node = node.parent;
        }
    }
}
