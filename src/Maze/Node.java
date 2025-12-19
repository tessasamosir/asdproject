// Kelas untuk node dalam pathfinding dengan terrain cost
class Node implements Comparable<Node> {
    Position pos;
    Node parent;
    int g, h, f;

    Node(Position pos, Node parent, int g, int h) {
        this.pos = pos;
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.f, other.f);
    }
}