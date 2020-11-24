package class118133.levuloi;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class BCA {
	class Pair{
		int first;
		int second;
		public Pair(int first, int second) {
			this.first = first;
			this.second = second;
		}
	}
	VarIntLS[] X;
	int lowerBound = 14;
	int upperBound = 15;
	ConstraintSystem S;
	int N = 13;
	int M = 3;
	ArrayList<Pair> Q = new ArrayList<>();
	int[] c = { 3, 3, 4, 3, 4, 3, 3, 3, 4, 3, 3, 4, 4 };
	LocalSearchManager mgr;
	
	{
		Q.add(new Pair(0, 2));
		Q.add(new Pair(0, 4));
		Q.add(new Pair(0, 8));
		Q.add(new Pair(1, 4));
		Q.add(new Pair(1, 10));
		Q.add(new Pair(3, 7));
		Q.add(new Pair(3, 9));
		Q.add(new Pair(5, 11));
		Q.add(new Pair(5, 12));
		Q.add(new Pair(6, 8));
		Q.add(new Pair(6, 12));
	}
	
	private void stateModel() {
		mgr = new LocalSearchManager();
		
		X = new VarIntLS[N];
		for (int i = 0; i < N; i++) {
			X[i] = new VarIntLS(mgr, 0, 2);
		}
		
		S = new ConstraintSystem(mgr);
		for (Pair c : Q) {
			S.post(new NotEqual(X[c.first], X[c.second]));
		}
		
		IFunction f;
		for (int i = 0; i < M; i++) {
			f = new ConditionalSum(X, c, i);
			S.post(new LessOrEqual(f, 15));
			S.post(new LessOrEqual(14, f));
		}
		
		S.post(new NotEqual(X[0], 2));
		S.post(new NotEqual(X[1], 0));
		S.post(new NotEqual(X[2], 1));
		S.post(new IsEqual(X[4], 0));
		S.post(new IsEqual(X[5], 1));
		S.post(new IsEqual(X[6], 1));
		S.post(new NotEqual(X[7], 0));
		S.post(new NotEqual(X[8], 2));
		S.post(new IsEqual(X[9], 2));
		S.post(new IsEqual(X[10], 0));
		S.post(new IsEqual(X[11], 2));
		S.post(new IsEqual(X[12], 2));
		
		mgr.close();
	}

	public void test() {
		LocalSearchManager mgr = new LocalSearchManager();
		
		for (int i = 0; i < N; i++) {
			X[i] = new VarIntLS(mgr, 0, 2);
		}
		System.out.println("Decision variable");
		for (int i = 0; i < N; i++) {
			System.out.print(X[i].getValue() + " ");
		}
		System.out.println();
		System.out.println("Amount");
		for (int i = 0; i < N; i++) {
			System.out.print(c[i] + " ");
		}
		System.out.println();
		IFunction f = new ConditionalSum(X, c, 0);
		mgr.close();
		System.out.println("Value = " + f.getValue());
	}
	
	class Move{
		int key;
		int value;
		public Move(int key, int value) {
			this.key = key;
			this.value = value;
		}
	}
	
	private void localSearch() {
		System.out.println("Init S.violations = " + S.violations());
		ArrayList<Move> cand = new ArrayList<>();
		Random r = new Random();
		for (int it = 1; it <= 1000000; it++) {
			cand.clear();
			for (int i = 0; i < N; i++) {
				int min_delta = 1000000;
				int delta;
				for (int v = X[i].getMinValue(); v <= X[i].getMaxValue(); v++) {
					delta = S.getAssignDelta(X[i], v);
					if (min_delta > delta) {
						min_delta = delta;
						cand.clear();
						cand.add(new Move(i, v));
					}
					else if (min_delta == delta) {
						cand.add(new Move(i, v));
					}
				}
			}
			
			Move m = cand.get(r.nextInt(cand.size()));
			X[m.key].setValuePropagate(m.value);
			System.out.println("Step " + it + ", S.violations = " + S.violations());
		}
	}
	
	public static void main(String[] args) {
		BCA app = new BCA();
		app.stateModel();
		app.localSearch();
	}
}
