import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Image image = new Image();
        List<Block> blocks = image.divideIntoBlocks(2,2,"image2.jpg");
       Block b = image.getAverageBlock(2 , 2 , blocks);
       b.printBlock();
    }
}