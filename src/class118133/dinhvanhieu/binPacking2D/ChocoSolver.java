package dinhvanhieu.binPacking2D;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChocoSolver {

    int W, L, numGood;
    class Good {
        int w, l;
        Good() {}
        Good(int _w, int _l) {
            w = _w;
            l = _l;
        }
    }
    ArrayList<Good> goods;
    String dataFileName;

    ChocoSolver(String fileName) {
        dataFileName = fileName;
    }

    void readData() {
        try {
            goods = new ArrayList<>();
            File dataFile = new File(dataFileName);
            Scanner reader = new Scanner(dataFile);
            W = reader.nextInt();
            L = reader.nextInt();
            while (true) {
                int w = reader.nextInt();
                if (w == -1) break;
                int l = reader.nextInt();
                numGood++;
                goods.add(new Good(w, l));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!!!");
        }
        return;
    }

    void solve() {
        Model model = new Model("BinPacking2D");
        IntVar[] x = new IntVar[numGood];
        IntVar[] y = new IntVar[numGood];
        IntVar[] xx = new IntVar[numGood];
        IntVar[] yy = new IntVar[numGood];
        IntVar[] w = new IntVar[numGood];
        IntVar[] l = new IntVar[numGood];


        for (int i = 0 ; i < numGood; ++i) {
            Good g = goods.get(i);
            x[i] = model.intVar("X_"+i, 1, W - 1);
            y[i] = model.intVar("Y_"+i, 1, L - 1);
            w[i] = model.intVar("w_"+i, 0, W - 1);
            l[i] = model.intVar("w_"+i, 0, L - 1);
            xx[i] = model.intVar("XX_"+i, 1, W);
            yy[i] = model.intVar("YY_"+i, 1, L);
        }

        for (int i = 0; i < numGood; ++i) {
            Good g = goods.get(i);
            model.arithm(w[i], "+", l[i], "=", g.w + g.l - 2).post();
            IntVar diff = w[i].dist(l[i]).intVar();
            model.arithm(diff, "=", Math.abs(g.w - g.l)).post();
            model.arithm(x[i], "+", w[i], "=", xx[i]).post();
            model.arithm(y[i], "+", l[i], "=", yy[i]).post();
        }

        for (int i = 0; i < numGood; ++i) {
            for (int j = i + 1; j < numGood; ++j) {
                IntVar xUp = xx[i].min(xx[j]).intVar();
                IntVar yUp = yy[i].min(yy[j]).intVar();
                IntVar xDown = x[i].max(x[j]).intVar();
                IntVar yDown = y[i].max(y[j]).intVar();

                BoolVar state0 = xUp.le(xDown).boolVar();
                BoolVar state1 = yUp.le(yDown).boolVar();

                state0.or(state1).post();
            }
        }

        Solution solution = model.getSolver().findSolution();

        for (int i = 0; i < numGood; ++i) {
            System.out.println(i + "th goods at:");
            System.out.print("(" + x[i].getValue() + "," + y[i].getValue() + ")");
            System.out.println("(" + xx[i].getValue() + "," + yy[i].getValue() + ")");
        }


    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        System.out.println("Start program");
        String filePath = "data/BinPacking2D/bin-packing-2D-W19-H24-I21.txt";
        ChocoSolver binPackingSolver = new ChocoSolver(filePath);

        binPackingSolver.readData();
        binPackingSolver.solve();

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.println("Running time: " + totalTime / 1e9 + "seconds");
    }
}
