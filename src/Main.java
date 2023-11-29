import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Image image = new Image();
        //image.getImagePixels("image1.jpg");

        String outputPath = "output.txt";
        printPixelValuesToFile(image.divideIntoBlocks(2 , 2 , "image1.jpg"), outputPath);
    }

    public static void printPixelValuesToFile(List<Block> blocks, String outputPath) {
        try (PrintWriter writer = new PrintWriter(outputPath)) {
            System.out.println("\n\n\n\n\n\n");
            for (Block  block : blocks) {
               for (List<Integer> row : block.pixels) {
                    String rowString = row.toString();
                    writer.println(rowString);
                }
                writer.println("------");
            }

            System.out.println("Pixel values have been written to: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}