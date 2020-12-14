package test;

import com.google.ortools.linearsolver.MPSolver;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class TSPNewSolver {

    private int n;

    public static void main(String[] args) {
        TSPNewSolver app = new TSPNewSolver();
        app.run();
    }

    private void run() {
        Model model = new Model("TSPNewSolver");
        int [][] distance = new int[20][20];
        try {
            // Read File in root folder
            Scanner scanner = new Scanner(new File("tsp-10.txt"));
            System.out.println("Load data .......");
            n = scanner.nextInt();
            for (int i=0; i<n; ++i) {
                for (int j=0; j<n; ++j) {
                    distance[i][j] = scanner.nextInt();
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        IntVar[] x = new IntVar[n];
        IntVar[] y = new IntVar[n+1];

        y[0] = model.intVar("y[0]", 0, 0);

        for (int i=0; i<n; ++i) {
            x[i] = model.intVar("x["+i+"]", 1, n);
        }
        for (int i=1; i<=n; ++i) {
            y[i] = model.intVar("y["+i+"]", 0, 50);
        }
        for (int i=0; i<n; ++i) {
            model.arithm(x[i], "!=", i).post();
        }

        model.allDifferent(x).post();
        for (int i=0; i<n; ++i) {
            for (int j=1; j<=n; ++j) {
                if (j==n) {
                    (x[i].eq(j)).imp(y[j].eq(y[i].add(distance[i][0]))).post();
                } else {
                    (x[i].eq(j)).imp(y[j].eq(y[i].add(distance[i][j]))).post();
                }
            }
        }

        model.setObjective(Model.MINIMIZE, y[n]);
        Solver solver = model.getSolver();
        // Find more better solution
        while(solver.solve()) {
            System.out.println("Solution:");
            System.out.print("0 ");
            int current = 0;
            while(current!=n) {
                current = x[current].getValue();
                System.out.print(" -> "+current);
            }
//        System.out.println();
//        System.out.println("Table");
//        for (int i=0; i<n; ++i) {
//            System.out.print(i+": ");
//            System.out.print(x[i].getValue());
//            System.out.println();
//        }
            System.out.println();
            System.out.println(y[n].getValue());
            System.out.println("-------------------------------");
        }
    }
}
