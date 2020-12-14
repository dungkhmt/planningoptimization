import java.util.Scanner;
import java.io.File;
import org.chocosolver.solver.*;
import org.chocosolver.solver.variables.IntVar;

public class BinPacking {
	int W;
	int L;	
	int N; // number package
	int w[] = new int[100];
	int l[] = new int[100];
	
	public void readInput() {
		String fileName = "C:\\Users\\CHUNG\\eclipse-workspace\\toiUu\\data\\bin-packing-2D-W10-H7-I6.txt";
		try {
			Scanner scanner = new Scanner(new File(fileName));
			W = scanner.nextInt();
			L = scanner.nextInt();
			int i = 0;
			while (scanner.hasNext()) {
				int x = scanner.nextInt();
				if (x < 0) break;
				int y = scanner.nextInt();
				if (y < 0) break;
				w[i]=x;
				l[i]=y;
				i++;
			}
			N = i;
		}catch (Exception e) {
			N = 0;
		}
		
		
	}
	public void solve() {
		Model model = new Model("BinPacking2D");	
		IntVar [] x = new IntVar[N];
		IntVar [] y = new IntVar[N];
		IntVar [] o = new IntVar[N];
		for (int i=0; i<N; i++) {
			x[i]= model.intVar("x"+i, 0, W);
			y[i]= model.intVar("y"+i, 0, L);
			o[i]= model.intVar("o"+i, 0, 1);
		}
		for (int i=0;i<N;i++) {
			model.ifThen(model.arithm(o[i], "=", 0), model.arithm(model.intOffsetView(x[i], w[i]), "<=", W));
			model.ifThen(model.arithm(o[i], "=", 0), model.arithm(model.intOffsetView(y[i], l[i]), "<=", L));
			model.ifThen(model.arithm(o[i], "=", 1), model.arithm(model.intOffsetView(x[i], l[i]), "<=", W));
			model.ifThen(model.arithm(o[i], "=", 1), model.arithm(model.intOffsetView(y[i], w[i]), "<=", L));
		}
		
		for (int i=0;i<N-1;i++) {
			for (int j=i+1;j<N;j++) {
				o[i].eq(0).and(o[j].eq(0)).imp((x[i].add(w[i]).le(x[j])).or((x[j].add(w[j]).le(x[i])), (y[i].add(l[i]).le(y[j])), (y[j].add(l[j]).le(y[i])))).post();
				o[i].eq(0).and(o[j].eq(1)).imp((x[i].add(w[i]).le(x[j])).or((x[j].add(l[j]).le(x[i])), (y[i].add(l[i]).le(y[j])), (y[j].add(w[j]).le(y[i])))).post();
				o[i].eq(1).and(o[j].eq(0)).imp((x[i].add(l[i]).le(x[j])).or((x[j].add(w[j]).le(x[i])), (y[i].add(w[i]).le(y[j])), (y[j].add(l[j]).le(y[i])))).post();
				o[i].eq(1).and(o[j].eq(1)).imp((x[i].add(l[i]).le(x[j])).or((x[j].add(l[j]).le(x[i])), (y[i].add(w[i]).le(y[j])), (y[j].add(w[j]).le(y[i])))).post();
			}
		}
		
		model.getSolver().solve();
		System.out.println("result:");
		for (int i=0;i<N;i++) {
			System.out.println(x[i]+ "  "+y[i]+"  "+ o[i]);
		}
		
	}
	
	public static void main(String [] args) {
		BinPacking app = new BinPacking();
		app.readInput();
		app.solve();
	}
}
