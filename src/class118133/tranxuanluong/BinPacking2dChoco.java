import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BinPacking2dChoco {
    int n;
    int W,L;
    int []w;
    int []l;
    IntVar[] x ;
    IntVar[] y ;
    IntVar[] o ;
    private void initData() throws FileNotFoundException {
        File input = new File("/home/luong/Desktop/bin-packing-2D.txt");
        Scanner sc = new Scanner(input);
        if (sc.hasNext()){
            W = sc.nextInt();
            L = sc.nextInt();
        }
        ArrayList<Integer> arrW = new ArrayList<>();
        ArrayList<Integer> arrL = new ArrayList<>();
        while (sc.hasNext()){
            int temp = sc.nextInt();
            if(temp==-1) break;
            arrW.add(temp);
            arrL.add(sc.nextInt());
        }
        n = arrL.size() -1;
        w = new int[n];
        l = new int[n];
        for (int i  = 0; i < n; i++){
            w[i] = arrW.get(i);
            l[i] = arrL.get(i);
        }
    }
    public void solve(){
        Model model = new Model("Bin packing");
        x = new IntVar[n];
        y = new IntVar[n];
        o = new IntVar[n];
        for(int q = 0; q < n; q++){
            x[q] = model.intVar("X_"+q, 0,W);
            y[q] = model.intVar("Y_"+q, 0, L);
            o[q] = model.intVar("O_"+q, 0, 1);
        }
        // rang buoc khong overlap
        for (int i = 0; i < n; i++){
            for (int j = i+1; j < n; j++){
                BoolVar []rotate = new BoolVar[2];
                rotate[0] = model.boolVar(o[i] ,"=",0);
            }
        }

    }

    public static void main(String[] args) {

    }
}

