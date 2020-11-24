package TwoDBinPacking;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.Scanner;

public class BinPacking {
    public static void main(String[] args) {
        int W,H;
        int[] w = new int[20];
        int[] h = new int[20];
        Scanner scan = new Scanner(System.in);
        W = scan.nextInt();
        H = scan.nextInt();
        int N = 0;
        while(true){
            w[N] = scan.nextInt();
            if (w[N] == -1) break;
            h[N] = scan.nextInt();
            N++;
        }

        scan.close();
        Model model = new Model("BinPacking");
        IntVar[] x = model.intVarArray("x_" , N ,0,W);
        IntVar[] y = model.intVarArray("y_",N,0,H);
        BoolVar[] o = model.boolVarArray("o_",N);

        /*

        oi = 0  xi + wi ≤ W  yi + hi ≤ H
        oi = 1  xi + hi ≤ W  yi + wi ≤ H
         */
        for (int i = 0 ; i < N ; i++){
            o[i].eq(0).imp(x[i].add(w[i]).le(W).and(y[i].add(h[i]).le(H))).post();
            o[i].eq(1).imp(x[i].add(h[i]).le(W).and(y[i].add(w[i]).le(H))).post();
        }
        /*
        overlap
        (oi = 0  oj = 0)  (xi + wi ≤ xj  xj + wj ≤ xi   yi + hi ≤ yj  yj + hj ≤ yi )
        (oi = 0  oj = 1)  (xi + wi ≤ xj  xj + hj ≤ xi   yi + hi ≤ yj  yj + wj ≤ yi )
        (oi = 1  oj = 0)  (xi + hi ≤ xj  xj + wj ≤ xi   yi + wi ≤ yj  yj + hj ≤ yi )
        (oi = 1  oj = 1)  (xi + hi ≤ xj  xj + hj ≤ xi   yi + wi ≤ yj  yj + wj ≤ yi )
         */
        for (int i =0; i < N-1 ; i++){
            for(int j=i+1; j<N ; j++){
                (o[i].eq(0).and(o[j].eq(0))).imp(x[i].add(w[i]).le(x[j]).or(x[j].add(w[j]).le(x[i])).or(y[i].add(h[i]).le(y[j]).or(y[j].add(h[j]).le(y[i])))).post();
                (o[i].eq(0).and(o[j].eq(1))).imp(x[i].add(w[i]).le(x[j]).or(x[j].add(h[j]).le(x[i])).or(y[i].add(h[i]).le(y[j]).or(y[j].add(w[j]).le(y[i])))).post();
                (o[i].eq(1).and(o[j].eq(0))).imp(x[i].add(h[i]).le(x[j]).or(x[j].add(w[j]).le(x[i])).or(y[i].add(w[i]).le(y[j]).or(y[j].add(h[j]).le(y[i])))).post();
                (o[i].eq(1).and(o[j].eq(1))).imp(x[i].add(h[i]).le(x[j]).or(x[j].add(h[j]).le(x[i])).or(y[i].add(w[i]).le(y[j]).or(y[j].add(w[j]).le(y[i])))).post();
            }
        }
        Solver solver = model.getSolver();
        Solution solution = solver.findSolution();
        System.out.println(solution.toString());
//        while (solver.solve()){
//            Solution solution = solver.findSolution();
//            System.out.println(solution.toString());
//            System.out.println(1);
//        }

    }
}
