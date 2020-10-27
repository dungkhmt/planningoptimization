package class118133.danglamsan;

import com.google.ortools.sat.*;

public class Sudoku {
    static {
        System.loadLibrary("jniortools");
    }
    CpModel cpModel;
    IntVar[][] x;

    public Sudoku() {
        this.cpModel = new CpModel();
        this.x = new IntVar[9][9];
    }

    public void initVar() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.x[i][j] = this.cpModel.newIntVar(1, 9, "x[" + i + "][" + j + "]");
            }
        }
    }

    public void makeConstrain() {
        for (int i = 0; i < 9; i++) {
            for (int j1 = 0; j1 < 9; j1++) {
                for (int j2 = j1 + 1; j2 < 9; j2++) {
                    this.cpModel.addDifferent(this.x[i][j1], this.x[i][j2]);
                    this.cpModel.addDifferent(this.x[j1][i], this.x[j2][i]);
                }
            }
        }

        for (int I = 0; I < 3; I++) {
            for (int J = 0; J < 3; J++) {
                for (int i1 = 0; i1 < 3; i1++) {
                    for (int j1 = 0; j1 < 3; j1++) {
                        for (int i2 = 0; i2 < 3; i2++) {
                            for (int j2 = 0; j2 < 3; j2 ++) {
                                if (i1 < i2 || i1 == i2 && j1 < j2) {
                                    this.cpModel.addDifferent(this.x[3 * I + i1][3 * J + j1], this.x[3 * I + i2][3 * J + j2]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void run() {
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(this.cpModel);
        if (status == CpSolverStatus.OPTIMAL) {
            for (IntVar[] i : this.x) {
                for (IntVar j : i) {
                    System.out.print(solver.value(j) + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println(status);
        }
    }

    public static void main(String[] args) {
        /*
        This sudoku game has no constraint with initial value.
         */
        Sudoku sudoku = new Sudoku();
        sudoku.initVar();
        sudoku.makeConstrain();
        sudoku.run();
    }
}
