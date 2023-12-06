import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
//import java.awt.*;


public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BlockInfo Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            StartPage startPage = new StartPage();
            frame.setContentPane(startPage.getPanel1());
            frame.setSize(700, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
            int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
            frame.setLocation(centerX, centerY);
            frame.setVisible(true);
        });
//        VectorQuantization v = new VectorQuantization();
//        ImageLoad i = new ImageLoad();
////        v.compress(v.getFinalBlocks(100,2,5,"image2.jpg"),
////                i.divideIntoBlocks(2,5,"image2.jpg"));
//        v.decompress("compressedFile.bin", "153.jpg");


    }

}