import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        Image image = new Image();
//        VectorQuantization v = new VectorQuantization();
//
//        List<Block> finalBlocks = v.getFinalBlocks(500 ,2  , 5 , "image2.jpg");
//        List<Block> originalBlocks = image.divideIntoBlocks(2, 5 , "image2.jpg");
//        List<Block> replacementImage = image.replaceImage(finalBlocks , originalBlocks);
//        image.newImage(replacementImage , "newImage47.jpg");


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