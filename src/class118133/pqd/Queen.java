package class118133.pqd;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Queen {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int N = 200;
		Model model = new Model("Queen");
		IntVar[] X = new IntVar[N];
		IntVar[] d1 = new IntVar[N];
		IntVar[] d2 = new IntVar[N];
		
		for(int i = 0; i < N; i++){
			X[i] = model.intVar("X[" + i + "]",0,N-1);			
			d1[i] = model.intOffsetView(X[i], i);
			d2[i] = model.intOffsetView(X[i], -i);
		}
		
		model.allDifferent(X).post();
		model.allDifferent(d1).post();
		model.allDifferent(d2).post();
		
		model.getSolver().findSolution();
		for(int i = 0; i < N; i++)
			System.out.println(X[i]);
		
	}

}
