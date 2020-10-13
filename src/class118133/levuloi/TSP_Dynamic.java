package class118133.levuloi;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class TSP_Dynamic {
	{
		System.loadLibrary("jniortools");
	}
	int N;
	int[][] c;
	MPSolver solver;
	MPVariable[][] X;
	HashSet<Integer> S;
	HashSet<HashSet<Integer>> history = new HashSet<>(); 
	
	private void getData(String filename) {
		try {
			Scanner in = new Scanner(new File(filename));
			N = in.nextInt();
			c = new int[N][N];
			for (int i = 0; i < N; ++i) {
				for (int j = 0; j < N; ++j) {
					c[i][j] = in.nextInt();
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setFlowConstraint() {
		solver = new MPSolver("TSP Solver", MPSolver.OptimizationProblemType.valueOf("CBC_MIXED_INTEGER_PROGRAMMING"));
		getData("data/TSP/tsp-100.txt");
		
		X = new MPVariable[N][N];
		
		// make variables
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i != j) {
					X[i][j] = solver.makeIntVar(0, 1, "X[" + i + "," + j + "]");
				}
			}
		}
		
		// flow balance 
		for (int i = 0; i < N; i++) {
			MPConstraint rowConstraint = solver.makeConstraint(1, 1);
			MPConstraint columnConstraint = solver.makeConstraint(1, 1);
			for (int j = 0; j < N; j++) {
				if (i != j) {
					rowConstraint.setCoefficient(X[i][j], 1);
					columnConstraint.setCoefficient(X[j][i], 1);
				}
			}
		}
		
		// objective
		MPObjective obj = solver.objective();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i != j) {
					obj.setCoefficient(X[i][j], c[i][j]);
				}
			}
		}
	}
	
	private int findNext(int i) {
		for (int j = 0; j < N; j++) {
			if (j != i && X[i][j].solutionValue() == 1) return j;
		}
		return -1;
	}
	
	private boolean detectCycle(){
		
		boolean[] visited = new boolean[N];
		for (int i = 0; i < N; i++) {
			visited[i] = false;
		}
		
		HashSet<Integer> subcycle = new HashSet<>();
		for (int i = 0; i < N; i++) {
			if (!visited[i]) {
				int count = 1;
				subcycle.clear();
				subcycle.add(i);
				visited[i] = true;
				int des = findNext(i);
				
				while (des != i) {
					visited[des] = true;
					subcycle.add(des);
					count++;
					des = findNext(des);
				}
				
				if (count == N) {
					return false;
				}

				history.add(subcycle);
			}
		}
		
		return true;
	}
	
	private void addConstraint(HashSet<Integer> subcycle) {
		MPConstraint c = solver.makeConstraint(0, subcycle.size() - 1);
		for (int i : subcycle) {
			for (int j : subcycle) {
				if (i != j) {
					c.setCoefficient(X[i][j], 1);
				}
			}
		}
	}
	
	public void iterativeSolve() {
		while (true) {
			setFlowConstraint();
			for (HashSet<Integer> constraint: history) {
				addConstraint(constraint);
			}
			solver.solve();
			if (!detectCycle()) break;
		}
	}
	
	public void printVar() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i != j) {
					System.out.print(X[i][j].solutionValue() + "\t");
				}
				else System.out.print(-1 + "\t");
			}
			System.out.println();
		}
	}
	
	public void test() {
		System.out.println(0 + " -> " + findNext(0));
		System.out.println(1 + " -> " + findNext(1));
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
		TSP_Dynamic tsp = new TSP_Dynamic();
		long start = System.currentTimeMillis();
		tsp.iterativeSolve();
		tsp.printTour();
		long end = System.currentTimeMillis();
		System.out.println("Elapsed: " + (end - start));
	}
}
