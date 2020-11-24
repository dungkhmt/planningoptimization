package test;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class BinPacking2D {

    int W, L;
    ArrayList<Integer> wList = new ArrayList<>();
    ArrayList<Integer> lList = new ArrayList<>();
    int n;

    private void solve() {
        Model model = new Model("BinPacking2D");

        try {
            // Read File in root folder
            Scanner scanner = new Scanner(new File("bin-packing.txt"));
            W = scanner.nextInt();
            L = scanner.nextInt();
            n = 0;
            int w, l;
            while (scanner.hasNext()) {
                w = scanner.nextInt();
                if (w == -1) {
                    scanner.close();
                    break;
                }
                wList.add(w);
                l = scanner.nextInt();
                lList.add(l);
                n++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        IntVar[] wGood = new IntVar[n];
        IntVar[] lGood = new IntVar[n];
        IntVar[] orientation = new IntVar[n];

        for (int i=0; i<n; ++i) {
            wGood[i] = model.intVar("wGood["+i+"]", 0, W);
            lGood[i] = model.intVar("lGood["+i+"]", 0, L);
            orientation[i] = model.intVar("orientation["+i+"]", 0, 1);
        }

        for (int i=0; i<n; ++i) {
            wGood[i].add(orientation[i].mul(lList.get(i) - wList.get(i))).add(wList.get(i)).le(W).post();
            lGood[i].add(orientation[i].mul(wList.get(i) - lList.get(i))).add(lList.get(i)).le(L).post();
        }

//        (oi = 0  oj = 0)  (xi + wi ≤ xj  xj + wj ≤ xi   yi + hi ≤ yj  yj + hj ≤ yi )
//        (oi = 0  oj = 1)  (xi + wi ≤ xj  xj + hj ≤ xi   yi + hi ≤ yj  yj + wj ≤ yi )
//        (oi = 1  oj = 0)  (xi + hi ≤ xj  xj + wj ≤ xi   yi + wi ≤ yj  yj + hj ≤ yi )
//        (oi = 1  oj = 1)  (xi + hi ≤ xj  xj + hj ≤ xi   yi + wi ≤ yj  yj + wj ≤ yi )

        for (int i=0; i<n; ++i) {
            for (int j=0; j<n; ++j) if (i!=j) {
                (orientation[i].eq(0)).and(orientation[j].eq(0))
                        .imp((wGood[i].sub(wGood[j]).le(wList.get(i)))
                                .or((wGood[j].sub(wGood[i])).le(wList.get(j)))
                                .or((lGood[i].sub(lGood[j]).le(lList.get(i))))
                                .or((lGood[j].sub(lGood[i])).le(lList.get(j)))
                        ).post();

                (orientation[i].eq(0)).and(orientation[j].eq(1))
                        .imp((wGood[i].sub(wGood[j]).le(wList.get(i)))
                                .or((wGood[j].sub(wGood[i])).le(lList.get(j)))
                                .or((lGood[i].sub(lGood[j]).le(lList.get(i))))
                                .or((lGood[j].sub(lGood[i])).le(wList.get(j)))
                        ).post();

                (orientation[i].eq(1)).and(orientation[j].eq(0))
                        .imp((wGood[i].sub(wGood[j]).le(lList.get(i)))
                                .or((wGood[j].sub(wGood[i])).le(wList.get(j)))
                                .or((lGood[i].sub(lGood[j]).le(wList.get(i))))
                                .or((lGood[j].sub(lGood[i])).le(lList.get(j)))
                        ).post();

                (orientation[i].eq(0)).and(orientation[j].eq(0))
                        .imp((wGood[i].sub(wGood[j]).le(lList.get(i)))
                                .or((wGood[j].sub(wGood[i])).le(lList.get(j)))
                                .or((lGood[i].sub(lGood[j]).le(wList.get(i))))
                                .or((lGood[j].sub(lGood[i])).le(wList.get(j)))
                        ).post();
            }
        }
        boolean hasResult = model.getSolver().solve();

        if (hasResult) {
            System.out.println("Can load all goods!");
            System.out.println("(x, y) is:");
            for (int i=0; i<n; ++i) {
                System.out.println("("+wGood[i]+", "+lGood[i]+")");
            }
        } else {
            System.out.println("Can't load all!");
        }
    }

    public static void main(String[] args) {
        BinPacking2D app = new BinPacking2D();
        app.solve();
    }
}
