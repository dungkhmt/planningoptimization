package class118133.dodinhdac;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;


public class NQueen {
	public static void main(String []args) {
		int n = 8;
		Model model = new Model(n + "-queens problem");
		IntVar[] vars = new IntVar[n];
		IntVar[] plusVars = new IntVar[n];
		IntVar[] subVars = new IntVar[n];
		for(int i = 0; i < n; i++){
		    vars[i] = model.intVar("Q_"+i, 1, n);
		    plusVars[i] = vars[i].add(i).intVar();
		    subVars[i] = vars[i].add(i).intVar();
		}

		
		model.allDifferent(vars).post();
		model.allDifferent(plusVars).post();
		model.allDifferent(subVars).post();
			
		Solution solution = model.getSolver().findSolution();
		if(solution != null){
		    System.out.println(solution.toString());
		}
	}
}
