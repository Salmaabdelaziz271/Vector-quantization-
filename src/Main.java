import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Image image = new Image();
        VectorQuantization v = new VectorQuantization();

        List<Block> finalBlocks = v.getFinalBlocks(1000 ,2  , 5 , "image2.jpg");
        List<Block> originalBlocks = image.divideIntoBlocks(2, 5 , "image2.jpg");
        List<Block> replacementImage = image.replaceImage(finalBlocks , originalBlocks);
        image.newImage(replacementImage , "newImage22.jpg");

    }

}