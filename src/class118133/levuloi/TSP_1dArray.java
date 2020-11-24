package class118133.levuloi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class TSP_1dArray {
	private int N;
	private IntVar[] X;
	private IntVar[] Y;
	private int[][] d;
	private Model model;
	private int MAX_VALUE;
	
	private void readFile() {
		String content = null;
		try {
			byte[] bytes = Files.readAllBytes(Paths.get("data/TSP/tsp-5.txt"));
			content = new String(bytes, StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] lines = content.split("\n");
		N = Integer.parseInt(lines[0]);
		
		d = new int[N + 1][N + 1];
		for (int i = 0; i < N; i++) {
			String line = lines[i + 1];
			String[] numbers = line.split(" ");
			for (int j = 0; j < N; j++) {
				d[i][j] = Integer.parseInt(numbers[j]);
			}
		}
		
		for (int i = 0; i < N + 1; i++) {
			d[i][N] = d[i][0];
			d[N][i] = d[0][i];
		}
		
		int sum = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				sum += d[i][j];
			}
		}
		MAX_VALUE = sum;
	}
	
	public void printMatrix() {
		for (int i = 0; i < N + 1; i++) {
			for (int j = 0; j < N + 1; j++) {
				System.out.printf("%d\t", d[i][j]);
			}
			System.out.println();
		}
	}
	
	public void stateModel() {
		model = new Model("TSP using constraint programming");
		X = new IntVar[N];
		Y = new IntVar[N + 1];
		for (int i = 0; i < N; i++) {
			X[i] = model.intVar("X[" + i + "]", 1, N);
			Y[i + 1] = model.intVar("Y[" + (i + 1) + "]", 0, MAX_VALUE);
		}
		Y[0] = model.intVar("Y[0]", 0, 0);
		
		model.allDifferent(X).post();
		for (int i = 0; i < N; i++) {
			model.arithm(X[i], "!=", i).post();
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 1; j < N + 1; j++) {
				X[i].eq(j).imp(Y[j].eq(Y[i].add(d[i][j]))).post();
			}
		}
		
		model.setObjective(false, Y[N]);
	}
	
	public void solve() {
		Solver solver = model.getSolver();
		solver.solve();
	}
	
	public void printResult() {
		int count = 0;
		int idx = 0;
		while (true) {
			System.out.print(idx + " ");
			idx = X[idx].getValue();
			count++;
			if (count == N) break;
		}
		System.out.println();
		System.out.printf("Objective = %d\n", Y[N].getValue());
	}
	
	public static void main(String[] args) {
		TSP_1dArray app = new TSP_1dArray();
		app.readFile();
		app.stateModel();
		app.solve();
		app.printResult();
	}
}
