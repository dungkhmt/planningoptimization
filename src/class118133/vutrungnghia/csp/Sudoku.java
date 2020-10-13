package class118133.vutrungnghia.csp;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.util.Scanner;

public class Sudoku {
    int[][] input;
    int N;
    Model model = new Model("Sudoku");

    private void readInput(){
        try{
            input = new int[N][N];
            Scanner in = new Scanner(new File("src/class118133/vutrungnghia/csp/sudoku_dataset.txt"));
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

    private void solve(){
        N = 9;
        readInput();
        IntVar[][] X = new IntVar[N][N];
        for (int i = 0; i < N; ++i) {
            for(int j = 0; j < N; ++j){
                if(input[i][j] == 0)
                    X[i][j] = model.intVar("X[" + i + "]", 1, N);
                else
                    X[i][j] = model.intVar("X[" + i + "]", input[i][j], input[i][j]);
            }
        }

        for(int i = 0; i < N; ++i) {
            IntVar[] row = new IntVar[N];
            IntVar[] col = new IntVar[N];
            for (int j = 0; j < N; ++j) {
                row[j] = X[i][j];
                col[j] = X[j][i];
            }
            model.allDifferent(row).post();
            model.allDifferent(col).post();
        }

        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j){
                IntVar[] block = new IntVar[N];
                int counter = 0;
                for(int row = 0; row < 3; ++row){
                    for(int col = 0; col < 3; ++col){
                        block[counter] = X[i * 3 + row][j * 3 + col];
                        counter++;
                    }
                }
                model.allDifferent(block).post();
            }
        }

        model.getSolver().solve();
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
        sudoku.solve();
    }
}
