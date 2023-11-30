import java.util.ArrayList;
import java.util.List;

public class VectorQuantization {

    public Block splitLeft(Block block) {
        List<List<Double>> blockPixels = new ArrayList<>();
        for (int y = 0; y < block.height; y++) {
            List<Double> sublist = new ArrayList<>();
            for (int x = 0; x < block.width; x++) {
                double pixel = block.pixels.get(y).get(x);
                if (pixel == (int) pixel) {
                    sublist.add(pixel - 1);
                }
                else {
                    sublist.add((double) (int) pixel);
                }
            }
            blockPixels.add(sublist);
        }
        Block splitBlock = new Block(block.width, block.height, blockPixels, -1);
        return splitBlock;
    }

    public Block splitRight(Block block) {
        List<List<Double>> blockPixels = new ArrayList<>();
        for (int y = 0; y < block.height; y++) {
            List<Double> sublist = new ArrayList<>();
            for (int x = 0; x < block.width; x++) {
                double pixel = block.pixels.get(y).get(x);
                sublist.add((double) (int) pixel + 1);
            }
            blockPixels.add(sublist);
        }
        Block splitBlock = new Block(block.width, block.height, blockPixels, -1);
        return splitBlock;
    }

    public double getDistance(Block block1, Block block2) {
        double distance = 0;
        for (int y = 0; y < block1.height; y++) {
            for (int x = 0; x < block1.width; x++) {
                distance += Math.abs(block1.pixels.get(y).get(x) - block2.pixels.get(y).get(x));
            }
        }
        return distance;
    }

    public void nearestVectors(List<Block> imageBlocks, List<Block> leaf) {
        for (Block block : imageBlocks) {
            double minDistance = Double.MAX_VALUE;  
            for (int i = 0; i < leaf.size(); i++) {
                double distance = getDistance(block, leaf.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    block.index = i;
                }
            }
        }
    }



}
