package class118133.nguyenmaiphuong.java;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class Sudoku {
    int board_size = 9;
    int[][] data = { { 5, 3, 0, 0, 7, 0, 0, 0, 0 }, { 6, 0, 0, 1, 9, 5, 0, 0, 0 }, { 0, 9, 8, 0, 0, 0, 0, 6, 0 },
            { 8, 0, 0, 0, 6, 0, 0, 0, 3 }, { 4, 0, 0, 8, 0, 3, 0, 0, 1 }, { 7, 0, 0, 0, 2, 0, 0, 0, 6 },
            { 0, 6, 0, 0, 0, 0, 2, 8, 0 }, { 0, 0, 0, 4, 1, 9, 0, 0, 5 }, { 0, 0, 0, 0, 8, 0, 0, 7, 9 } };

    int[][] block = { { 0, 0, 0, 1, 1, 1, 2, 2, 2 }, { 0, 0, 0, 1, 1, 1, 2, 2, 2 }, { 0, 0, 0, 1, 1, 1, 2, 2, 2 },
            { 3, 3, 3, 4, 4, 4, 5, 5, 5 }, { 3, 3, 3, 4, 4, 4, 5, 5, 5 }, { 3, 3, 3, 4, 4, 4, 5, 5, 5 },
            { 6, 6, 6, 7, 7, 7, 8, 8, 8 }, { 6, 6, 6, 7, 7, 7, 8, 8, 8 }, { 6, 6, 6, 7, 7, 7, 8, 8, 8 }, };

    LocalSearchManager mgr;
    VarIntLS[][] x;
    ConstraintSystem constraintSystem;

    public void setConstraint() {
        mgr = new LocalSearchManager();

        // Khởi tạo biến
        x = new VarIntLS[board_size][board_size];
        for (int i = 0; i < board_size; ++i)
            for (int j = 0; j < board_size; ++j) {
                if (data[i][j] != 0)
                    x[i][j] = new VarIntLS(mgr, data[i][j], data[i][j]);
                else
                    x[i][j] = new VarIntLS(mgr, 1, board_size);
            }

        // Khởi tạo ràng buộc
        constraintSystem = new ConstraintSystem(mgr);
        for (int i = 0; i < board_size; ++i) {
            VarIntLS[] y = new VarIntLS[board_size];
            for (int j = 0; j < board_size; ++j)
                y[j] = x[i][j];
            constraintSystem.post(new AllDifferent(y));
        }

        for (int j = 0; j < board_size; ++j) {
            VarIntLS[] y = new VarIntLS[board_size];
            for (int i = 0; i < board_size; ++i)
                y[i] = x[i][j];
            constraintSystem.post(new AllDifferent(y));
        }

        for (int I = 0; I < 3; ++I)
            for (int J = 0; J < 3; ++J) {
                VarIntLS[] y = new VarIntLS[board_size];
                int cnt = -1;
                for (int i = 0; i < 3; ++i)
                    for (int j = 0; j < 3; ++j)
                        y[++cnt] = x[I * 3 + i][J * 3 + j];

                constraintSystem.post(new AllDifferent(y));
            }

        mgr.close();
    }

    class Move {
        VarIntLS x;
        int newVal;

        public Move(VarIntLS x, int newVal) {
            this.x = x;
            this.newVal = newVal;
        }
    }

    public void search() {

        Random r = new Random();
        ArrayList<Move> candidates = new ArrayList();
        int it = 0;
        while (it < 100000 && constraintSystem.violations() > 0) {
            candidates.clear();
            int minDelta = Integer.MAX_VALUE;

            for (int i = 0; i < 9; ++i)
                for (int j = 0; j < 9; ++j) {
                    int newVal = r.nextInt(9);
                    int delta = constraintSystem.getAssignDelta(x[i][j], newVal);
                    if (delta < minDelta) {
                        candidates.clear();
                        candidates.add(new Move(x[i][j], newVal));
                        minDelta = delta;
                    } else if (delta == minDelta) {
                        candidates.add(new Move(x[i][j], newVal));
                    }
                }

            int idx = r.nextInt(candidates.size());
            Move m = candidates.get(idx);
            m.x.setValuePropagate(m.newVal);

            System.out.println("Step " + it + ", violations = " + constraintSystem.violations());
            it++;
        }

        for (int i = 0; i < board_size; ++i) {
            for (int j = 0; j < board_size; ++j)
                System.out.print(x[i][j].getValue());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.setConstraint();
        sudoku.search();
    }
}
