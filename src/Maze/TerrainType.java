import java.awt.*;

// Enum untuk tipe terrain
enum TerrainType {
    GRASS(1, new Color(139, 195, 74), "Rumput"),
    MUD(5, new Color(141, 110, 99), "Lumpur"),
    WATER(10, new Color(100, 181, 246), "Air");

    final int cost;
    final Color color;
    final String name;

    TerrainType(int cost, Color color, String name) {
        this.cost = cost;
        this.color = color;
        this.name = name;
    }
}