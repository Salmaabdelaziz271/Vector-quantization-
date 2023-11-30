import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Image image = new Image();
        VectorQuantization v = new VectorQuantization();
        //List<Block> blocks = image.divideIntoBlocks(2,2,"image2.jpg");
//       Block b = image.getAverageBlock(2 , 2 , blocks);
//       Block left = v.splitLeft(b);
//       Block right = v.splitRight(b);
//       left.printBlock();
//       System.out.println("-------------");
//       right.printBlock();
        List<Block> blocks = v.getFinalBlocks(4 , 2,2,"image1.jpg");
        for(Block b : blocks){
            b.printBlock();
        }


    }
}