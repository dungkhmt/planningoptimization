package class118133.lethenam;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class chocoEx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model model = new Model("Example choco");
		IntVar[] X= new IntVar[5];
		for (int i = 0; i < 5; i++) {
			X[i] = model.intVar("X[" + i + "]", 1, 5);
		}
		model.arithm(model.intOffsetView(X[2], 3), "!=", X[1]).post();
		model.arithm(X[3], "<=", X[4]).post();;
		model.arithm(X[2], "+", X[3], "=", model.intOffsetView(X[0], 1)).post();
		model.arithm(X[4], "<=", 3).post();
		model.arithm(X[1], "+", X[4], "=", 7).post();
		model.ifThen(model.arithm(X[2], "=", 1), model.arithm(X[4], "!=", 2));
		
		model.getSolver().solve();
		for (int i = 0; i < 5; i++) {
			System.out.println(X[i]);
		}
	}
}
