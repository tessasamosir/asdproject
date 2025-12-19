import javax.swing.*;
import java.awt.*;

public class SnakeLadderGameGUI {
    public static void main(String[] args) {

        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI Semibold", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TitledBorder.font", new Font("Segoe UI Semibold", Font.PLAIN, 14));

        SwingUtilities.invokeLater(() -> {
            new GameSetupFrame();
        });
    }
}
