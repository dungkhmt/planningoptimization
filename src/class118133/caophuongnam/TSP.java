package class118133.caophuongnam;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class TSP {
    static {
        System.loadLibrary("jniortools");
    }
    int N;
    int[][] c = {
            {0, 6, 1, 1},
            {6, 0, 1, 1},
            {1, 1, 0, 5},
            {1, 1, 5, 0}
    };
    MPSolver solver;
    MPVariable[][] X;

    int[] b;
    HashSet<Integer> S;
    int timesDetect = 5;
//    private void process() {
//        S.clear();
//        for (int i=0; i<N; ++i) if(b[i] == 1) S.add(i);
//
//        if (S.size() > 1 && S.size() < N) {
//            MPConstraint c = solver.makeConstraint(0, S.size()-1);
//            for (int i: S) {
//                for (int j : S) {
//                    if (i != j) {
//                        c.setCoefficient(X[i][j], 1);
//                    }
//                }
//            }
//        }
//    }

//    private void TRY(int k) {
//        for (int v=0; v<=1; v++) {
//            b[k] = v;
//            if (k == N-1) {
//                process();
//                System.out.println("Skip check sub process ...");
//            } else {
//                TRY(k+1);
//            }
//        }
//    }

    private void detectWrongProcess(MPVariable[][] X, int N) {
        int[] visited = new int[N];
        int first = 0;
        while (visited[first] != 1) {
            visited[first] = 1;
            for (int i=0; i<N; ++i) if (i!=first) {
                if (X[first][i].solutionValue() == 1) {
                    first = i;
                    break;
                }
            }
            if (visited[first] == 1) {
                addConstraint(first);
            }
        }
    }

    private void addConstraint(int start) {
        int ind = 0;
        S.clear();
        S.add(start);
        for (int i=0; i<N; ++i) {
            if (i!=start) {
                if (X[start][i].solutionValue() == 1) {
                    S.add(i);
                    ind = i;
                    break;
                }
            }
        }
        while (ind!=start) {
            for (int i=0; i<N; ++i) if (i!=ind) {
                if (X[ind][i].solutionValue() == 1) {
                    S.add(i);
                    ind = i;
                    break;
                }
            }
        }
        if (S.size()<N && S.size()>1) {
            System.out.println("Detect Wrong Process");
            MPConstraint c = solver.makeConstraint(0, S.size()-1);
            for (int i: S) {
                for (int j : S) {
                    if (i != j) {
                        c.setCoefficient(X[i][j], 1);
                    }
                }
            }
            solve();
        }
    }

    private void solve() {
        MPSolver.ResultStatus stat = solver.solve();
        if (stat != MPSolver.ResultStatus.OPTIMAL) {
            System.err.println("The problem does not have an optimal solution!");
            return;
        }

        // Change "1" to timesDetect to restrict times we detect wrong process
        if (1>0) {
            timesDetect--;
            detectWrongProcess(X, N);
        }
    }

    public void run() {
        System.out.println("Solve start ...");

        solver = new MPSolver(
                "TSP Solver",
                MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING")
        );
        try {
            Scanner scanner = new Scanner(new File("tsp-10.txt"));
            N = scanner.nextInt();
            c = new int [N][N];
            for (int i=0; i<N; ++i) {
                for (int j=0; j<N; ++j) {
                    c[i][j] = scanner.nextInt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        X = new MPVariable[N][N];
        for (int i = 0; i<N; ++i) {
            for (int j=0; j<N; ++j) {
                if (i!=j) {
                    X[i][j] = solver.makeIntVar( 0, 1, "X["+i+","+j+"]");
                }
            }
        }
        for (int i=0; i<N; ++i) {
            MPConstraint c = solver.makeConstraint(1, 1);
            for (int j = 0; j<N; ++j) if(i!=j) {
                c.setCoefficient(X[i][j], 1);
            }

            c = solver.makeConstraint(1, 1);
            for (int j=0; j<N; ++j) if (i!=j) {
                c.setCoefficient(X[j][i], 1);
            }
        }

        //SEG generation
        // b = new int[N];
        S = new HashSet<Integer>();
        //TRY(0);

        MPObjective obj = solver.objective();
        for (int i=0; i<N; ++i) {
            for (int j=0; j<N; ++j) if(i!=j) {
                obj.setCoefficient(X[i][j], c[i][j]);
            }
        }

        solve();

        System.out.println("Optimal Objective: " + solver.objective().value());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1");
        for (int i=0; i<N; ++i) {
            for (int j=0; j<N; ++j) if (i!=j) {
                System.out.println("X["+i+","+j+"] = " + X[i][j].solutionValue());
            }
        }
        int start = 0;
        int ind = 0;
        boolean notDone = true;
        while(notDone) {
            for (int i=0; i<N; ++i) if (i!=ind) {
                if (X[ind][i].solutionValue() == 1) {
                    stringBuilder.append(" -> "+ (i+1));
                    ind = i;
                    break;
                }
            }
            if (ind == start) notDone = false;
        }

        System.out.println("Solution for this problem is:");
        System.out.println(stringBuilder.toString());
    }

    public static void main(String[] args) {
        TSP app = new TSP();
        app.run();
    }
}
