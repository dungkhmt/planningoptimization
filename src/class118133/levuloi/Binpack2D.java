package class118133.levuloi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

public class Binpack2D {
	private int W;
	private int L;
	private int[] w;
	private int[] l;
	
	private Model model;
	private IntVar[] x;
	private IntVar[] y;
	private IntVar[] o;
	
	private void readFile() {
		String content = null;
		try {
			byte[] bytes = Files.readAllBytes(Paths.get("data/BinPacking2D/bin-packing-2D-W19-H24-I21.txt"));
			content = new String(bytes, StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		String[] lines = content.split("\n");
		String[] firstLine = lines[0].split(" ");
		W = Integer.parseInt(firstLine[0]);
		L = Integer.parseInt(firstLine[1]);
		
		int length = lines.length - 2;
		w = new int[length];
		l = new int[length];
		
		for (int i = 0; i < length; i++) {
			String[] line = lines[i + 1].split(" ");
			w[i] = Integer.parseInt(line[0]);
			l[i] = Integer.parseInt(line[1]);
		}
	}
	
	private void solver() {
		int length = w.length; 
		
		x = new IntVar[length];
		y = new IntVar[length];
		o = new IntVar[length];
		
		model = new Model("Binpack2D");
		for (int i = 0; i < length; i++) {
			x[i] = model.intVar("x[" + i + "]", 0, W - 1);
			y[i] = model.intVar("y[" + i + "]", 0, L - 1);
			o[i] = model.intVar("o[" + i + "]", 0, 1);
		}
		
		for (int i = 0; i < length; i++) {
			model.sum(
				new IntVar[] {
					x[i], model.intScaleView(o[i], w[i]), model.intScaleView(model.intOffsetView(model.intMinusView(o[i]), 1), l[i])},
				"<=", W
			).post();
			
			model.sum(
				new IntVar[] {
					y[i], model.intScaleView(o[i], l[i]), model.intScaleView(model.intOffsetView(model.intMinusView(o[i]), 1), w[i])}, 
				"<=", L
			).post();
		}
		
		for (int i = 0; i < length - 1; i++) {
			for (int j = i + 1; j < length; j++) {
			
				Constraint one = model.sum(
					new IntVar[] {
						x[i], 
						model.intScaleView(o[i], w[i]), 
						model.intScaleView(model.intOffsetView(model.intMinusView(o[i]), 1), l[i]), 
						model.intMinusView(x[j])
					}, "<=", 0
				);
				
				Constraint two = model.sum(
					new IntVar[] {
						x[j], 
						model.intScaleView(o[j], w[j]), 
						model.intScaleView(model.intOffsetView(model.intMinusView(o[j]), 1), l[j]), 
						model.intMinusView(x[i])
					}, "<=", 0
				);
				
				
				Constraint three = model.sum(
					new IntVar[] {
						y[i], 
						model.intScaleView(o[i], l[i]), 
						model.intScaleView(model.intOffsetView(model.intMinusView(o[i]), 1), w[i]), 
						model.intMinusView(y[j])
					}, "<=", 0
				);
				
				Constraint four = model.sum(
					new IntVar[] {
						y[j], 
						model.intScaleView(o[j], l[j]), 
						model.intScaleView(model.intOffsetView(model.intMinusView(o[j]), 1), w[j]), 
						model.intMinusView(y[i])
					}, "<=", 0
				);

				model.or(one, two, three, four).post();
			} 
		}
		
		model.getSolver().solve();
	}
	
	private void printResult() {
		for (int i = 0; i < w.length; i++) {
			System.out.printf("%d. x = %d; y = %d; o = %d\n", i + 1, x[i].getValue(), y[i].getValue(), o[i].getValue());
		}
	}
	
	public static void main(String[] args) {	
		Binpack2D app = new Binpack2D();
		app.readFile();
		app.solver();
		app.printResult();
	}
}
