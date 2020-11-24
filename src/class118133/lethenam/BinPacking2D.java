package class118133.lethenam;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.chocosolver.solver.Model;

import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;



public class BinPacking2D {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Bin Packing 2D");
		
		int N = 10000, count = 0, flag = 1;
		int[] box = new int[2];
		int[][] size = new int[N][2];
		try {
			Scanner in = new Scanner(new File("./data/BinPacking2D/bin-packing-2D-W10-H7-I6.txt"));
			box[0] = in.nextInt();
			box[1] = in.nextInt();
//			System.out.println(box[0] + " " + box[1]);
			while(true){
				for (int i = 0; i < 2; i++) {
					size[count][i] = in.nextInt();
					if (size[count][i] == -1) {
						flag = 0;
						break;
					}
				}
				if (flag == 0) {
					break;
				}
				count++;
			}

		}catch (Exception e){
            e.printStackTrace();
        }
		

//		for (int i = 0; i < count; i++) {
//			System.out.println(size[i][0] + " " + size[i][1]);
//		}
		
		int W = box[0];
		int L = box[1];
		
		Model model = new Model("Bin Packing 2D");
		IntVar[] x = new IntVar[count];
		IntVar[] y = new IntVar[count];
		IntVar[] o = new IntVar[count];
		int[] w = new int[count];
		int[] l = new int[count];
		
		for (int i = 0; i < count; i++) {
			w[i] = size[i][0];
			l[i] = size[i][1];
			
		}
		for (int i = 0; i < count; i++) {
			x[i] = model.intVar("x[" + i + "]", 0, W);
			y[i] = model.intVar("y[" + i + "]", 0, L);
			o[i] = model.intVar("o[" + i + "]", 0, 1);
		}

		
		for (int i = 0; i < count; i++) {
			model.ifThen(model.arithm(o[i], "=", 0), model.arithm(model.intOffsetView(x[i], w[i]), "<=", W));
			model.ifThen(model.arithm(o[i], "=", 0), model.arithm(model.intOffsetView(y[i], l[i]), "<=", L));
			
			model.ifThen(model.arithm(o[i], "=", 1), model.arithm(model.intOffsetView(x[i], l[i]), "<=", W));
			model.ifThen(model.arithm(o[i], "=", 1), model.arithm(model.intOffsetView(y[i], w[i]), "<=", L));
			
		}
		
		for(int i = 0; i < count; i++) {
			for(int j = 0; j < count; j++) {
				if (i != j) {
					
					BoolVar k1 = model.boolVar();
					k1.eq(o[i].eq(0).and(o[j].eq(0))).post();
					k1.imp((x[i].add(w[i]).le(x[j])).or(y[i].add(l[i]).le(y[j])).or(x[j].add(w[j]).le(x[i])).or(y[j].add(l[j]).le(y[i]))).post();
					
					BoolVar k2 = model.boolVar();
					k2.eq(o[i].eq(0).and(o[j].eq(1))).post();
					k2.imp((x[i].add(w[i]).le(x[j])).or(y[i].add(l[i]).le(y[j])).or(x[j].add(l[j]).le(x[i])).or(y[j].add(w[j]).le(y[i]))).post();
					
					BoolVar k3 = model.boolVar();
					k3.eq(o[i].eq(1).and(o[j].eq(0))).post();
					k3.imp((x[i].add(l[i]).le(x[j])).or(y[i].add(w[i]).le(y[j])).or(x[j].add(w[j]).le(x[i])).or(y[j].add(l[j]).le(y[i]))).post();
					
					BoolVar k4 = model.boolVar();
					k4.eq(o[i].eq(1).and(o[j].eq(1))).post();
					k4.imp((x[i].add(l[i]).le(x[j])).or(x[i].add(w[i]).le(y[j])).or(x[j].add(l[j]).le(x[i])).or(y[j].add(w[j]).le(y[i]))).post();
				}
			}
		}

		model.getSolver().solve();
		for(int i = 0; i < count; i++) {
			System.out.println(x[i].getValue() + ", " + y[i].getValue() + ", " + o[i].getValue());
		}
		System.out.println("end");
	}

}
