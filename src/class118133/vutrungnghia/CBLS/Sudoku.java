package class118133.vutrungnghia.CBLS;
//
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.constraints.alldifferent.AllDifferentVarIntLS;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;

import java.io.File;
import java.util.Scanner;

public class Sudoku {
    int[][] input;
    int N;

    LocalSearchManager mgr;
    VarIntLS[][] X;
    ConstraintSystem constraints;

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

    public void hillClimbing(){
        GenericHillClimbSearch genericHillClimbSearch = new GenericHillClimbSearch(constraints);
        genericHillClimbSearch.search(100000);
//        HillClimbing hillClimbing =  new HillClimbing();
//        hillClimbing.hillClimbing(S, 1000);
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
        Sudoku sudoku = new Sudoku();
        sudoku.readInput();
        sudoku.stateModel();
        sudoku.hillClimbing();
        sudoku.print();
    }
}
