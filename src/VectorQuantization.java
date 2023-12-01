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

    List<Block> getFinalBlocks (int finalBlocksNum , int blockWidth, int blockHeight , String imagePath){
        Image image = new Image();
        List<Block> imageBlocks =  image.divideIntoBlocks(blockWidth , blockHeight , imagePath);
        List<Block> leaf = new ArrayList<>();
        leaf.add(splitLeft(image.getAverageBlock(blockWidth , blockHeight ,imageBlocks)));
        leaf.add(splitRight(image.getAverageBlock(blockWidth , blockHeight , imageBlocks)));

        List<Block> nearstBlocks = new ArrayList<>();
        List<Block> tempBlocks = new ArrayList<>();
        List <Block>newLeaf = new ArrayList<>();



        while(leaf.size()<finalBlocksNum){
            nearestVectors(imageBlocks , leaf);
            for(int i=0 ;i<leaf.size() ;i++){
                for(Block b : imageBlocks){
                    if(b.index == i){
                        nearstBlocks.add(b);
                    }
                }
                newLeaf.add(image.getAverageBlock(blockWidth, blockHeight , nearstBlocks));
                nearstBlocks.clear();
            }
            leaf.clear();
            leaf.addAll(newLeaf);
            newLeaf.clear();


            for(int i=0 ;i<leaf.size() ;i++){
                Block left = splitLeft(leaf.get(i));
                Block right = splitRight(leaf.get(i));
                newLeaf.add(left);
                newLeaf.add(right);
            }
            leaf.clear();
            leaf.addAll(newLeaf);
            newLeaf.clear();
        }

        while (!tempBlocks.equals(leaf)){
            tempBlocks = new ArrayList<>(leaf);

            nearestVectors(imageBlocks , leaf);
            for(int i=0 ;i<leaf.size() ;i++){
                for(Block b : imageBlocks){
                    if(b.index == i){
                        nearstBlocks.add(b);
                    }
                }
                newLeaf.add(image.getAverageBlock(blockWidth , blockHeight , nearstBlocks));
                nearstBlocks.clear();
            }
            leaf.clear();
            leaf.addAll(newLeaf);
            newLeaf.clear();
        }

        return leaf;
    }

}

