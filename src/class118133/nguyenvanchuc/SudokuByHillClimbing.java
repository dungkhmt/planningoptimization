package class118133.nguyenvanchuc;

import class118133.pqd.GenericHillClimbingSearch;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.Random;



public class SudokuByHillClimbing {
    LocalSearchManager manager;
    VarIntLS[][] X;
    ConstraintSystem S;


    class Move{
        int row;
        int firstCol;
        int secondCol;

        public Move(int row, int firstCol, int secondCol){
            this.row= row;
            this.firstCol= firstCol;
            this.secondCol= secondCol;
        }
    }
    private void setConstraint(){
        this.manager= new LocalSearchManager();
        this.X= new VarIntLS[9][9];
        this.S= new ConstraintSystem(this.manager);

        for (int i=0;i<9; i++){
            for (int j=0;j<9; j++){
                X[i][j]= new VarIntLS(manager, 1,9);
                X[i][j].setValue(j+1);
            }
        }

        // add row constraint
        for(int i = 0; i < 9; i++){
            VarIntLS[] y = new VarIntLS[9];
            for(int j = 0; j < 9; j++)
                y[j] = X[i][j];
            S.post(new AllDifferent(y));
        }

        //add col constraint
        for (int i=0; i<9; i++){
            VarIntLS[] y= new VarIntLS[9];
            for (int j=0; j<9; j++){
                y[j]=X[j][i];
            }
            S.post(new AllDifferent(y));
        }

        // add subsquare constraint
        for(int I = 0; I <= 2; I++){
            for(int J = 0; J <= 2; J++){
                VarIntLS[] y = new VarIntLS[9];
                int index = -1;
                for(int i = 0; i <= 2; i++)
                    for(int j = 0; j <= 2; j++){
                        index++;
                        y[index] = X[3*I+i][3*J+j];
                    }
                S.post(new AllDifferent(y));
            }
        }
        this.manager.close();
    }
    private void solve(){
        Random random = new Random();
        ArrayList<SudokuByHillClimbing.Move> candidates = new ArrayList<SudokuByHillClimbing.Move>();
        for (int it = 0; it <= 100000 && S.violations() > 0; it++) {
            candidates.clear();
            int minDelta = Integer.MAX_VALUE;
            for (int i = 0; i < 9; ++i) {
                for (int j1 = 0; j1 < 8; ++j1) {
                    for (int j2 = j1 + 1; j2 < 9; j2++) {
                        int delta = S.getSwapDelta(X[i][j1], X[i][j2]);
                        if (delta < minDelta) {
                            candidates.clear();
                            minDelta = delta;
                        }

                        if (delta == minDelta) {
                            candidates.add(new SudokuByHillClimbing.Move(i, j1, j2));
                        }
                    }
                }
            }
            SudokuByHillClimbing.Move moveInfo = candidates.get(random.nextInt(candidates.size()));
            X[moveInfo.row][moveInfo.firstCol].swapValuePropagate(X[moveInfo.row][moveInfo.secondCol]);

            System.out.println("iteration: "+ it +" violation: "+ S.violations());
        }
    }
    private void printSudoku(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++)
                System.out.print(X[i][j].getValue() + " ");
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SudokuByHillClimbing app = new SudokuByHillClimbing();
        app.setConstraint();
        app.solve();
        app.printSudoku();
    }
}
