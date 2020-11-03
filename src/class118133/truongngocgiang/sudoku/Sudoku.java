package class118133.truongngocgiang.sudoku;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.basic.NotEqual;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;
// import localsearch.search.Move;

public class Sudoku {
    public static void main(String[] args) {
        int N = 9;
        LocalSearchManager mng = new LocalSearchManager();
        VarIntLS[][] X = new VarIntLS[N][N];
        ConstraintSystem S = new ConstraintSystem(mng);

        for (int i = 0; i < N; ++i)
        for (int j = 0; j < N; ++j) {
            X[i][j] = new VarIntLS(mng, 1, N);
            X[i][j].setValue(j+1);
        }

        for (int i = 0; i < N; ++i) {
            for (int j1 = 0; j1 < N; ++j1)
            for (int j2 = j1+1; j2 < N; ++j2) {
                S.post(new NotEqual(X[i][j1], X[i][j2]));
                S.post(new NotEqual(X[j1][i], X[j2][i]));
            }
        }

        for (int i = 0; i < N; i += 3)
        for (int j = 0; j < N; j += 3) {
            for (int x1 = 0; x1 < 9; ++x1)
            for (int x2 = x1+1; x2 < 9; ++x2) {
                int u1 = i + (int) x1/3;
                int v1 = j + x1%3;
                int u2 = i + (int) x2 / 3;
                int v2 = j + x2%3;

                S.post(new NotEqual(X[u1][v1], X[u2][v2]));
            }
        }

        mng.close();

        // HillClimbing hc = new HillClimbing();
        // hc.hillClimbing(S, 1000000);
        Sudoku sdk = new Sudoku();

        Random R = new Random();
        ArrayList<Move> candidates = new ArrayList<Move>();
        for (int it = 0; it < 1000000; ++it) {  
            candidates.clear();
            int minDelta = Integer.MAX_VALUE;
            
            for (int i = 0; i < N; ++i)
                for (int j1 = 0; j1 < N; ++j1)
                for (int j2 = j1+1; j2 < N; ++j2) {
                    int delta = S.getSwapDelta(X[i][j1], X[i][j2]);
                    if (delta < minDelta) {
                        candidates.clear();
                        candidates.add(sdk.new Move(i, j1, j2));
                        
                        minDelta = delta;
                    }
                    else if (delta == minDelta) {
                        candidates.add(sdk.new Move(i, j1, j2));
                    }
                }
            
            int idx = R.nextInt(candidates.size());
            Move m = candidates.get(idx);
            int i = m.i; int j1 = m.j1, j2 = m.j2;
            X[i][j1].swapValuePropagate(X[i][j2]);

            System.out.println("Step " + i + ", violations = " + S.violations());
            if (S.violations() == 0) break;
        }

        for (int i = 0; i < N; ++i)
        for (int j = 0; j < N; ++j)
            System.out.print(X[i][j].getValue() + (j==N-1 ? "\n" : " "));
    }

    class Move {
        int i;
        int j1;
        int j2;
        public Move(int i, int j1, int j2) {
            this.i = i; this.j1 = j1; this.j2 = j2;
        }
    }    
}
