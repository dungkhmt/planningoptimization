package khanhnhq;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import java.util.Scanner;

public class TSP_1d {
	int N = 100;
	int[][] c = new int [N+1][N+1];
	public void input() {
		Scanner scanner = new Scanner( System.in );
		int number = scanner.nextInt();
		N = number;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				number = scanner.nextInt();
				c[i][j] = number;
			}
		}
		for(int i = 0; i < N; i++) {
			c[N][i] = c[0][i];
			c[i][N] = c[i][0];
		}
	}
	public void solve() {
		Model model = new Model("TSP_1d");
		
		int upperbound = 0;
		for(int i = 1; i <= N; i++) {
			upperbound += c[i][i-1];
		}
//		System.out.println(upperbound);
		
        IntVar[] X = model.intVarArray("X" , N, 1, N);
        IntVar[] Y = model.intVarArray("sum", N+1, 0, upperbound);
        for(int i = 0; i < N; i++) {
        	model.arithm(X[i], "!=", i).post();
        }
        for(int i = 0; i < N; i++) {
        	for(int j = i + 1; j < N; j++) {
        		model.arithm(X[i], "!=", X[j]).post();;
        	}
        }
        
        for(int i = 0; i < N; i++) {
        	for(int j = 1; j <= N; j++) {
        		(X[i].eq(j)).imp(Y[j].eq(Y[i].add(c[i][j]))).post();
        	}
        }
        
//        model.setObjective(Model.MINIMIZE, Y[N]);
    	
        Solution solution = model.getSolver().findOptimalSolution(Y[N], false);
        if(solution != null){

     		System.out.println("Do dai chu trinh");
     		System.out.println(solution.getIntVal(Y[N]));
         	for (int i = 0; i < N; ++i) {
         		System.out.println("Dinh sau dinh thu  " + i);
                 System.out.println("X[" + i + "] = " + solution.getIntVal(X[i]));
             }
         }
         else {
         	System.out.println("No solution");
         }
       

	}
    public static void main(String[] args) {
    		TSP_1d app = new TSP_1d();
    		app.input();
            app.solve();
            
        }
}
