package class118133.lethenam;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class MIPEX2 {
//	{
//        System.loadLibrary("jniortools");
//    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary("jniortools");
		System.out.println("Example 2 MIP");
		double INF = java.lang.Double.POSITIVE_INFINITY;
		MPSolver solver = new MPSolver("SimpleMIP", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
		MPVariable x1 = solver.makeNumVar(0, 14, "X1");
		MPVariable x2 = solver.makeNumVar(0, 20, "X2");
		
		MPConstraint c1 = solver.makeConstraint(-INF, 0);
		c1.setCoefficient(x1, 1);
		c1.setCoefficient(x2, -5);
		
		MPConstraint c2 = solver.makeConstraint(0, 20);
		c2.setCoefficient(x1, -2);
		c2.setCoefficient(x2, 10);
		
		MPObjective obj = solver.objective();
		obj.setCoefficient(x1, 2);
		obj.setCoefficient(x2, 1);
		obj.setMaximization();
		
		MPSolver.ResultStatus rs = solver.solve();
		if (rs != MPSolver.ResultStatus.OPTIMAL) {
			System.out.println("Cannot find optimal solution");
		}
		else {
			System.out.println("Objective value = " + obj.value());
			System.out.println("x1 = " + x1.solutionValue());
			System.out.println("x2 = " + x2.solutionValue());
		}
	}
}

