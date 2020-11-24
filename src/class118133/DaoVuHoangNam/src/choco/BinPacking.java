package ortools;

import java.util.Scanner;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class BinPacking {
	
	//private static int n; //So luong hang
	private static int l; // Chieu dai container
	private static int w; // Chieu rong container
	private static int[] ll = new int[20];
	private static int[] ww = new int[20];
	private static IntVar[] turn = new IntVar[20];
	private static IntVar[] x = new IntVar[20];
	private static IntVar[] y = new IntVar[20];
	//private static IntVar[][] con = new IntVar[20][20];
	
	public static void main(String[] args) {
		//MPSolver solver = new MPSolver("BinPacking",MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
		Model model = new Model("BinPacking");
		//Doc du lieu dau vao 
		Scanner scanner = new Scanner(System.in);
		//n = scanner.nextInt();
		l = scanner.nextInt();
		w = scanner.nextInt();
		int n = 0;
		ll[n] = scanner.nextInt();
		while (ll[n]!=-1) {
			ww[n] = scanner.nextInt();
			//System.out.println(ll[n]+" "+ww[n]);
			n++;
			ll[n] = scanner.nextInt();
		}
		
		//System.out.println(n);
		
		for (int i=0;i<n;i++) {
			x[i] = model.intVar("X[" +i+ "]",0,l-1);
			y[i] = model.intVar("Y[" +i+ "]",0,w-1);
			turn[i] = model.intVar("TURN["+i+"]",0,1);
		}
		
		for (int i=0;i<n;i++) {
			model.ifThen(model.arithm(turn[i],"=",0), model.arithm(model.intOffsetView(x[i], ll[i]), "<=", l));
			model.ifThen(model.arithm(turn[i],"=",0), model.arithm(model.intOffsetView(y[i], ww[i]), "<=", w));
			model.ifThen(model.arithm(turn[i],"=",1), model.arithm(model.intOffsetView(x[i], ww[i]), "<=", l));
			model.ifThen(model.arithm(turn[i],"=",1), model.arithm(model.intOffsetView(y[i], ll[i]), "<=", w));
			//x[i].add(turn[i].mul(ww[i] - ll[i]).add(ll[i])).le(w).post();
			for (int j=i+1;j<n;j++) {
				model.arithm(x[i], "!=", x[j]).post();
				model.arithm(y[i], "!=", y[j]).post();
			}
		}
		
		for (int i=0;i<n;i++) {
			for (int j=i+1;j<n;j++) {
				turn[i].eq(0).and(turn[j].eq(0)).imp(x[i].add(ll[i]).le(x[j]).
						or(y[i].add(ww[i]-1).le(y[j])).
						or(x[j].add(ll[j]-1).le(x[i])).
						or(y[j].add(ww[j]-1).le(y[i]))).post();
				turn[i].eq(0).and(turn[j].eq(1)).imp(x[i].add(ll[i]).le(x[j]).
						or(y[i].add(ww[i]-1).le(y[j])).
						or(x[j].add(ww[j]-1).le(x[i])).
						or(y[j].add(ll[j]-1).le(y[i]))).post();
				turn[i].eq(1).and(turn[j].eq(0)).imp(x[i].add(ww[i]).le(x[j]).
						or(y[i].add(ll[i]-1).le(y[j])).
						or(x[j].add(ll[j]-1).le(x[i])).
						or(y[j].add(ww[j]-1).le(y[i]))).post();
				turn[i].eq(1).and(turn[j].eq(1)).imp(x[i].add(ww[i]).le(x[j]).
						or(y[i].add(ll[i]-1).le(y[j])).
						or(x[j].add(ww[j]-1).le(x[i])).
						or(y[j].add(ll[j]-1).le(y[i]))).post();
			}
		}
		Boolean hasResult = model.getSolver().solve();
		if (hasResult) {
			for (int i=0;i<n;i++) {
				System.out.println("X["+i+"] = "+x[i]+", Y["+i+"] = "+y[i]+", Turn["+i+"] ="+turn[i]);
			}
		} else {
			System.out.println("-1");
		}
		
	}
}
