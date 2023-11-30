import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Image image = new Image();
        VectorQuantization v = new VectorQuantization();
        List<Block> allBlocks = image.divideIntoBlocks(2, 2, "image1.jpg");
        Block b = image.getAverageBlock(2, 2, allBlocks);
        Block right = v.splitRight(b);
        Block left = v.splitLeft(b);
        List<Block> blocks = new ArrayList<>();
        blocks.add(left);
        blocks.add(right);
        v.nearestVectors(allBlocks, blocks);
        for (Block block : allBlocks) {
            block.printBlock();
            System.out.println("index: " + block.index);
            System.out.println("--------------");
        }
        

    }
}