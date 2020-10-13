package class118133.pqd;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class TSP {
	{
		System.loadLibrary("jniortools");
	}
	int N;
	int[][] c = {
			{0,6,1,1},
			{6,0,1,1},
			{1,1,0,5},
			{1,1,5,0}
	};
	MPSolver solver;
	MPVariable[][] X;
	MPObjective obj ;
	
	// intermediate data structure for subset generation
	int[] b;// binary sequence representing subsets
	HashSet<Integer> S;
	Random R = new Random();
	
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
			} else {
				TRY(k + 1);
			}
		}
	}

	public void genData(String fn, int N) {
		try {
			PrintWriter out = new PrintWriter(fn);
			c = new int[N][N];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					c[i][j] = R.nextInt(10) + 1;
				}
			}
			out.println(N);
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++)
					if (i != j)
						out.print(c[i][j] + " ");
					else
						out.print(0 + " ");
				out.println();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private int findNext(int i) {
		for (int j = 0; j < N; j++)
			if (i != j) {
				if (Math.abs(X[i][j].solutionValue() - 1) < 0.01)
					return j;
			}
		return -1;
	}

	public void printTour() {
		int s = 0;
		System.out.print(s);
		while (true) {
			int ns = findNext(s);
			if (ns == -1)
				break;
			System.out.print(" -> " + ns);
			if (ns == 0)
				break;
			s = ns;
		}
	}

	public void loadData(String fn) {
		try {
			Scanner in = new Scanner(new File(fn));
			N = in.nextInt();
			c = new int[N][N];
			for (int i = 0; i < N; i++)
				for (int j = 0; j < N; j++)
					c[i][j] = in.nextInt();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
<<<<<<< HEAD
	public void solve() {
		System.out.println("solve start...");
		//genData("data/TSP/tsp-10.txt",10);
		loadData("data/TSP/tsp-13.txt");
		
		
=======

	public void createSolverDynSEC(HashSet<ArrayList<Integer>> C) {
>>>>>>> c0a26da57a3e8efef0f0d533eff67648237689b2
		solver = new MPSolver("TSP solver",
				MPSolver.OptimizationProblemType
						.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));

		X = new MPVariable[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (i != j) {
					X[i][j] = solver.makeIntVar(0, 1, "X[" + i + "," + j + "]");
				}

		// flow balance
		for (int i = 0; i < N; i++) {
			MPConstraint c = solver.makeConstraint(1, 1);
			for (int j = 0; j < N; j++)
				if (i != j) {
					c.setCoefficient(X[i][j], 1);
				}

			c = solver.makeConstraint(1, 1);
			for (int j = 0; j < N; j++)
				if (i != j) {
					c.setCoefficient(X[j][i], 1);
				}
		}
		
		// SEC
		for(ArrayList<Integer> S: C){
			MPConstraint c = solver.makeConstraint(0,S.size() -1);
			for(int i : S)
				for(int j : S) if(i != j)
					c.setCoefficient(X[i][j], 1);
		}
		
		// objective
		obj = solver.objective();
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (i != j)
					obj.setCoefficient(X[i][j], c[i][j]);

	}
	private ArrayList<Integer> extractTour(int s){
		int v = s;
		ArrayList<Integer> T = new ArrayList<Integer>();
		while(true){
			T.add(v);
			int nv = findNext(v); 
			if(nv != s){
				v = nv;
			}else{
				break;
			}			
		}
		return T;
	}
	private String toString(ArrayList<Integer> T){
		String s= "(";
		for(int v: T)
			s += v + " ";
		return s + ")";
	}
	public void solveDynSEC(){
		HashSet<ArrayList<Integer>> C = new HashSet<>();
		boolean[] mark = new boolean[N];
		
		boolean found = false;
		for(int it = 1; it <= 5000; it++){
			createSolverDynSEC(C);
			MPSolver.ResultStatus stat = solver.solve();
			System.out.println("get obj = " + obj.value());
			
			//C.clear();
			
			// detect sub-tours
			for(int i = 0; i < N; i++) mark[i] = false;
			for(int s = 0; s < N; s++) if(!mark[s]){
				ArrayList<Integer> ST = extractTour(s);
				if(ST.size() == N){
					System.out.println("FOUND optimal solution!! cost = " + obj.value() + " tour = " + toString(ST));
					found = true;
					break;
				}else{
					for(int v: ST){
						mark[v] = true;						
					}
					C.add(ST);
					System.out.println("sub-tour detected " + toString(ST));
				}
			}
			if(found) break;
		}
	}
		
	public void solve(){
		System.out.println("solve start...");
		
		solver = new MPSolver("TSP solver", 
				MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
		N = c.length;
		
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
		TRY(0);
		
		// objective
		MPObjective obj = solver.objective();
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)if(i != j)
				obj.setCoefficient(X[i][j], c[i][j]);
		
		MPSolver.ResultStatus stat = solver.solve();
		if (stat != MPSolver.ResultStatus.OPTIMAL) {
		      System.err.println("The problem does not have an optimal solution!");
		      return;
		    }
		for(int i = 0; i < N; i++){
			for(int j = 0; j < N; j++) if(i != j){
				System.out.println("X[" + i + "," + j + "] = " + X[i][j].solutionValue());
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TSP app = new TSP();
		app.loadData("data/TSP/tsp-100.txt");
		//app.solve();
		app.solveDynSEC();
	}

}