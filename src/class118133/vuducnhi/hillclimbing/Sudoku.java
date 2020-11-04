package class118133.vuducnhi.hillclimbing;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.Random;

class Move {
    int row;
    int firstCol;
    int secondCol;

    public Move(int row, int firstCol, int secondCol) {
        this.row = row;
        this.firstCol = firstCol;
        this.secondCol = secondCol;
    }
}

public class Sudoku {
    LocalSearchManager searchManager;
    ConstraintSystem constraintSystem;
    VarIntLS[][] X;

    public void initialize() {
        searchManager = new LocalSearchManager();
        constraintSystem = new ConstraintSystem(searchManager);
        X = new VarIntLS[9][9];
    }

    public void defineConstraints() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                X[i][j] = new VarIntLS(searchManager, 1, 9);
                X[i][j].setValue(j + 1);
            }
        }

        for (int j = 0; j < 9; ++j) {
            VarIntLS[] Y = new VarIntLS[9];
            for (int i = 0; i < 9; ++i) {
                Y[i] = X[i][j];
            }
            constraintSystem.post(new AllDifferent(Y));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                VarIntLS[] Y = new VarIntLS[9];
                int index = -1;
                for (int ii = 0; ii < 3; ++ii) {
                    for (int jj = 0; jj < 3; ++jj) {
                        ++index;
                        Y[index] = X[3 * i + ii][3 * j + jj];
                    }
                }
                constraintSystem.post(new AllDifferent(Y));
            }
        }

        searchManager.close();
    }

    public void solve() {
        Random random = new Random();
        ArrayList<Move> candidates = new ArrayList<Move>();
        for (int it = 0; it <= 100000 && constraintSystem.violations() > 0; ++it) {
            candidates.clear();
            int minDelta = Integer.MAX_VALUE;
            for (int i = 0; i < 9; ++i) {
                for (int j1 = 0; j1 < 8; ++j1) {
                    for (int j2 = j1 + 1; j2 < 9; j2++) {
                        int delta = constraintSystem.getSwapDelta(X[i][j1], X[i][j2]);
                        if (delta < minDelta) {
                            candidates.clear();
                            minDelta = delta;
                        }

                        if (delta == minDelta) {
                            candidates.add(new Move(i, j1, j2));
                        }
                    }
                }
            }
            Move moveInfo = candidates.get(random.nextInt(candidates.size()));
            X[moveInfo.row][moveInfo.firstCol].swapValuePropagate(X[moveInfo.row][moveInfo.secondCol]);
        }
    }

    public void printSudokuBoard() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                System.out.print(X[i][j].getValue() + " " + (j == 2 || j == 5 ? "| " : ""));
            }
            System.out.println();
            if (i % 3 == 2) {
                for (int j = 0; j < 11; ++j) {
                    System.out.print("- ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        Sudoku solver = new Sudoku();
        solver.initialize();
        solver.defineConstraints();
        solver.solve();
        solver.printSudokuBoard();
    }
}
