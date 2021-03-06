package class118133.levuloi;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class CBLS {
	LocalSearchManager mgr;
	VarIntLS[] X;
	ConstraintSystem S;
	
	private void stateModel() {
		mgr = new LocalSearchManager();
		
		X = new VarIntLS[5];
		for (int i = 0; i < 5; i++) {
			X[i] = new VarIntLS(mgr, 1, 5);
			
		}
		S = new ConstraintSystem(mgr);
		
//		IFunction f1 = new FuncPlus(X[2], 3);
//		IConstraint c1 = new NotEqual(f1, X[1]);
//		S.post(c1);
		
		S.post(new NotEqual(new FuncPlus(X[2], 3), X[1]));
		S.post(new LessOrEqual(X[3], X[4]));
		S.post(new IsEqual(new FuncPlus(X[2], X[3]), new FuncPlus(X[0], 1)));
		S.post(new LessOrEqual(X[4], 3));
		S.post(new IsEqual(new FuncPlus(X[1], X[4]), 7));
		S.post(new Implicate(new IsEqual(X[2], 1), new NotEqual(X[4], 2)));
		mgr.close();
	}
	
	private void localSearch() {
		/*for (int i = 0; i < X.length; i++) {
			System.out.println("X[" + i + "] = " + X[i].getValue());
		}
		System.out.println("S.violations = " + S.violations());
		int delta = S.getAssignDelta(X[3], 4);
		System.out.println("delta = " + delta);
		
		X[3].setValuePropagate(4);
		System.out.println("new violations = " + S.violations());*/
		
		System.out.println("Init S.violations = " + S.violations());
		ArrayList<Move> cand = new ArrayList<Move>();
		Random r = new Random();
		for (int it = 1; it <= 100; it++) {
			cand.clear();
			int min_delta = 10000000;
//			VarIntLS sel_x = null;
//			int sel_v = -1;
			
			// query neighborhood
			for (int i = 0; i < X.length; i++) {
				for (int v = X[i].getMinValue(); v <= X[i].getMaxValue(); v++) {
					int delta = S.getAssignDelta(X[i], v);
					if (delta < min_delta) {
						min_delta = delta;
//						sel_x = X[i];
//						sel_v = v;
						cand.clear();
						cand.add(new Move(i, v));
					}
					else if (delta == min_delta) {
						cand.add(new Move(i, v));
					}
				}
			}
			
			// local move 
//			sel_x.setValuePropagate(sel_v);
			Move m = cand.get(r.nextInt(cand.size()));
			X[m.i].setValuePropagate(m.value);
			System.out.println("Step " + it + ", S.violations = " + S.violations());
		}
	}
	
	class Move{
		int i;
		int value;
		public Move(int i, int value) {
			this.i = i;
			this.value = value;
		}
	}
	
	public static void main(String[] args) {
		CBLS app = new CBLS();
		app.stateModel();
		app.localSearch();
	}
}
