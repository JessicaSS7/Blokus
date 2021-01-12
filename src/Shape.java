import java.util.*;
import javafx.util.Pair;

public class Shape {
    public int i;
    public int j;
    public ArrayList<Pair<Integer,Integer>> neighbor;
    Shape(int ii,int jj, ArrayList<Pair<Integer,Integer>> neighbors){
        i = ii;
        j = jj;
        neighbor = neighbors;

    }

}