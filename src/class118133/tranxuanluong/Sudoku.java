import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class CSP {
   
    public void initBoardGame(int a[][],int n){
    	/*
        9 0 5 0 0 0 0 0 8
	4 0 0 5 7 0 1 0 6
	0 2 7 6 0 0 0 4 0
	0 9 6 0 0 3 5 1 2
	7 0 4 0 1 0 3 0 0
	2 1 0 9 8 0 0 0 4
	0 8 1 0 0 4 0 9 0
	3 0 0 8 0 0 0 5 1
	0 0 2 0 0 7 0 6 0
	*/
	
        try {
            File myObj = new File("/home/luong/Desktop/sudoku.txt");
            Scanner myReader = new Scanner(myObj);
            for(int i = 0; i < n;i++){
                for (int j = 0; j < n; j++){
                    a[i][j] = myReader.nextInt();
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    // Sudoku 9 x 9
    public void sudoku(){
        int n = 9;
        int [][]a = new int[n][n];
        initBoardGame(a,n);
        Model model = new Model();
        IntVar [][]vars = new IntVar[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if(a[i][j] != 0){
                    vars[i][j] = model.intVar(String.valueOf(i*n+j),a[i][j],a[i][j]);
                }
                else{
                    vars[i][j] = model.intVar(String.valueOf(i*n+j),1,9);
                }
            }
        }
        // cac phan tu cua hang khac nhau
        for(int row = 0; row < n; row++){

            for(int i = 0; i < n - 1; i++){
                for (int j = i+1; j < n; j++){
                    model.arithm(vars[row][i],"!=",vars[row][j]).post();
                }
            }
        }
        // cac phan tu tren cot khac nhau
        for (int col = 0; col < n; col ++){
            for(int i = 0; i < n - 1; i++){
                for (int j = i+1; j < n; j++){
                    model.arithm(vars[i][col],"!=",vars[j][col]).post();
                }
            }
        }
        // cac phan tu trong o 3 x 3 doi mot khac nhau => dieu kien nay da duoc thoa man tu 2 dieu kien truoc
        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
        }
    }
    public static void main(String[] args) {
        CSP csp = new CSP();
        csp.sudoku();

    }
}

