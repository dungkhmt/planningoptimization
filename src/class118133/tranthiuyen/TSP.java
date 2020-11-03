package class118133.tranthiuyen;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class TSP {
	{
		System.loadLibrary("jniortools");
	}

	int N;
	int[][] c ;
	MPSolver solver;
	MPVariable[][] X;

	// intermediate data structure for subset generation
	int[] b;// binary sequence representing subsets
	HashSet<Integer> S = new HashSet<>();
	private void readFile(){
		try {
			Scanner scanner = new Scanner(new File(".\\data\\TSP\\tsp-50.txt"));
			N = scanner.nextInt() ;
			c = new int[N][N] ;
			for (int i = 0 ; i < N ; i ++ )
				for (int j = 0 ; j < N ; j++){
					c[i][j] = scanner.nextInt() ;
				}
		}catch (Exception e){

		}
	}
	private void process(List<Integer> output) {
		S.clear();
		S.addAll(output);
		if (S.size() > 1 && S.size() < N) {
			MPConstraint c = solver.makeConstraint(0, S.size() - 1);
			for (int i : S)
				for (int j : S)
					if (i != j) {
						c.setCoefficient(X[i][j], 1);
					}
		}
	}

//	private void TRY(int k) {// thu cac gia tri cho b[k]
//		for (int v = 0; v <= 1; v++) {
//			b[k] = v;
//			if (k == N - 1) {
//				process();
//			} else {
//				TRY(k + 1);
//			}
//		}
//	}

	public void solve() {
		System.out.println("solve start...");

		solver = new MPSolver("TSP solver",
				MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
		N = c.length;

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

//		// SEG generation
//		b = new int[N];
//		S = new HashSet<Integer>();
//		TRY(0);

		// objective
		MPObjective obj = solver.objective();
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (i != j)
					obj.setCoefficient(X[i][j], c[i][j]);
		while (true) {
			MPSolver.ResultStatus stat = solver.solve();
			if (stat != MPSolver.ResultStatus.OPTIMAL) {
				System.err.println("The problem does not have an optimal solution!");
				return;
			}
			List<Integer> output = extractChuTrinh(X);
			if (output != null ){
				process(output);
			} else return;

		}

//		int u = 0;
//		int sum = 0;
//		System.out.println(u);
//		while (true) {
//			for (int i = 0; i < N; i++)
//				if (i != u && X[u][i].solutionValue() == 1) {
//					System.out.println(i);
//					u = i;
//					sum++;
//					if (sum == N - 1) break;
//				}
//			if (sum == N - 1) break;
//		}

	}
	public List<Integer> extractChuTrinh(MPVariable[][] X  ) {
		int u = 0;
		int sum = 0;
		List<Integer> output = new ArrayList<>();
		int[] luu = new int[N];
		boolean[] fre = new boolean[N];
		for (int i = 0; i < N; i++) fre[i] = true;
		fre[0] = false;
		luu[0] = 0;
		while (true) {
			for (int i = 0; i < N; i++)
				if (i != u && X[u][i].solutionValue() == 1) {
					luu[++sum] = i;
					u = i;
					if (sum == N - 1) break;
					if (!fre[i]) {
						int start = 0;
						for (start = 0; start < sum; start++)
							if (luu[start] == i) break;

						for (int j = start; j < sum; j++) {
							output.add(luu[j]);
						}
						return output;
					} else
						fre[i] = false;

				}
			if (sum == N - 1) break;
		}
//		for (int i : output) {
//			System.out.println(i);
//		}
		System.out.println("obj: " + solver.objective().value() );
		for (int i : luu) {
			System.out.print(i + "--> ");
		}
		return  null ;
	}
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		TSP app = new TSP();
		app.readFile();
		app.solve();
	}

}
