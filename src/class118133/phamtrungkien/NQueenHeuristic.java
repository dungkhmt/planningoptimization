package phamtrungkien;

import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class NQueenHeuristic {
	public static final int N = 8;
	public static void main(String[] args) {
		
		LocalSearchManager mgr = new LocalSearchManager();
		VarIntLS[] x = new VarIntLS[N];
		for(int i = 0; i < N; i++)
			x[i] = new VarIntLS(mgr,0,N-1);
		
		ConstraintSystem S = new ConstraintSystem(mgr);
		for (int i=0;i < N;i++)
		for (int j=i+1;j < N;j++) {
			S.post(new NotEqual(x[i], x[j]));
			S.post(new NotEqual(new FuncPlus(x[i], i), new FuncPlus(x[j], j)));
			S.post(new NotEqual(new FuncPlus(x[i], j), new FuncPlus(x[j], i)));
		}
		mgr.close();
		
		MinMaxSelector mms = new MinMaxSelector(S);
		for(int it = 0; it <= 10000 && S.violations() > 0; it++){
			VarIntLS y = mms.selectMostViolatingVariable();
			int v = mms.selectMostPromissingValue(y);
			y.setValuePropagate(v);
		}
		
		int[] q = new int[N];
		for(int i = 0; i < x.length; i++)
			q[i] = x[i].getValue();
		print(q);
	}
	
	public static void print(int[] q) {
		
		char[][] a = new char[N][N];
		
		for (int i=0;i < N;i++)
		for (int j=0;j < N;j++)
			a[i][j] = '.';
		
		for (int i=0;i < N;i++)
			a[q[i]][i] = 'x';
		
		for (int i=0;i < N;i++) {
		for (int j=0;j < N;j++)
			System.out.print(a[i][j] + " ");
		System.out.println();
		}
	}

}
