package class118133.levuloi;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class NQueen {
	private Model model;
	private IntVar[] X;
	private int n = 8;
	
	{
		X = new IntVar[n];
	}
	
	public void defineModel(Model model) {
		this.model = model;
	}
	
	public void printResult() {
		for (int i = 0; i < n; i++) {
			System.out.println(X[i]);
		}
	}
	
	public void solve() {
		model.getSolver().solve();
	}
	
	public static void main(String[] args) {
		NQueen ins = new NQueen();
		ins.defineModel(RetrieveModel.getNQueenModel(ins.X, ins.n));
		ins.solve();
		ins.printResult();
	}
}

class RetrieveModel{
	public static Model getNQueenModel(IntVar[] X, int n) {
		Model model = new Model("N-Queen");
		for (int i = 0; i < n; i++) {
			X[i] = model.intVar("X[" + i + "]", 1, n);
		}

		IntVar[] d1 = new IntVar[n];
		IntVar[] d2 = new IntVar[n];
		for (int i = 0; i < n; i++) {
			d1[i] = model.intOffsetView(X[i], i);
			d2[i] = model.intOffsetView(X[i], -i);
		}
		
		model.allDifferent(X).post();
		model.allDifferent(d1).post();
		model.allDifferent(d2).post();
		
		return model;
	}
}
