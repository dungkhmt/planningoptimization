package choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Sudoku {
	public static void main(String[] args) {
		Model model = new Model("Sudoku");
		IntVar[][] x = new IntVar[9][9];
		for (int i = 0; i < 9; i++)
		  for (int j = 0; j < 9; j++)
		    x[i][j] = model.intVar("x[" + i + "," + j + "]", 1, 9);

		for (int i = 0; i < 9; i++) {
		  for (int j1 = 0; j1 < 9; j1++){
		    for (int j2 = j1 + 1; j2 < 9; j2++) {
		      model.arithm(x[i][j1], "!=", x[i][j2]).post();
		      model.arithm(x[j1][i], "!=", x[j2][i]).post();
		    }
		  }
		}
		
		for (int I = 0; I < 3; I++) 
			  for (int J = 0; J < 3; J++) 
			    for (int i1 = 0; i1 < 3; i1++) 
			      for (int j1 = 0; j1 < 3; j1++) 
			        for (int i2 = 0; i2 < 3; i2++) 
			          for (int j2 = 0; j2 < 3; j2++) 
			            if (i1 < i2 || i1 == i2 && j1 < j2) 
			              model.arithm(x[3 * I + i1][3 * J + j1],
			                 "!=", x[3 * I + i2][3 * J + j2]).post();
		model.getSolver().solve();
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++) 
			    System.out.print(x[i][j].getValue() + " ");
			System.out.println();
		}
	}
}
