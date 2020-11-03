package class118133.hoangminhtan.Java;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.Random;

public class Sudoku {
    LocalSearchManager mgr;
    VarIntLS[][] X;
    ConstraintSystem S;
    int MAX_ITERS = (int) 1e6;

    public void stateModel()
    {
        mgr  = new LocalSearchManager();
        X = new VarIntLS[9][9];
        for (int i = 0; i< 9; ++i)
        {
            for (int j= 0; j< 9; ++j)
            {
                X[i][j] = new VarIntLS(mgr, 1, 9);
                X[i][j].setValue(j+1);
            }
        }

        S = new ConstraintSystem(mgr);

        // all different theo hang
        for (int i= 0; i<9; ++i)
        {
            VarIntLS[] y = new VarIntLS[9];
            for (int j = 0; j < 9; ++j)
            {
                y[j] = X[i][j];
            }
            S.post(new AllDifferent(y));
        }

        // ALL DIFFERENT cot
        for (int i= 0; i<9; ++i)
        {
            VarIntLS[] y = new VarIntLS[9];
            for (int j = 0; j < 9; ++j)
            {
                y[j] = X[j][i];
            }
            S.post(new AllDifferent(y));
        }

        // all different theo o vuong
        for (int i3 = 0; i3 < 3; ++i3)
        {
            for (int j3 = 0; j3 < 3; j3 ++)
            {
                VarIntLS[] y = new VarIntLS[9];
                for (int i = 0; i < 3; ++i)
                {
                    for (int j =0; j < 3; j++)
                    {
                        y[i * 3 + j] = X[i3 * 3 + i][j3 * 3 + j];
                    }
                }
                S.post(new AllDifferent(y));
            }
        }
        mgr.close();
    }

    public void search() {
        class Move {
            int i;
            int j1;
            int j2;

            public Move(int i, int j1, int j2) {
                this.i = i;
                this.j1 = j1;
                this.j2 = j2;
            }

            public void print()
            {

            }
        }

        Random Rand = new Random();
        ArrayList<Move> candidates = new ArrayList<Move>();
        int iter = 0;

        while(iter < MAX_ITERS && S.violations() > 0)
        {
            int minDelta = Integer.MAX_VALUE;
            candidates.clear();
            for(int i = 0; i < 9; i++) {
                for (int j1 = 0; j1 < 8; j1++) {
                    for (int j2 = j1 + 1; j2 < 9; j2++) {
                        int delta = S.getSwapDelta(X[i][j1], X[i][j2]);
                        if (delta < minDelta) {
                            candidates.clear();
                            candidates.add(new Move(i, j1, j2));
                            minDelta = delta;
                        } else if (delta == minDelta)
                            candidates.add(new Move(i, j1, j2));
                    }
                }
            }
            System.out.println("Iteration " + (iter+1) + ", violations: " + S.violations());
            Move move = candidates.get(Rand.nextInt(candidates.size()));
            X[move.i][move.j1].swapValuePropagate(X[move.i][move.j2]);
            iter++;
        }

        if (S.violations() > 0)
        {
            System.out.println("Can not find solution in " + MAX_ITERS + "iterations");
        }
        else
        {
            System.out.println("Solution found: ");
            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++)
                    System.out.print(X[i][j].getValue() + " ");
                System.out.println();
            }

        }
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
        sudoku.stateModel();
        sudoku.search();
    }

}
