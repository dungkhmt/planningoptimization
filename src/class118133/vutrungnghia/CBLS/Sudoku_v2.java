package class118133.vutrungnghia.CBLS;
//
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.constraints.alldifferent.AllDifferentVarIntLS;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Sudoku_v2 {
    int[][] input;
    int N;

    LocalSearchManager mgr;
    VarIntLS[][] X;
    ConstraintSystem constraints;
    Random random = new Random();

    private static class Move{
        int i1;
        int i2;
        int row;
        public Move(int row, int i1, int i2){
            this.i1 = i1;
            this.i2 = i2;
            this.row = row;
        }
    }

    ArrayList<Move> candidates = new ArrayList<>();


    private void readInput(){
        N = 9;
        try{
            input = new int[N][N];
            Scanner in = new Scanner(new File("src/class118133/vutrungnghia/CBLS/Sudoku_input.txt"));
            for(int i = 0; i < N; ++i){
                for(int j = 0; j < N; ++j){
                    input[i][j] = in.nextInt();
                }
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stateModel(){
        mgr = new LocalSearchManager();

        // value space
        X = new VarIntLS[N][N];
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                if(input[i][j] == 0)
                    X[i][j] = new VarIntLS(mgr, 1, N);
                else
                    X[i][j] = new VarIntLS(mgr, input[i][j], input[i][j]);
            }
        }

        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                X[i][j].setValuePropagate(j + 1);
            }
        }

        // constraint
        constraints = new ConstraintSystem(mgr);
        for(int i = 0; i < N; ++i){
            VarIntLS[] row = new VarIntLS[N];
            for(int j = 0; j < N; ++j)
                row[j] = X[i][j];
            constraints.post(new AllDifferent(row));

            VarIntLS[] col = new VarIntLS[N];
            for(int j = 0; j < N; ++j)
                col[j] = X[j][i];
            constraints.post(new AllDifferent(col));

            for(int iRow = 0; iRow < 3; ++iRow){
                for(int iCol = 0; iCol < 3; ++iCol){
                    VarIntLS[] block = new VarIntLS[N];
                    int counter = 0;
                    for(int j = 0; j < 3; ++j){
                        for(int k = 0; k < 3; ++k){
                            block[counter] = X[j + iRow * 3][k + iCol * 3];
                            counter++;
                        }
                    }
                    constraints.post(new AllDifferent(block));
                }
            }
        }

        mgr.close();
    }

    private void exploreNeighborhood(){
        candidates.clear();
        int minDelta = Integer.MAX_VALUE;
        for(int i = 0; i < N; ++i){
            for(int j1 = 0; j1 < N; ++j1){
                for(int j2 = j1 + 1; j2 < N; ++j2){
                    int delta = constraints.getSwapDelta(X[i][j1], X[i][j2]);
                    if(delta > 0)
                        continue;
                    if(delta < minDelta){
                        candidates.clear();
                        candidates.add(new Move(i, j1, j2));
                        minDelta = delta;
                    }
                    else if(delta == minDelta)
                        candidates.add(new Move(i, j1, j2));
                }
            }
        }
    }


    public void search(int maxIterations) {
        candidates.clear();
        double t0 = System.currentTimeMillis();
        for (int it = 0; it <= maxIterations; ++it) {
            exploreNeighborhood();

            if (constraints.violations() == 0) {
                System.out.println("Reach global solution");
                break;
            } else if (candidates.size() == 0) {
                System.out.println("Reach local solution");
                break;
            }

            Move mv = candidates.get(random.nextInt(candidates.size()));
            int v1 = X[mv.row][mv.i1].getValue();
            int v2 = X[mv.row][mv.i2].getValue();
            X[mv.row][mv.i1].setValuePropagate(v2);
            X[mv.row][mv.i2].setValuePropagate(v1);
            System.out.println("Step: " + it + " Move: " + mv.row + " " + mv.i1 + " " + mv.i2 + " No.violations: " + constraints.violations());
        }
    }

    public void print(){
        for(int i = 0; i < N; ++i){
            for(int j = 0; j < N; ++j){
                if (input[i][j] != 0)
                    System.out.print( "[" + X[i][j].getValue() + "] ");
                else
                    System.out.print( " " + X[i][j].getValue() + "  ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Sudoku_v2 sudoku = new Sudoku_v2();
        sudoku.readInput();
        sudoku.stateModel();
        sudoku.search(100000);
        sudoku.print();
    }
}
