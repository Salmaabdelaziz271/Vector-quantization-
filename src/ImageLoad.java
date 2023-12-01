import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageLoad {
    int width;
    int height;
    int blocksNum;

    List<List<Double>>  getImagePixels (String imagePath)
    {
        List<List<Double>> pixels = new ArrayList<>();
        try{

            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            width = image.getWidth();
            height = image.getHeight();

            for (int y = 0; y < height; y++) {
                List<Double> row = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    double grayValue = (pixel >> 16) & 0xFF;
                    row.add(grayValue);
                }
                pixels.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    List<Block> replaceImage (List<Block> finalBlocks , List<Block> originalImage){
        VectorQuantization v = new VectorQuantization();
        v.nearestVectors(originalImage , finalBlocks);

        for(Block block : originalImage){
            int index = block.index;
            if (index >= 0 && index < finalBlocks.size()) {
                Block replacementBlock = finalBlocks.get(index);
                block.setPixels(replacementBlock.getPixels());
            }
        }
        return originalImage;
    }



    public void newImage(List<Block> replacedBlocks, String outputPath) {
        try {
            BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int currentX = 0;
            int currentY = 0;

            for (Block block : replacedBlocks) {
                List<List<Double>> blockPixels = block.getPixels();

                for (int y = 0; y < block.getHeight() && currentY + y < height; y++) {
                    for (int x = 0; x < block.getWidth() && currentX + x < width; x++) {
                        int pixelValue = blockPixels.get(y).get(x).intValue();
                        resultImage.setRGB(currentX + x, currentY + y, (pixelValue << 16) | (pixelValue << 8) | pixelValue);
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
