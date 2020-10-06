
package class118133.vutrungnghia;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

public class TSP {
    {
        System.loadLibrary("jniortools");
    }
    int N;
    int[][] c;
    private void get_data(String filename){
        try{
            Scanner in = new Scanner(new File(filename));
            N = in.nextInt();
            c = new int[N][N];
            for(int i = 0; i < N; ++i){
                for(int j = 0; j < N; ++j){
                    c[i][j] = in.nextInt();
                }
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    MPSolver solver;
    MPVariable[][] x;

    // intermediate data structure for subset generation
    int[] b;
    HashSet<Integer> S;
    private void process(){
//        System.out.println();
//        for (int i = 0; i < N; ++i)
//            System.out.println(b[i] + " ");
//        System.out.println();
        S.clear();
        for(int i = 0; i < N; ++i){
            if (b[i] == 1)
                S.add(i);
        }

        if(S.size() > 1&& S.size() < N){
            MPConstraint c = solver.makeConstraint(0, S.size() - 1);
            for (int i:S){
                for(int j:S) if (i!=j){
                    c.setCoefficient(x[i][j], 1);
                }
            }
        }
    }
    private int findNext(int i) {
        for (int j = 0; j < N; j++)
            if (i != j) {
                if (Math.abs(x[i][j].solutionValue() - 1) < 0.01)
                    return j;
            }
        return -1;
    }

    public void printTour() {
        int s = 0;
        System.out.print(s);
        while (true) {
            int ns = findNext(s);
            if (ns == -1)
                break;
            System.out.print(" -> " + ns);
            if (ns == 0)
                break;
            s = ns;
        }
    }
    private void TRY(int k){
        for (int v = 0; v <= 1; v++){
            b[k] = v;
            if (k == N - 1){
                process();
            }
            else{
                TRY((k + 1));
            }
        }
    }

    public  void solve(){
        System.out.println("solve start...");
        get_data("data/TSP/tsp-10.txt");
        solver = new MPSolver("TSP solver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
        N = c.length;
//        System.out.println(N);
        x = new MPVariable[N][N];
        for(int i = 0; i < N; ++i){
            for (int j = 0; j < N; ++j) if (i!=j){
                x[i][j] = solver.makeIntVar(0, 1, "x[" + i + "," + j + "]");
            }
        }

        // flow balance
        for(int i = 0; i < N; ++i){
            MPConstraint c = solver.makeConstraint(1, 1);
            for(int j = 0; j < N; ++j) if (i != j){
                c.setCoefficient(x[i][j], 1);
            }
            c = solver.makeConstraint(1, 1);
            for(int j = 0; j < N; j++){
                if (i != j){
                    c.setCoefficient(x[j][i], 1);
                }
            }
        }

        // seg generation
        b = new int[N];
        S = new HashSet<Integer>();
        TRY(0);

        // objective
        MPObjective obj = solver.objective();
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                if (i != j){
                    obj.setCoefficient(x[i][j], c[i][j]);
                }
            }
        }
        MPSolver.ResultStatus stat = solver.solve();
        if (stat != MPSolver.ResultStatus.OPTIMAL){
            System.err.println("The problem does not have an optimal solution!");
            return;
        }
        for (int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                if (i != j){
                    System.out.println("X[" + i + "," + j + "] = " + x[i][j].solutionValue());
                }
            }
        }
        printTour();
    }
    public static void main(String[] args) {
        TSP app = new TSP();
        app.solve();
    }

}