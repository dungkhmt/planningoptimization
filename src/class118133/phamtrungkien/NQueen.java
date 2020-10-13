package phamtrungkien;

import org.chocosolver.solver.*;
import org.chocosolver.solver.variables.IntVar;

public class NQueen {

	public static void main(String[] args) {
		Model model = new Model("NQueen");
		IntVar[] vs = model.intVarArray("vs", 8, 1, 8);
		Solver solver = model.getSolver();
		
		model.allDifferent(vs).post();
		
		for (int i=0;i < 8;i++)
			for (int j=i+1;j < 8;j++)
				model.arithm(vs[i], "-", vs[j],"!=",j-i).post();
		
		for (int i=0;i < 8;i++)
			for (int j=i+1;j < 8;j++)
				model.arithm(vs[i], "-", vs[j],"!=",i-j).post();
		
		if (solver.solve()) {
			for (int i=0;i < 8;i++)
				System.out.print(vs[i] + " ");
		}
	}

}