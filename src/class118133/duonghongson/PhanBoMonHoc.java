package class118133.duonghongson;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class PhanBoMonHoc {
	public static void main(String[] args) {
		int N = 9;
		int P = 4;
		int[] credits= {3, 2, 2, 1, 3, 3, 1, 2, 2};
		int alpha = 2;
		int beta = 4;
		int lamda = 3;
		int gamma = 7;
		int[] I = {0, 0, 1, 2, 3, 4, 3};
		int[] J = {1, 2, 3, 5, 6, 7, 8};
		
		int[] oneN = {1, 1, 1, 1, 1, 1, 1, 1, 1};
		int[] oneP = {1, 1, 1, 1};
		
		Model model = new Model("BACP");
		IntVar[][] x = new IntVar[P][N]; // x[j][i]=1 indicates that course i is assigned to semester j
		for(int j = 0; j<P; j++)
			for (int i = 0; i < N; i++)
				x[j][i] = model.intVar("x[" + j + "," + i + "]", 0, 1);
		for (int j = 0; j < P; j++) {
			model.scalar(x[j], credits, ">=", lamda).post();
			model.scalar(x[j], credits, "<=", gamma).post();
			
			model.scalar(x[j], oneN, ">=", alpha).post();
			model.scalar(x[j], oneN, "<=", beta).post();
		}
		
		for(int i = 0; i<N; i++) {
			IntVar[] y = new IntVar[P];
			for (int j = 0; j<P; j++) y[j] = x[j][i];
			model.scalar(y, oneP, "=", 1).post(); //each course is assigned to exactly one semester
		}
		for (int k = 0; k<I.length; k++) {
			int i = I[k]; int j = J[k];
			for(int q = 0; q<P; q++)
				for (int p = 0; p<=q; p++)
					model.ifThen(model.arithm(x[q][i], "=", 1),
							model.arithm(x[p][j], "=", 0));
		}
		model.getSolver().solve();
		
		for (int j = 0; j<P; j++) {
			System.out.print("semester " + j + ": ");
			for (int i = 0; i<N; i++) if(x[j][i].getValue() == 1) {
				System.out.print("[course " + i + ", credit "+ credits[i] + "] ");
			System.out.println();
			}
		}
	}
}
