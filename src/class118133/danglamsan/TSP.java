package class118133.danglamsan;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.util.HashSet;
import class118133.danglamsan.*;

public class TSP {
    static {
        System.loadLibrary("jniortools");
    }

    MPSolver solver;
    DataModelTSP dataModelTSP;
    MPVariable[][] x;
    int[] b;
    HashSet<Integer> S = new HashSet<Integer>();
    public TSP(String fileName) {
        dataModelTSP = new DataModelTSP(fileName);
        this.x = new MPVariable[dataModelTSP.distanceMatrix.length][dataModelTSP.distanceMatrix.length];
        this.b = new int[dataModelTSP.distanceMatrix.length];
        this.solver = new MPSolver("TSP solver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
    }
    private void process() {
//        for (int i = 0; i < DataModelTSP.distanceMatrix.length; i++) {
//            System.out.print(b[i] + " ");
//        }
//        System.out.println();
        int N = dataModelTSP.distanceMatrix.length;
        S.clear();
        for (int i = 0; i < N; i++) {
            if (b[i] == 1) {
                S.add(i);
            }
        }
        if (S.size() > 1 && S.size() < N) {
            MPConstraint c = solver.makeConstraint(0, S.size() - 1);
            for (int i : S) {
                for (int j : S) {
                    if (i != j) {
                        c.setCoefficient(this.x[i][j], 1);
                    }
                }
            }
        }
    }
    // intermediate data structure for subset generation
    private void Try(int k) {
        // thu cac gia tri cho b[k]
        for (int v = 0; v <=1 ; v++) {
            b[k - 1] = v;
            if (k == dataModelTSP.distanceMatrix.length) {
                this.process();
            } else {
                this.Try(k + 1);
            }
        }
    }
    public void solve() {
        System.out.println("Solver is starting ...");
        int N = dataModelTSP.distanceMatrix.length;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.x[i][j] = this.solver.makeIntVar(0, 1, "X[" + i + "," + j + "]");
            }
        }
        // flow balance
        for (int i = 0; i < N; i++) {
            MPConstraint c = solver.makeConstraint(1, 1);
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    c.setCoefficient(this.x[i][j], 1);
                }
            }
            c = solver.makeConstraint(1, 1);
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    c.setCoefficient(this.x[j][i], 1);
                }
            }
        }

        // SEG generation
        this.Try(1);

        // objective
        MPObjective obj = solver.objective();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                obj.setCoefficient(this.x[i][j], dataModelTSP.distanceMatrix[i][j]);
            }
        }

        MPSolver.ResultStatus res_stat = solver.solve();
        if (res_stat != MPSolver.ResultStatus.OPTIMAL) {
            System.err.println("The problem does not have optimal solution");
            return;
        } else {
            printTour();
        }
        System.out.println("Objective Value: " + solver.objective().value());

    }
    public int findNext(int s) {
        for (int i = 0; i < dataModelTSP.len; i++) {
            if (s != i)
                if (this.x[s][i].solutionValue() == 1.0) {
                    return i;
                }
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
        System.out.println();
    }

    public static void main(String[] args) {
        TSP tsp = new TSP("./data/TSP/tsp-10.txt");
        tsp.solve();
    }
}
