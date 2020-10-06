
package class118133.vutrungnghia;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class TSP_v2 {
    {
        System.loadLibrary("jniortools");
    }
    int N;
    int[][] c;
    MPSolver solver;
    MPVariable[][] x;

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

    private void addConstraints(HashSet<Integer> S){
            MPConstraint c = solver.makeConstraint(0, S.size() - 1);
            for (int i:S){
                for(int j:S) if (i!=j){
                    c.setCoefficient(x[i][j], 1);
                }
            }
    }

    private ArrayList<HashSet<Integer>> extractSubtours(){
        ArrayList<HashSet<Integer>> result = new ArrayList<>();
        boolean[] mask = new boolean[N];
        for(int i = 0; i < N; ++i)
            mask[i] = false;
        for(int n = 0; n < N; ++n){
            if (mask[n])
                continue;
            HashSet<Integer> h = new HashSet<>();
            int start = n;
            mask[start] = true;
            h.add(start);
            while(true){
                int ns = findNext(start);
                mask[ns] = true;
                if(ns == n)
                    break;
                h.add(ns);
                start = ns;
            }
            if (h.size() < N)
                result.add(h);
        }
        System.out.println("No. sub tours: " + result.size());
        return result;
    }

    private int findNext(int i) {
        for (int j = 0; j < N; j++)
            if (i != j) {
                if (Math.abs(x[i][j].solutionValue() - 1) < 0.01)
                    return j;
            }
        System.err.println(i +  " must belong to a tour!!!");
        System.exit(1);
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

    public void solve(){
        System.out.println("solve start...");
        get_data("data/TSP/tsp-50.txt");
        solver = new MPSolver("TSP solver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
        N = c.length;
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

        // objective
        MPObjective obj = solver.objective();
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                if (i != j){
                    obj.setCoefficient(x[i][j], c[i][j]);
                }
            }
        }

        MPSolver.ResultStatus stat;
        boolean flag = false;
        int counter = 0;
        do{
            System.out.println("STEP: " + counter);
            System.out.println("No.constraints: " + solver.numConstraints());
            stat = solver.solve();
            ArrayList<HashSet<Integer>> subtours = extractSubtours();
            flag = (subtours.size() == 0);
            for(HashSet<Integer> h:subtours)
                addConstraints(h);
            counter ++;
            System.out.println("-----------------------------------");
        }while(!flag);

        if (stat != MPSolver.ResultStatus.OPTIMAL){
            System.err.println("The problem does not have an optimal solution!");
            return;
        }
        System.out.println("Optimal solution: " + obj.value());
        printTour();
    }
    public static void main(String[] args) {
        TSP_v2 app = new TSP_v2();
        app.solve();
    }

}