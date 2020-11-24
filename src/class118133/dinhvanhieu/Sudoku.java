package dinhvanhieu;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.constraints.basic.Implicate;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class Sudoku {
	LocalSearchManager mgr;
	VarIntLS[][] X;
	ConstraintSystem S;
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		X = new VarIntLS[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				X[i][j] = new VarIntLS(mgr, 1, 9);
				X[i][j].setValue(j + 1);
			}
		}
		S = new ConstraintSystem(mgr);
		for (int i = 0; i < 9; i++) {
			VarIntLS[] y = new VarIntLS[9];
			for (int j = 0; j < 9; j++) {
				y[j] = X[i][j];
			}
			S.post(new AllDifferent(y));
		}
		for (int i = 0; i < 9; i++) {
			VarIntLS[] y = new VarIntLS[9];
			for (int j = 0; j < 9; j++) {
				y[j] = X[j][i];
			}
			S.post(new AllDifferent(y));
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				VarIntLS[] y = new VarIntLS[9];
				int idx = -1;
				for (int ii = 0; ii < 3; ii++) {
					for (int jj = 0; jj < 3; jj++) {
						idx ++;
						y[idx] = X[3*i+ii][3*j+jj];
					}
				}
				S.post(new AllDifferent(y));
			}
		}
		mgr.close();
	}
	
	public void search() {
		GenericHillClimbingSearch searcher = new GenericHillClimbingSearch();
		searcher.search(S, 100000, 10000);
	}
	
	public static void main(String[] args) {
		Sudoku app = new Sudoku();
		app.stateModel();
		app.search();
	}
}