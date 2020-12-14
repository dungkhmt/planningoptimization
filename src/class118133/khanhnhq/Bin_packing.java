package khanhnhq;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import Problem.CHOCO.Bin_packing_2d;

import java.util.Scanner;

public class Bin_packing {
	int W, L;
	int N = 1000000;
	Integer[] w = new Integer[N];
	Integer[] l = new Integer[N];
	
	
	
	public void input() {
		Scanner scanner = new Scanner( System.in );
		int cnt = 0;
		while(true) {
			cnt++;
			int number = scanner.nextInt(); 
			if(number == -1) break;
			if(cnt == 1) W = number;
			if(cnt == 2) L = number;
			if(cnt > 2) {
				if(cnt % 2 == 1) {
					w[(cnt - 3) / 2] = number;
				}
				else {
					l[(cnt - 3) / 2] = number;
				}
			}
			
		}
        scanner.close();
		N = (cnt - 3) / 2;
	}
	
	public void solve() {
		Model model = new Model("BinPacking");
        IntVar[] X = model.intVarArray("X" , N, 0, W);
        IntVar[] Y = model.intVarArray("Y", N, 0, L);
        BoolVar[] o = model.boolVarArray("O",N);

        for (int i = 0 ; i < N ; i++){
            o[i].eq(0).imp(X[i].add(w[i]).le(W).and(Y[i].add(l[i]).le(L))).post();
            o[i].eq(1).imp(X[i].add(l[i]).le(W).and(Y[i].add(w[i]).le(L))).post();
        }
        
        for (int i =0; i < N-1 ; i++){
            for(int j=i+1; j<N ; j++){
                (o[i].eq(0).and(o[j].eq(0))).imp(X[i].add(w[i]).le(X[j]).or(X[j].add(w[j]).le(X[i])).or(Y[i].add(l[i]).le(Y[j]).or(Y[j].add(l[j]).le(Y[i])))).post();
                (o[i].eq(0).and(o[j].eq(1))).imp(X[i].add(w[i]).le(X[j]).or(X[j].add(l[j]).le(X[i])).or(Y[i].add(l[i]).le(Y[j]).or(Y[j].add(w[j]).le(Y[i])))).post();
                (o[i].eq(1).and(o[j].eq(0))).imp(X[i].add(l[i]).le(X[j]).or(X[j].add(w[j]).le(X[i])).or(Y[i].add(w[i]).le(Y[j]).or(Y[j].add(l[j]).le(Y[i])))).post();
                (o[i].eq(1).and(o[j].eq(1))).imp(X[i].add(l[i]).le(X[j]).or(X[j].add(l[j]).le(X[i])).or(Y[i].add(w[i]).le(Y[j]).or(Y[j].add(w[j]).le(Y[i])))).post();
            }
        }
        Solution solution = model.getSolver().findSolution();
        if(solution != null){
        	for (int i = 0; i < N; ++i) {
        		System.out.println("Toa do hinh chu nhat thu  " + (i + 1));
                System.out.println("X[" + i + "] = " + X[i].getValue());
                System.out.println("Y[" + i + "] = " + Y[i].getValue());
                System.out.println("o[" + i + "] = " + o[i].getValue());
            }
        }
        else {
        	System.out.println("No solution");
        }

	}
	public static void main(String[] args) {
		Bin_packing app = new Bin_packing();
		app.input();
//		System.out.print(app.N);
        app.solve();
        
    }
}