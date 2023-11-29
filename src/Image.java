import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class Image {
    int width;
    int height;
    int blocksNum;

    List<List<Double>> getImagePixels(String imagePath) {
        List<List<Double>> pixels = new ArrayList<>();

        double[][] pixelValues = {
                {1, 2, 7, 9, 4, 11},
                {3, 4, 6, 6, 12, 12},
                {4, 9, 15, 14, 9, 9},
                {10, 10, 20, 18, 8, 8},
                {4, 3, 17, 16, 1, 4},
                {4, 5, 18, 18, 5, 6}
        };

        for (int y = 0; y < pixelValues.length; y++) {
            List<Double> row = new ArrayList<>();
            for (int x = 0; x < pixelValues[y].length; x++) {
                row.add(pixelValues[y][x]);
            }
            pixels.add(row);
        }

        width = pixels.get(0).size();
        height = pixels.size();

        return pixels;
    }

    public List<Block> divideIntoBlocks(int blockWidth, int blockHeight, String imagePath) {
        List<Block> allBlocks = new ArrayList<>();
        List<List<Double>> imagePixels = getImagePixels(imagePath);

        int width = imagePixels.get(0).size();
        int height = imagePixels.size();

        for (int i = 0; i < height; i += blockHeight) {
            for (int j = 0; j < width; j += blockWidth) {
                List<List<Double>> blockPixels = new ArrayList<>();
                for (int y = i; y < i + blockHeight && y < height; y++) {
                    List<Double> sublist = new ArrayList<>();
                    for (int x = j; x < j + blockWidth && x < width; x++) {
                        sublist.add(imagePixels.get(y).get(x));
                    }
                    blockPixels.add(sublist);
                }
                Block b = new Block(blockWidth, blockHeight, blockPixels, -1);
                allBlocks.add(b);
            }
        }
        return allBlocks;
    }

    public Block getAverageBlock(int blockWidth, int blockHeight, List<Block> blocks) {

        int totalBlocks = blocks.size();
        int index = -1;
        double[][] sumOfPixels = new double[blockHeight][blockWidth];

        for (Block block : blocks) {
            List<List<Double>> blockPixels = block.pixels;

            for (int y = 0; y < blockHeight && y < blockPixels.size(); y++) {
                for (int x = 0; x < blockWidth && x < blockPixels.get(y).size(); x++) {
                    sumOfPixels[y][x] += blockPixels.get(y).get(x);
                }
            }
        }

        List<List<Double>> averagePixels = new ArrayList<>();
        for (int y = 0; y < blockHeight; y++) {
            List<Double> row = new ArrayList<>();
            for (int x = 0; x < blockWidth; x++) {
                double averageValue = sumOfPixels[y][x] / totalBlocks;
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                row.add(Double.parseDouble(decimalFormat.format(averageValue)));
            }
            averagePixels.add(row);
        }

        return new Block(blockWidth, blockHeight, averagePixels, index);
    }


}
