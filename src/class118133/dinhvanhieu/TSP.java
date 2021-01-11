package dinhvanhieu;

import java.io.File;
import java.util.Scanner;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class TSP {
	{
        System.loadLibrary("jniortools");
    }
	String filename;
	int INF = 10000000;
    int N;
    int[][] c;
    MPSolver solver;
    MPVariable[][] x;
    MPVariable[] y;
    
    TSP(String filename){
    	this.filename = filename;
    }
    
    private void get_data(){
        try{
            Scanner in = new Scanner(new File(filename));
            N = in.nextInt();
            c = new int[N+1][N+1];
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    c[i][j] = in.nextInt();
                }
            }
            in.close();
            for(int i = 0; i < N; i++) {
            	c[i][N] = c[i][0];
            	c[N][i] = c[0][i];
            }
            for (int i = 0; i <= N; i++) {
            	for (int j = 0; j <= N; j++) {
            		System.out.print(c[i][j] + " ");
            	}
            	System.out.println();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
	
    public void solve(){
        System.out.println("solve start...");
        get_data();
        solver = new MPSolver("TSP solver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
        N++;
        x = new MPVariable[N][N];
        for(int i = 0; i < N; i++){
            for (int j = 0; j < N; j++) if (i!=j){
                x[i][j] = solver.makeIntVar(0, 1, "x[" + i + "," + j + "]");
            }
        }
        y = new MPVariable[N];
        for (int i = 0; i < N; i++) {
        	y[i] = solver.makeIntVar(0, INF, "y[" + i + "]");
        }

        // flow balance
        for(int i = 0; i < N-1; i++) {
            MPConstraint c = solver.makeConstraint(1, 1);
            for(int j = 1; j < N; j++) if (i != j) {
                c.setCoefficient(x[i][j], 1);
            }
        }
        
        for(int j = 0; j < N-1; j++) {
            MPConstraint c = solver.makeConstraint(1, 1);
            for(int i = 1; i < N; i++) if (i != j) {
                c.setCoefficient(x[j][i], 1);
            }
        }
        for (int i = 0; i < N-1; i++) {
        	for (int j = 1; j < N; j++) {
        		MPConstraint c1 = solver.makeConstraint(c[i][j]-INF, 2*INF);
        		c1.setCoefficient(y[j], 1);
        		c1.setCoefficient(y[i], -1);
        		c1.setCoefficient(x[i][j], -INF);
        		
        		MPConstraint c2  = solver.makeConstraint(-c[i][j]-INF, 2*INF);
        		c2.setCoefficient(y[j], -1);
        		c2.setCoefficient(y[i], 1);
        		c2.setCoefficient(x[i][j], -INF);
        	}
        }
        
        MPObjective obj = solver.objective();
        obj.setCoefficient(y[N-1], 1);
        obj.setMinimization();
        
        MPSolver.ResultStatus stat = solver.solve();
        if (stat != MPSolver.ResultStatus.OPTIMAL) {
        	System.out.println("The problem does not have an optimal solution!!!");
        	return;
        }
        System.out.println("Objective = " + obj.value());
        for (int i = 0; i < N; i++) {
        	for (int j = 0; j < N; j++) {
        		if (i != j) {
        			System.out.println("x[" + i + "," + j + "] = " + x[i][j].solutionValue());
        		}
        	}
        }
        for (int i = 0; i < N; i++) {
        	System.out.println("y[" + i + "] = " + y[i].solutionValue());
        }
    }
        
	public static void main(String[] args) {
		System.out.println("Hello");
		TSP app = new TSP("data/TSP/tsp-5.txt");
		app.solve();
	}
}


