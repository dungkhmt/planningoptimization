package class118133.caophuongnam;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;

public class BCA {
    static {
        System.loadLibrary("jniortools");
    }

    int M = 3;
    int N = 13;
    int[][] teachClass = {{1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
            {1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1},};

    int[] credits = {3, 3, 4, 3, 4, 3, 3, 3, 4, 3, 3, 4, 4};

    int[][] conflicts = {{0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
            {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0}};

    private void solve() {
        MPSolver solver = new MPSolver(
                "BCA",
                MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING
        );

        MPVariable[][] x = new MPVariable[M][N];
        for (int i=0; i<M; ++i) {
            for (int j=0; j<N; ++j) {
                x[i][j] = solver.makeIntVar(0, 1, "x["+i+","+j+"]");
            }
        }
        MPVariable[] load = new MPVariable[M];
        int totalCredits = 0;
        for (int i=0; i<credits.length; ++i) {
            totalCredits += credits[i];
        }

        for (int i=0; i<M; ++i) {
            load[i] = solver.makeIntVar(0, totalCredits, "load["+i+"]");
        }

        MPVariable maxLoad = solver.makeIntVar(0, totalCredits, "maxLoad");

        // Constraint for teacher doesn't teach some class
        for (int i=0; i<M; ++i) {
            for (int j=0; j<N; ++j) {
                if (teachClass[i][j] == 0) {
                    MPConstraint c = solver.makeConstraint(0, 0);
                    c.setCoefficient(x[i][j], 1);
                }
            }
        }

        // Constraint for some class can't be taught by a teacher
        for (int i=0; i<N; ++i) {
            for (int j=0; j<N; ++j) {
                if (conflicts[i][j] == 1) {
                    for (int t=0; t<M; ++t) {
                        MPConstraint c = solver.makeConstraint(0, 1);
                        c.setCoefficient(x[t][i], 1);
                        c.setCoefficient(x[t][j], 1);
                    }
                }
            }
        }

        // Constraint for every classes are lectured by one and only one teacher
        for (int i=0; i<N; ++i) {
            MPConstraint c = solver.makeConstraint(1, 1);
            for (int j=0; j<M; ++j) {
                c.setCoefficient(x[j][i], 1);
            }
        }

        // Setup load for each teacher
        for (int t=0; t<M; ++t) {
            MPConstraint c = solver.makeConstraint(0, 0);
            for (int i=0; i<N; ++i) {
                c.setCoefficient(x[t][i], credits[i]);
            }
            c.setCoefficient(load[t], -1);
        }

        // Setup maxLoad is max load of teachers
        for (int t=0; t<M; ++t) {
            MPConstraint c = solver.makeConstraint(0, totalCredits);
            for (int i=0; i<N; ++i) {
                c.setCoefficient(load[t], -1);
            }
            c.setCoefficient(maxLoad, 1);
        }

        MPObjective obj = solver.objective();
        obj.setCoefficient(maxLoad, 1);
        obj.setMinimization();
        ResultStatus rs = solver.solve();

        if (rs != ResultStatus.OPTIMAL) {
            System.out.println("Cannot find optimal solution!");
        } else {
            System.out.println("Max of load is: " + obj.value());
            for (int t=0; t<M; ++t) {
                System.out.println("Teacher "+t+" will teach classes:");
                for (int i=0; i<N; ++i) if (x[t][i].solutionValue()==1) {
                    System.out.println(i+" with load: "+credits[i]);
                }
                System.out.println("Total load: " + (int) load[t].solutionValue());
            }
        }
    }

    public static void main(String[] args) {
        BCA app = new BCA();
        app.solve();
    }
}
