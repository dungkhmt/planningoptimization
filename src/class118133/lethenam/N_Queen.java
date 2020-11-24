package class118133.lethenam;

import java.util.Scanner;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class N_Queen {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Model model = new Model("N_Queen");
		IntVar[] x = new IntVar[8];
		IntVar[] d1 = new IntVar[8];
		IntVar[] d2 = new IntVar[8];
		for (int i = 0; i < 8; i++) {
			x[i] = model.intVar("X[" + i + "]", 1, 8);
			d1[i] = model.intOffsetView(x[i], i);
			d2[i] = model.intOffsetView(x[i], -i);
		}
		model.allDifferent(x).post();;
		model.allDifferent(d1).post();;
		model.allDifferent(d2).post();;
		
		model.getSolver().solve();
		
		for(int i = 0; i < 8; i++) {
			System.out.println("X[" + i + "] = " + x[i].getValue());
		}
	}

}
