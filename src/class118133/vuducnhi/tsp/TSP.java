package class118133.vuducnhi.tsp;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.HashSet;

public class TSP {
    static {
        System.loadLibrary("jniortools");
    }
    int N;
    int[][] cost;
    MPSolver solver;
    MPVariable[][] X;
    int[] b;

    private void process() {
        HashSet<Integer> S = new HashSet<Integer>();
        for (int i = 0; i < N; ++i) {
            if (b[i] == 1) {
                S.add(i);
            }
        }
        if (S.size() > 1 && S.size() < N) {
            MPConstraint c = solver.makeConstraint(0, S.size() - 1);
            for (int i : S) {
                for (int j : S) {
                    if (i != j) {
                        c.setCoefficient(X[i][j], 1);
                    }
                }
            }
        }
    }
    private void backtrack(int k) {
        for (int v = 0; v <= 1; ++v) {
            b[k] = v;
            if (k == N - 1) {
                process();
            } else {
                backtrack(k + 1);
            }
        }
    }
    public void solve() {
        solver = new MPSolver("TSP solver", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
        N = cost.length;
        X = new MPVariable[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (i != j) {
                    X[i][j] = solver.makeIntVar(0, 1, "X[" + i + "," + j + "]");
                }
            }
        }
        for (int i = 0; i < N; ++i) {
            MPConstraint c = solver.makeConstraint(1, 1);
            for (int j = 0; j < N; ++j) {
                if (i != j) {
                    c.setCoefficient(X[i][j], 1);
                }
            }
            c = solver.makeConstraint(1, 1);
            for (int j = 0; j < N; ++j) {
                if (i != j) {
                    c.setCoefficient(X[j][i], 1);
                }
            }
        }
        b = new int[N];
        backtrack(0);
        MPObjective obj = solver.objective();
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (i != j) {
                    obj.setCoefficient(X[i][j], cost[i][j]);
                }
            }
        }
        MPSolver.ResultStatus stat = solver.solve();
        if (stat != MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("The problem does not have an optimal solution!");
            return;
        }
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                if (i != j) {
                    System.out.println(i + " " + j + " " + X[i][j].solutionValue());
                }
            }
        }
    }
    public static void main(String[] args) {
        TSP app = new TSP();
        DataReader dataReader = new DataReader(10);
        app.cost = dataReader.readCostFromDataFile();
        app.solve();
    }
}



