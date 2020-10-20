package class118133.vuducnhi.csp;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

public class Sudoku {
    public static void main(String[] args) {
        int N = 9;
        Model model = new Model("Sudoku");
        IntVar[][] X = new IntVar[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                X[i][j] = model.intVar("X[" + i + ", " + j + "]", 1, N);
            }
        }

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                for (int k = 0; k < N; ++k) {
                    if (j != k) {
                        model.arithm(X[i][j], "!=", X[i][k]).post();
                    }
                    if (i != k) {
                        model.arithm(X[i][j], "!=", X[k][j]).post();
                    }
                }
            }
        }

        for (int i = 0; i + 3 < N; i += 3) {
            for (int j = 0; j + 3 < N; j += 3) {
                
            }
        }
        Solution solution = model.getSolver().findSolution();
        if (solution != null) {
            System.out.println("solution = " + solution);
        } else {
            System.out.println("Found nothing");
        }
    }
}
