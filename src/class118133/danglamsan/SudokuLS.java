package class118133.danglamsan;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SudokuLS {
    LocalSearchManager mgr;
    VarIntLS[][] x;
    ConstraintSystem S;
    int n;
    class SwapMove {
        int row, i, j;
        public SwapMove(int row, int i, int j) {
            this.row = row;
            this.i = i;
            this.j = j;
        }
    }

    public SudokuLS() {
        this.n = 9;
        this.mgr = new LocalSearchManager();
        this.x = new VarIntLS[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.x[i][j] = new VarIntLS(this.mgr, 1, 9);
            }
        }

    }
    public void stateModel() {
        this.makeConstraint();
    }

    private void makeConstraint() {
        S = new ConstraintSystem(mgr);
        for (int i = 0; i < 9; i++) {
            this.S.post(new AllDifferent(this.x[i]));
        }
        for (int col = 0; col < 9; col++) {
            VarIntLS[] tmp = new VarIntLS[9];
            for (int j = 0; j < 9; j++) {
                tmp[j] = this.x[j][col];
            }
            this.S.post(new AllDifferent(tmp));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                VarIntLS[] tmp = new VarIntLS[9];
                int idx = -1;
                for (int i1 = 0; i1 < 3; i1++) {
                    for (int j1 = 0; j1 < 3; j1++) {
                        tmp[++idx] = this.x[3 * i + i1][3 * j + j1];
                    }
                }
                this.S.post(new AllDifferent(tmp));
            }
        }
        mgr.close();
    }

    public void solve() {
        this.init();
        this.hillClimbing(10000);
    }

    private void init() {
        for (int row = 0; row < 9; row++) {
            for (int j = 1; j <= 9; j++){
                this.x[row][j - 1].setValuePropagate(j);
            }
        }
    }

    private void hillClimbing(int maxIter) {
        List<SwapMove> cand = new ArrayList<>();
        Random r = new Random();

        for (int iter = 0; iter < maxIter; iter++) {
            if (this.S.violations() == 0) {
                System.out.println("This problem is solved");
                break;
            }

            cand.clear();
            int minDelta = Integer.MAX_VALUE;
            for (int row = 0; row < 9; row++) {
                for (int i = 0; i < 9; i++) {
                    for (int j = i + 1; j < 9; j++) {
                        int d = this.S.getSwapDelta(this.x[row][i], this.x[row][j]);
                        if(d > 0)
                            continue;
                        if (d < minDelta) {
                            cand.clear();
                            cand.add(new SwapMove(row, i, j));
                            minDelta = d;
                        } else if (d == minDelta) {
                            cand.add(new SwapMove(row, i, j));
                        }
                    }
                }
            }
            int idx = r.nextInt(cand.size());
            SwapMove swapMove = cand.get(idx);
            this.x[swapMove.row][swapMove.i].swapValuePropagate(this.x[swapMove.row][swapMove.j]);

            System.out.println("Step " + iter + ", violations = " + this.S.violations() + " --- Size Candidate = " + cand.size());
            if (cand.size() == 0) {
                System.out.println("Local Optimal");
                break;
            }
        }
    }

    public static void main(String[] args) {
        SudokuLS sudokuLS = new SudokuLS();
        sudokuLS.stateModel();
        sudokuLS.solve();
    }

}

