import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class FirstCP {

	public static void main(String[] args) {
		int n = 8;
		Model model = new Model("Queen");
		IntVar[] x = new IntVar[n];
		IntVar[] d1 = new IntVar[n];
		IntVar[] d2 = new IntVar[n];
		for (int i = 0; i < n; i++) {
			x[i] = model.intVar("X" + i, 1, n);
			d1[i] = model.intOffsetView(x[i], i);
			d2[i] = model.intOffsetView(x[i], -i);
		}
		model.allDifferent(x).post();
		model.allDifferent(d1).post();
		model.allDifferent(d2).post();
		model.getSolver().solve();
		for(int i = 0; i < n; i++) {
			System.out.println("x["+ i + "] = " + x[i].getValue());
		}
	}
}
