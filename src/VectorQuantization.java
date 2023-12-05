import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                } else {
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
            int minIndex = -1;

            for (int i = 0; i < leaf.size(); i++) {
                double distance = getDistance(block, leaf.get(i));
                if (distance < minDistance) {
                    minDistance = distance;
                    minIndex = i;
                }
            }

            block.index = minIndex;
        }
    }


    List<Block> getFinalBlocks(int finalBlocksNum, int blockWidth, int blockHeight, String imagePath) {
        ImageLoad image = new ImageLoad();
        List<Block> imageBlocks = image.divideIntoBlocks(blockWidth, blockHeight, imagePath);
        List<Block> leaf = new ArrayList<>();
        leaf.add(splitLeft(image.getAverageBlock(blockWidth, blockHeight, imageBlocks)));
        leaf.add(splitRight(image.getAverageBlock(blockWidth, blockHeight, imageBlocks)));

        List<Block> nearstBlocks = new ArrayList<>();
        List<Block> tempBlocks = new ArrayList<>();
        List<Block> newLeaf = new ArrayList<>();

        while (leaf.size() < finalBlocksNum) {
            nearestVectors(imageBlocks, leaf);
            for (int i = 0; i < leaf.size(); i++) {
                nearstBlocks.clear();
                for (Block b : imageBlocks) {
                    if (b.index == i) {
                        nearstBlocks.add(b);
                    }
                }
                newLeaf.add(image.getAverageBlock(blockWidth, blockHeight, nearstBlocks));
            }
            leaf.clear();
            leaf.addAll(newLeaf);
            newLeaf.clear();

            for (int i = 0; i < leaf.size(); i++) {
                Block left = splitLeft(leaf.get(i));
                Block right = splitRight(leaf.get(i));
                newLeaf.add(left);
                newLeaf.add(right);
            }
            leaf.clear();
            leaf.addAll(newLeaf);
            newLeaf.clear();
        }
        int max_iteration = 5;
        while (!tempBlocks.equals(leaf)) {
            if(max_iteration <= 0)
                break;
            tempBlocks = new ArrayList<>(leaf);
            nearestVectors(imageBlocks, leaf);
            for (int i = 0; i < leaf.size(); i++) {
                nearstBlocks.clear();
                for (Block b : imageBlocks) {
                    if (b.index == i) {
                        nearstBlocks.add(b);
                    }
                }
                newLeaf.add(image.getAverageBlock(blockWidth, blockHeight, nearstBlocks));
            }
            leaf.clear();
            leaf.addAll(newLeaf);
            newLeaf.clear();
            max_iteration--;
        }

        return leaf;
    }

    public void compress(List<Block>finalBlock, List<Block>originalImage) {
        int width = finalBlock.get(0).width;
        int height = finalBlock.get(0).height;
        String codeBook = "";
        codeBook += (width + " " + height + " ");

        for(Block block : finalBlock) {
            codeBook += block.getBlock();
        }
        nearestVectors(originalImage , finalBlock);
        int numOfBits = (int)Math.ceil((Math.log(finalBlock.size()) / Math.log(2)));
        String compressedText = "", binaryText = "";
        for(Block block : originalImage){
            int index = block.index;
            if (index >= 0 && index < finalBlock.size()) {
                binaryText += String.format("%" + numOfBits + "s", Integer.toBinaryString(index & 0xFF)).replace(' ', '0');
            }
        }
        int lastSubString = binaryText.length() % 8;
        if(lastSubString == 0)
            lastSubString = 8;
        for(int i = 0; i < binaryText.length(); i+=8) {
            String binaryString =  binaryText.substring(i, Math.min(i + 8, binaryText.length()));
            int intValue = Integer.parseInt(binaryString, 2);
            compressedText += (char) intValue;
        }

        String fileName = "compressedFile.bin";

        try {
            Path filePath = Paths.get(fileName);
            Files.createFile(filePath);
            System.out.println("File created successfully at: " + filePath.toAbsolutePath());
        }
        catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
        }
        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(fileName))) {
            writer.writeBytes(codeBook + "\n" + lastSubString + " " + compressedText);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void decompress(String path, String outputImagePath) {
        File file = new File(path);
        String codeBook = "";
        String compressedText = "";
        int cnt = 1;
        try (DataInputStream reader = new DataInputStream(new FileInputStream(file))) {
            while (reader.available() > 0) {
                char c = (char) reader.readByte();
                if(c == '\n' && cnt == 1) {
                    cnt++;
                    continue;
                }
                if(cnt == 1) {
                    codeBook += c;
                }
                else {
                    compressedText += c;
                }

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        int lastBits = compressedText.charAt(0) - '0';
        String binaryText = "";
        for (int i = 2; i < compressedText.length() - 1; i++) {
            binaryText += String.format("%8s", Integer.toBinaryString(compressedText.charAt(i) & 0xFF)).replace(' ', '0');
        }
        binaryText += String.format("%" + lastBits + "s", Integer.toBinaryString(compressedText.charAt(compressedText.length()-1) & 0xFF)).replace(' ', '0');

        String[] dimensions = codeBook.split(" ");
        String width = dimensions[0];
        String height = dimensions[1];


        int step = Integer.parseInt(height) * Integer.parseInt(width);
        List<Block> allBlocks = new ArrayList<>();
        for (int j = 2; j + step <= dimensions.length; j += step) {
            List<List<Double>> block = new ArrayList<>();
            String text = "";
            for (int k = j; k < j + step; k++) {
                text += dimensions[k] + " ";
            }

            String[] values = text.trim().split(" ");
            int count = 0;
            for (int y = 0; y < Integer.parseInt(height); y++) {
                List<Double> row = new ArrayList<>();
                for (int x = 0; x < Integer.parseInt(width); x++) {
                    row.add(Double.parseDouble(values[count]));
                    count++;
                }
                block.add(row);
            }

            Block b = new Block(Integer.parseInt(width), Integer.parseInt(height), block, -1);
            allBlocks.add(b);
        }
        int numOfBits = (int)Math.ceil((Math.log(allBlocks.size()) / Math.log(2)));
        List<Block> decompressedBlocks = new ArrayList<>();
        for (int i = 0; i < binaryText.length(); i+=numOfBits) {
            String binaryString = binaryText.substring(i, i + numOfBits);
            int idx = Integer.parseInt(binaryString, 2);
            decompressedBlocks.add(allBlocks.get(idx));
        }

        ImageLoad.newImage(decompressedBlocks, outputImagePath);


    }



}