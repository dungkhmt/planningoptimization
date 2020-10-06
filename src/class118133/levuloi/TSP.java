package linear;

import java.util.HashSet;

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
	
	// intermediate data structure for subset generation
	int[] b; // binary sequence representing subsets
	HashSet<Integer> S;
	
	private void process() {
		S.clear();
		for (int i = 0; i < N; i++) {
			if (b[i] == 1) S.add(i);
		}
		if (S.size() > 1 && S.size() < N){
			MPConstraint c = solver.makeConstraint(0, S.size() - 1);
			for (int i : S) {
				for (int j : S) {
					c.setCoefficient(X[i][j], 1);
				}
			}
		}
	}
	
	private void TRY(int k) {
		for (int v = 0; v <= 1; v++) {
			b[k] = v;
			if (k == N - 1) {
				process();
			}
			else {
				TRY(k + 1);
			}
		}
	}
	
	public void solve() {
		System.out.println("solve start...");
		
		solver = new MPSolver("TSP solver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
		N = c.length;
		
		X = new MPVariable[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i != j) {
					X[i][j] = solver.makeIntVar(0, 1, "X[" + i + "," + j + "]");
				}
			}
		}
		
		// flow balance constraint
		for (int i = 0; i < N; i++) {
			MPConstraint c = solver.makeConstraint(1, 1);
			for (int j = 0; j < N; j++) {
				if (i != j) {
					c.setCoefficient(X[i][j], 1);
				}
			}
			
			c = solver.makeConstraint(1, 1);
			for (int j = 0; j < N; j++) {
				if (i != j) {
					c.setCoefficient(X[j][i], 1);
				}
			}
		}
		
		// SEG generation
		b = new int[N];
		S = new HashSet<Integer>();
		TRY(0);
		
		// objective
		MPObjective obj = solver.objective();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i != j) {
					obj.setCoefficient(X[i][j], c[i][j]);
				}
			}
		}
		
		MPSolver.ResultStatus stat = solver.solve();
		if (stat != MPSolver.ResultStatus.OPTIMAL) {
			System.err.println("The problem does not have an optimal solution");
			
		}
	}
	
	private int findNext(int i) {
		for (int j = 0; j < N; j++) {
			if (j != i && X[i][j].solutionValue() == 1) return j;
		}
		return -1;
	}
	
	public void printTour() {
		int i = 0;
		int count = 0;
		while (true) {
			System.out.printf("%d -> ", i + 1);
			count++;
			if (count == N) break;
			i = findNext(i);
		}
		System.out.println(findNext(i) + 1);
	}
	
	public static void main(String[] args) {
		TSP tsp = new TSP();
		tsp.solve();
		for (int i = 0; i < tsp.N; i++) {
			for (int j = 0; j < tsp.N; j++) {
				if (i != j) {
					System.out.printf("X[%d][%d] = %.1f\n",i, j, tsp.X[i][j].solutionValue());
				}
			}
		}
		tsp.printTour();
	}
}
