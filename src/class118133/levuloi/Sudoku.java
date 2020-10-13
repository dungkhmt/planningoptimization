package class118133.levuloi;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Sudoku {
	private Model model;
	private IntVar[][] X;
	private int n = 9;
	
	{
		X = new IntVar[n][n];
	}
	
	private static Model getSudokuModel(IntVar[][] X, int n) {
		
		Model model = new Model("Sudoku");
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				X[i][j] = model.intVar("X[" + i + "," + j + "]", 1, n);
			}
		}
		
		IntVar[][] cols = new IntVar[n][n];
		for (int i = 0; i < n; i++) {
			model.allDifferent(X[i]).post();
			for (int j = 0; j < n; j++) {
				cols[i][j] = X[j][i];
			}
			model.allDifferent(cols[i]).post();
		}
		
		IntVar[][] cells = new IntVar[n][n];
		for (int i = 0; i < 9; i++) {
			int row = i / 3;
			int col = i % 3;
			
			for (int j = 0; j < 9; j++) {
				int innerRow = j / 3;
				int innerCol = j % 3;
				cells[i][j] = X[row * 3 + innerRow][col * 3 + innerCol];
			}
			model.allDifferent(cells[i]).post();
		}
		
		return model;
	}
	
	public void defineModel(Model model) {
		this.model = model;
	}
	
	public void printResult() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.printf("%d ", X[i][j].getValue());
				if (j % 3 == 2) {
					System.out.printf("| ");
				}
			}
			System.out.println();
			if (i % 3 == 2) {
				System.out.println("---------------------");
			}
		}
	}
	
	public void solve() {
		model.getSolver().solve();
	}
	
	public static void main(String[] args) {
		Sudoku ins = new Sudoku();
		ins.defineModel(Sudoku.getSudokuModel(ins.X, ins.n));
		ins.solve();
		ins.printResult();
	}
}
