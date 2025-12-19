import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

// BFS Algorithm dengan terrain cost
class BFSFinder extends PathFinder {
    BFSFinder(int[][] maze, TerrainType[][] terrain, Position start, Position goal) {
        super(maze, terrain, start, goal);
    }

    @Override
    boolean findPath() {
        Queue<Node> queue = new LinkedList<>();
        Map<Position, Node> nodeMap = new HashMap<>();

        Node startNode = new Node(start, null, 0, 0);
        queue.offer(startNode);
        nodeMap.put(start, startNode);
        visited.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.pos.equals(goal)) {
                reconstructPath(current);
                calculateTotalCost();
                return true;
            }

            for (int[] dir : getDirections()) {
                int newRow = current.pos.row + dir[0];
                int newCol = current.pos.col + dir[1];
                Position newPos = new Position(newRow, newCol);

                if (isValid(newRow, newCol) && !visited.contains(newPos)) {
                    visited.add(newPos);
                    Node newNode = new Node(newPos, current, 0, 0);
                    queue.offer(newNode);
                    nodeMap.put(newPos, newNode);
                }
            }
        }
        return false;
    }

    private void reconstructPath(Node node) {
        while (node != null) {
            path.add(0, node.pos);
            node = node.parent;
        }
    }
}