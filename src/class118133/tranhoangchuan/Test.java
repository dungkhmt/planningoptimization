package class118133.tranhoangchuan;

import com.google.ortools.constraintsolver.IntVarElement;
import com.google.ortools.constraintsolver.Solver;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import com.google.ortools.sat.*;
public class Test {
	static {
	    System.loadLibrary("jniortools");
	}
	int N;
	int[][] c = {	
			{0,6,1,1},	
			{6,0,1,1},
			{1,1,0,5},
			{1,1,5,0}
	};
	CpModel model;
	IntVar[] X;
	public void solve(String args[]) {
		model = new CpModel();
		for (int i = 0; i < N; ++i) {
			X[i] = model.newIntVar(0, N-1,"X["+ i + "]");
		}
	}
}