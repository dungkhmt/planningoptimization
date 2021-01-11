package phamtrungkien;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import localsearch.constraints.basic.AND;
import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.OR;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;

public class TwoDBinPacking {
	public static int W = 4;
	public static int H = 6;
	public static int N = 3;
	public static int w[];
	public static int h[];
	public static LocalSearchManager mgr;
	public static VarIntLS[] x;
	public static VarIntLS[] y;
	public static VarIntLS[] o;
	public static ConstraintSystem S;
	
	public static void model() {
		mgr = new LocalSearchManager();
		x = new VarIntLS[N];
		y = new VarIntLS[N];
		o = new VarIntLS[N];
		for (int i=0;i < N;i++) {
			x[i] = new VarIntLS(mgr,0,W);
			y[i] = new VarIntLS(mgr,0,H);
			o[i] = new VarIntLS(mgr,0,1);
		}
		
		S = new ConstraintSystem(mgr);
		
		for (int i=0;i < N;i++)
			for (int j=i+1;j < N;j++) {
				//-------------------------------------------------------
				IConstraint[] c0 = new IConstraint[2];
				c0[0] = new IsEqual(o[i], 0);
				c0[1] = new IsEqual(o[j], 0);
				IConstraint[] c1 = new IConstraint[4];
				c1[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
				c1[1] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
				c1[2] = new LessOrEqual(new FuncPlus(y[i], h[i]), y[j]);
				c1[3] = new LessOrEqual(new FuncPlus(y[j], h[j]), y[i]);
				S.post(new Implicate(new AND(c0), new OR(c1)));
				
				//-------------------------------------------------------
				c0 = new IConstraint[2];
				c0[0] = new IsEqual(o[i], 0);
				c0[1] = new IsEqual(o[j], 1);
				c1 = new IConstraint[4];
				c1[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
				c1[1] = new LessOrEqual(new FuncPlus(x[j], h[j]), x[i]);
				c1[2] = new LessOrEqual(new FuncPlus(y[i], h[i]), y[j]);
				c1[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), y[i]);
				S.post(new Implicate(new AND(c0), new OR(c1)));
				
				//-------------------------------------------------------
				c0 = new IConstraint[2];
				c0[0] = new IsEqual(o[i], 1);
				c0[1] = new IsEqual(o[j], 0);
				c1 = new IConstraint[4];
				c1[0] = new LessOrEqual(new FuncPlus(x[i], h[i]), x[j]);
				c1[1] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
				c1[2] = new LessOrEqual(new FuncPlus(y[i], w[i]), y[j]);
				c1[3] = new LessOrEqual(new FuncPlus(y[j], h[j]), y[i]);
				S.post(new Implicate(new AND(c0), new OR(c1)));
				
				//-------------------------------------------------------
				c0 = new IConstraint[2];
				c0[0] = new IsEqual(o[i], 1);
				c0[1] = new IsEqual(o[j], 1);
				c1 = new IConstraint[4];
				c1[0] = new LessOrEqual(new FuncPlus(x[i], h[i]), x[j]);
				c1[1] = new LessOrEqual(new FuncPlus(x[j], h[j]), x[i]);
				c1[2] = new LessOrEqual(new FuncPlus(y[i], w[i]), y[j]);
				c1[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), y[i]);
				S.post(new Implicate(new AND(c0), new OR(c1)));
			}
		
		for (int i=0;i < N;i++) {
			IConstraint[] c0 = new IConstraint[2];
			c0[0] = new IsEqual(o[i], 0);
			c0[1] = new IsEqual(o[i], 1);
			
			IConstraint[] c1 = new IConstraint[2];
			c1[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), W);
			c1[1] = new LessOrEqual(new FuncPlus(y[i], h[i]), H);
			
			IConstraint[] c2 = new IConstraint[2];
			c2[0] = new LessOrEqual(new FuncPlus(x[i], h[i]), W);
			c2[1] = new LessOrEqual(new FuncPlus(y[i], w[i]), H);
			
			S.post(new Implicate(c0[0], new AND(c1)));
			S.post(new Implicate(c0[1], new AND(c2)));
		}
		mgr.close();
	}
	public static void search() {
		HillClimbing searcher = new HillClimbing();
		int iter = 100000;
		searcher.hillClimbing(S, iter);
	}
	public static void input() throws FileNotFoundException {
		File fi = new File("test1.txt");
		Scanner sc = new Scanner(fi);
		W = sc.nextInt();
		H = sc.nextInt();
		N = 0;
		ArrayList<Integer> tmp_w = new ArrayList<Integer>();
		ArrayList<Integer> tmp_h = new ArrayList<Integer>();
		while (true) {
			int w = sc.nextInt();
			if (w == -1) break;
			int h = sc.nextInt();
			tmp_w.add(w);
			tmp_h.add(h);
		}
		N = tmp_w.size();
		w = new int[N];
		h = new int[N];
		for (int i=0;i < N;i++) {
			w[i] = tmp_w.get(i);
			h[i] = tmp_h.get(i);
			//System.out.println(w[i] + " " + h[i]);
		}
		
	}
	public static void output() throws IOException {
		FileWriter fw = new FileWriter("out1.txt");
		for (int i=0;i < N;i++)
			fw.write(x[i].getValue() + " " + y[i].getValue()+"\n");
		fw.close();
	}
	public static void main(String[] args) throws IOException {
		input();
		model();
		search();
		output();
	}

}
