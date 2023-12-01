import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BlockInfo Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            BlockInfo blockInfo = new BlockInfo();
            frame.setContentPane(blockInfo.getPanel1());
            frame.setSize(700, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
            int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
            frame.setLocation(centerX, centerY);
            frame.setVisible(true);
        });
    }

}