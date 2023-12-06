import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageLoad {
    static int width;
    static int height;

    static List<List<List<Double>>> getImagePixels(String imagePath) {
        List<List<List<Double>>> pixels = new ArrayList<>();
        try {
            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            width = image.getWidth();
            height = image.getHeight();

            for (int y = 0; y < height; y++) {
                List<List<Double>> row = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);

                    int red = (pixel >> 16) & 0xFF;
                    int green = (pixel >> 8) & 0xFF;
                    int blue = pixel & 0xFF;

                    List<Double> rgbValues = new ArrayList<>();
                    rgbValues.add((double) red);
                    rgbValues.add((double) green);
                    rgbValues.add((double) blue);

                    row.add(rgbValues);
                }
                pixels.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pixels;
    }

    public static List<Block> divideIntoBlocks(int blockWidth, int blockHeight, String imagePath) {
        List<Block> allBlocks = new ArrayList<>();
        List<List<List<Double>>> imagePixels = getImagePixels(imagePath);

        int width = imagePixels.get(0).size();
        int height = imagePixels.size();

        for (int i = 0; i < height; i += blockHeight) {
            for (int j = 0; j < width; j += blockWidth) {
                List<List<List<Double>>> blockPixels = new ArrayList<>();
                for (int y = i; y < i + blockHeight && y < height; y++) {
                    List<List<Double>> row = new ArrayList<>();
                    for (int x = j; x < j + blockWidth && x < width; x++) {
                        row.add(imagePixels.get(y).get(x));
                    }
                    blockPixels.add(row);
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
        List<List<List<Double>>> sumOfPixels = new ArrayList<>();

        for (Block block : blocks) {
            List<List<List<Double>>> blockPixels = block.getPixels();

            for (int y = 0; y < blockHeight && y < blockPixels.size(); y++) {
                for (int x = 0; x < blockWidth && x < blockPixels.get(y).size(); x++) {
                    List<Double> rgbValues = blockPixels.get(y).get(x);
                    if (sumOfPixels.size() <= y) {
                        sumOfPixels.add(new ArrayList<>());
                    }
                    if (sumOfPixels.get(y).size() <= x) {
                        sumOfPixels.get(y).add(new ArrayList<>(rgbValues)); // Initialize with a copy of the RGB values
                    } else {
                        for (int i = 0; i < rgbValues.size(); i++) {
                            sumOfPixels.get(y).get(x).set(i, sumOfPixels.get(y).get(x).get(i) + rgbValues.get(i));
                        }
                    }
                }
            }
        }

        List<List<List<Double>>> averagePixels = new ArrayList<>();
        for (int y = 0; y < blockHeight; y++) {
            List<List<Double>> row = new ArrayList<>();
            for (int x = 0; x < blockWidth; x++) {
                List<Double> rgbValues = new ArrayList<>();
                for (int i = 0; i < sumOfPixels.get(y).get(x).size(); i++) {
                    double averageValue = sumOfPixels.get(y).get(x).get(i) / totalBlocks;
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    rgbValues.add(Double.parseDouble(decimalFormat.format(averageValue)));
                }
                row.add(rgbValues);
            }
            averagePixels.add(row);
        }

        return new Block(blockWidth, blockHeight, averagePixels, index);
    }



    List<Block> replaceImage(List<Block> finalBlocks, List<Block> originalImage) {
        VectorQuantization v = new VectorQuantization();
        v.nearestVectors(originalImage, finalBlocks);

        for (Block block : originalImage) {
            int index = block.index;
            if (index >= 0 && index < finalBlocks.size()) {
                Block replacementBlock = finalBlocks.get(index);
                block.setPixels(replacementBlock.getPixels());
            }
        }
        return originalImage;
    }

    static void newImage(List<Block> replacedBlocks, String outputPath) {
        try {
            BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int currentX = 0;
            int currentY = 0;

            for (Block block : replacedBlocks) {
                List<List<List<Double>>> blockPixels = block.getPixels();

                for (int y = 0; y < block.getHeight() && currentY + y < height; y++) {
                    for (int x = 0; x < block.getWidth() && currentX + x < width; x++) {
                        List<Double> rgbValues = blockPixels.get(y).get(x);
                        int red = rgbValues.get(0).intValue();
                        int green = rgbValues.get(1).intValue();
                        int blue = rgbValues.get(2).intValue();

                        int pixelValue = (red << 16) | (green << 8) | blue;
                        resultImage.setRGB(currentX + x, currentY + y, pixelValue);
                    }
                }

                currentX += block.getWidth();
                if (currentX >= width) {
                    currentX = 0;
                    currentY += block.getHeight();
                }
            }

            ImageIO.write(resultImage, "jpg", new File(outputPath));

            System.out.println("Image with replaced blocks saved to: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

