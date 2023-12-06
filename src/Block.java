import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {
    int width;
    int height;
    int index;
    List<List<Double>> pixels = new ArrayList<>();

    public Block(int width, int height, List<List<Double>> pixels, int index) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
        this.index = index;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<List<Double>> getPixels() {
        return pixels;
    }

    public void setPixels(List<List<Double>> pixels) {
        this.pixels = pixels;
    }

    public void setPixel(Double pixel) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels.get(y).set(x, pixel);
            }
        }
    }

    public void printBlock() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(pixels.get(y).get(x) + " ");
            }
            System.out.println();
        }
        System.out.println("\n");

    }
    public String getBlock() {
        String result  = "";
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double pixelValue = pixels.get(y).get(x);
                int intValue = (int) pixelValue;
                result += (intValue + " ");
            }

        }
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Block other = (Block) obj;
        // Compare the contents of pixels or other relevant fields
        return Objects.equals(pixels, other.pixels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pixels);
    }

}
