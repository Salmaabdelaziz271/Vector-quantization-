import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Image image = new Image();
        VectorQuantization v = new VectorQuantization();

        List<Block> finalBlocks = v.getFinalBlocks(50 ,2  , 5 , "image2.jpg");
        List<Block> originalBlocks = image.divideIntoBlocks(2, 5 , "image2.jpg");
        List<Block> replacementImage = image.replaceImage(finalBlocks , originalBlocks);
        image.newImage(replacementImage , "newImage45.jpg");

    }

}