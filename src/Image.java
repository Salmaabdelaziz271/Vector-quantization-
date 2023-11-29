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

    List<List<Integer>>  getImagePixels (String imagePath)
    {
        List<List<Integer>> pixels = new ArrayList<>();
        try{

            File imageFile = new File(imagePath);
            BufferedImage image = ImageIO.read(imageFile);

            width = image.getWidth();
            height = image.getHeight();

            for (int y = 0; y < height; y++) {
                List<Integer> row = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    int grayValue = (pixel >> 16) & 0xFF;
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
        List<List<Integer>> imagePixels =getImagePixels(imagePath);

        int width = imagePixels.get(0).size();
        int height = imagePixels.size();

        for (int i = 0; i < height; i += blockHeight) {
            for (int j = 0; j < width; j += blockWidth) {
                List<List<Integer>> blockPixels = new ArrayList<>();
                for (int y = i; y < i + blockHeight && y < height; y++) {
                    List<Integer> sublist = new ArrayList<>();
                    for (int x = j; x < j + blockWidth && x < width; x++) {
                        sublist.add(imagePixels.get(y).get(x));
                    }
                    blockPixels.add(sublist);
                }
                Block b = new Block(blockWidth, blockHeight, blockPixels);
                allBlocks.add(b);
            }
        }
        return allBlocks;
    }
}
