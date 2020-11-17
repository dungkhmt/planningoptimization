package code;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class binPackingChoco {
    static int W, H;
    static int[] w = new int[200];
    static int[] h = new int[200];
    static int N = 0;

    public void loadData() {
        try {
            File file = new File("C:\\Users\\trung\\IdeaProjects\\optimizeProb\\data\\BinPacking2D\\bin-packing-2D-W10-H7-I6.txt");
            Scanner in = new Scanner(file);
            W = in.nextInt();
            H = in.nextInt();
            while (true) {
                int t = in.nextInt();
                if(t==-1) break;
                w[N] = t;
                t = in.nextInt();
                w[N] = t;
                N++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        binPackingChoco ins = new binPackingChoco();
        ins.loadData();

//        for (int i = 0; i < N; i++) {
//            System.out.println(w[i] + " ");
//            System.out.println(h[i] + " ");
//        }

        Model model = new Model("BinPacking-Choco-solver");
        IntVar[] x = model.intVarArray("x", N, 0, W);
        IntVar[] y = model.intVarArray("y", N, 0, H);
        BoolVar[] o = model.boolVarArray("o", N);

        for (int i = 0; i < N; i++) {
            o[i].eq(0).imp(x[i].add(w[i]).le(W).and(y[i].add(h[i]).le(H))).post();
            o[i].eq(1).imp(x[i].add(h[i]).le(W).and(y[i].add(w[i]).le(H))).post();
        }

        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                (o[i].eq(0).and(o[j].eq(0))).imp(x[i].add(w[i]).le(x[j]).or(x[j].add(w[j]).le(x[i])).or(y[i].add(h[i]).le(y[j]).or(y[j].add(h[j]).le(y[i])))).post();
                (o[i].eq(0).and(o[j].eq(1))).imp(x[i].add(w[i]).le(x[j]).or(x[j].add(h[j]).le(x[i])).or(y[i].add(h[i]).le(y[j]).or(y[j].add(w[j]).le(y[i])))).post();
                (o[i].eq(1).and(o[j].eq(0))).imp(x[i].add(h[i]).le(x[j]).or(x[j].add(w[j]).le(x[i])).or(y[i].add(w[i]).le(y[j]).or(y[j].add(h[j]).le(y[i])))).post();
                (o[i].eq(1).and(o[j].eq(1))).imp(x[i].add(h[i]).le(x[j]).or(x[j].add(h[j]).le(x[i])).or(y[i].add(w[i]).le(y[j]).or(y[j].add(w[j]).le(y[i])))).post();
            }
        }

        Solver solver = model.getSolver();
        Solution solution = solver.findSolution();

        for(int i=0;i<N;i++){
            System.out.println("x["+i+"] = "+ x[i].getValue() + "; y["+i+"] = "+y[i].getValue());
        }
    }
}
