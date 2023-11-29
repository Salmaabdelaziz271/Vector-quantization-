import java.util.ArrayList;
import java.util.List;

public class Block
{
    int width;
    int height;
    List<List<Integer>> pixels = new ArrayList<>();

    public Block(int width , int height , List<List<Integer>> pixels){
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }


}
