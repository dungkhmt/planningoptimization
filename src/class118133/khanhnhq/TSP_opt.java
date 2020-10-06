package class118133.khanhnhq;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import Problem.TSP_opt;

public class TSP_opt {
	{
		System.loadLibrary("jniortools");
	}
		
	int N;
//	int[][] c = { { 0, 6, 1, 1 }, { 6, 0, 1, 1 }, { 1, 1, 0, 5 },
//			{ 1, 1, 5, 0 } };
//	
//	int[][] c = {
//		{0, 3, 9, 2, 1, 9, 6, 5, 9, 8, 3, 10, 4}, 
//		{9, 0, 3, 3, 5, 7, 2, 8, 1, 5, 7, 4, 9}, 
//		{1, 2, 0, 5, 9, 9, 1, 7, 2, 8, 2, 8, 5}, 
//		{4, 1, 4, 0, 7, 8, 5, 6, 9, 2, 9, 2, 10}, 
//		{1, 2, 1, 7, 0, 4, 3, 1, 1, 8, 8, 1, 8 },
//		{10, 8, 4, 8, 6, 0, 3, 9, 9, 6, 3, 7, 5 },
//		{7, 9, 10, 5, 10, 1, 0, 7, 10, 1, 9, 2, 8}, 
//		{3, 1, 1, 7, 6, 7, 3, 0, 2, 7, 6, 7, 1 },
//		{9, 7, 5, 2, 7, 8, 10, 10, 0, 5, 5, 3, 4}, 
//		{5, 2, 7, 7 ,9, 9, 1, 4, 1, 0, 5, 2, 7 },
//		{9, 4, 1, 2, 2, 9, 6, 7, 1, 1, 0, 1, 4 },
//		{5, 2, 6, 10, 3, 9, 7, 10, 5, 5, 1, 0, 9}, 
//		{2, 5, 6, 7, 2, 8, 3, 5, 8, 1, 6, 6, 0},
//};
	int[][] c;

	MPSolver solver;
	MPVariable[][] X;
	
	// intermediate data structure for subset generation
	int[] b;// binary sequence representing subsets
	HashSet<Integer> S;
	private void process(){
		//for(int i = 0; i < N; i++){
		//	System.out.print(b[i] + " ");
		//}
		//System.out.println();
		S.clear();
		for(int i = 0; i < N; i++) if(b[i] == 1) S.add(i);
		if(S.size() > 1 && S.size() < N){
			MPConstraint c = solver.makeConstraint(0,S.size()-1);
			for(int i : S)
				for(int j : S) if(i != j){
					c.setCoefficient(X[i][j], 1);
				}
		}
	}
	private void TRY(int k){// thu cac gia tri cho b[k]
		for(int v = 0; v <= 1; v++){
			b[k] = v;
			if(k == N-1){
				process();
			}else{
				TRY(k+1);
			}
		}
	}
	
	private int findNext(int i){
		for(int j = 0; j < N; j++) if(i != j){
			if(Math.abs(X[i][j].solutionValue() - 1) < 0.01) return j;
		}
		return -1;
	}
	
	private int check() {
		S.clear();
		int s = 0;
		S.add(s);
		int cnt = 0;
//		System.out.print(s);
		while(true) {
			int ns = findNext(s);
			s = ns;
//			System.out.print(ns);
			S.add(ns);
			if(ns == 0) {
				break;
			}
			cnt++;
		}
		if(cnt < N - 1) return 0;
		return 1;
	}
	public void loadData(String fn){
		try{
			Scanner in = new Scanner(new File(fn));
			N = in.nextInt();
			c = new int[N][N];
			for(int i = 0; i < N; i++)
				for(int j = 0; j < N; j++)
					c[i][j] = in.nextInt();
			in.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void solve(){
//		N = c.length;
		loadData("src/Problem/tsp-50.txt");
		System.out.println("solve start...");
//		System.out.println(c[0][0]);
		solver = new MPSolver("TSP solver", 
		             MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));

		
		X = new MPVariable[N][N];
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)if(i != j){
				X[i][j] = solver.makeIntVar(0, 1, "X[" + i + "," + j + "]");
			}
		
		// flow balance
		for(int i = 0; i < N; i++){
			MPConstraint c = solver.makeConstraint(1,1);
			for(int j = 0; j < N; j++)if(i != j){
				c.setCoefficient(X[i][j], 1);
			}
			
			c = solver.makeConstraint(1,1);
			for(int j = 0; j < N; j++)if(i != j){
				c.setCoefficient(X[j][i], 1);
			}
		}
		
		// SEG generation
		b = new int[N];
		S = new HashSet<Integer>();
//		TRY(0);
		
		// objective
		MPObjective obj = solver.objective();
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)if(i != j)
				obj.setCoefficient(X[i][j], c[i][j]);
		solver.solve();
		while(check() == 0) {
			
//			MPSolver.ResultStatus stat = solver.solve();
//			for(int i : S) {
//				System.out.print(i + " ");
//			}
//			System.out.println();
			if(S.size() > 1 && S.size() < N){
				MPConstraint c = solver.makeConstraint(0,S.size()-1);
				for(int i : S)
					for(int j : S) if(i != j){
						c.setCoefficient(X[i][j], 1);
					}
			}
			solver.solve();

		}
		MPSolver.ResultStatus stat = solver.solve();
		if (stat != MPSolver.ResultStatus.OPTIMAL) {
		      System.err.println("The problem does not have an optimal solution!");
		      return;
		    }
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++) if(i != j){
				System.out.println("X[" + (i) + "," + (j) + "] = " + X[i][j].solutionValue());
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TSP_opt app = new TSP_opt();
		app.solve();
	}

}
