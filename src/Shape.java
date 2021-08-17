import java.util.*;

public class Shape {
    public int i;
    public int j;
    public ArrayList<Pair> neighbor;
    Shape(int ii,int jj, ArrayList<Pair> neighbors){
        i = ii;
        j = jj;
        neighbor = neighbors;
    }
}