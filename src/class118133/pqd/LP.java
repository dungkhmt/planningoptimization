package class118133.pqd;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class LP {
	static{
		System.loadLibrary("jniortools");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MPSolver solver = new MPSolver("LinearProgrammingExample", MPSolver.OptimizationProblemType.GLOP_LINEAR_PROGRAMMING);
		double infinity = java.lang.Double.POSITIVE_INFINITY;
		MPVariable x1 = solver.makeNumVar(0.0, infinity, "x1");
		MPVariable x2 = solver.makeNumVar(0.0, infinity, "x2");
		MPVariable x3 = solver.makeNumVar(0.0, infinity, "x3");
		MPVariable x4 = solver.makeNumVar(0.0, infinity, "x4");
		MPVariable x5 = solver.makeNumVar(0.0, infinity, "x5");
		MPVariable x6 = solver.makeNumVar(0.0, infinity, "x6");
		MPVariable x7 = solver.makeNumVar(0.0, infinity, "x7");
		
		MPConstraint c0 = solver.makeConstraint(-infinity,9.0,"c0");
		c0.setCoefficient(x1, 1);
		c0.setCoefficient(x4, 1);
		c0.setCoefficient(x6, 6);
		MPConstraint c1 = solver.makeConstraint(9.0,infinity,"c1");
		c1.setCoefficient(x1, 1);
		c1.setCoefficient(x4, 1);
		c1.setCoefficient(x6, 6);
		
		MPConstraint c2 = solver.makeConstraint(-infinity,2.0,"c2");
		c2.setCoefficient(x1, 3);
		c2.setCoefficient(x2, 1);
		c2.setCoefficient(x3, -4);
		c2.setCoefficient(x6, 2);
		c2.setCoefficient(x7, 1);
		MPConstraint c3 = solver.makeConstraint(2.0,infinity,"c3");
		c3.setCoefficient(x1, 3);
		c3.setCoefficient(x2, 1);
		c3.setCoefficient(x3, -4);
		c3.setCoefficient(x6, 2);
		c3.setCoefficient(x7, 1);
		
		MPConstraint c4 = solver.makeConstraint(-infinity,6.0,"c4");
		c4.setCoefficient(x1, 1);
		c4.setCoefficient(x2, 2);
		c4.setCoefficient(x5, 1);
		c4.setCoefficient(x6, 2);
		MPConstraint c5 = solver.makeConstraint(6.0,infinity,"c5");
		c5.setCoefficient(x1, 1);
		c5.setCoefficient(x2, 2);
		c5.setCoefficient(x5, 1);
		c5.setCoefficient(x6, 2);
		
		MPObjective obj = solver.objective();
		obj.setCoefficient(x1, 1);
		obj.setCoefficient(x2, -6);
		obj.setCoefficient(x3, 32);
		obj.setCoefficient(x4, 1);
		obj.setCoefficient(x5, 1);
		obj.setCoefficient(x6, 10);
		obj.setCoefficient(x7, 100);
		obj.setMinimization();
		
		final MPSolver.ResultStatus resultStatus = solver.solve();
	    // Check that the problem has an optimal solution.
	    if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
	      System.err.println("The problem does not have an optimal solution!");
	      return;
	    }
	    // [END solve]

	    // [START print_solution]
	    // The value of each variable in the solution.
	    System.out.println("Solution");
	    System.out.println("x1 = " + x1.solutionValue());
	    System.out.println("x2 = " + x2.solutionValue());
	    System.out.println("x3 = " + x3.solutionValue());
	    System.out.println("x4 = " + x4.solutionValue());
	    System.out.println("x5 = " + x5.solutionValue());
	    System.out.println("x6 = " + x6.solutionValue());
	    System.out.println("x7 = " + x7.solutionValue());

	    
	    // The objective value of the solution.
	    System.out.println("Optimal objective value = " + solver.objective().value());
	    // [END print_solution]

	}

}
