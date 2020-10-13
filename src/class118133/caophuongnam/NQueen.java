package class118133.caophuongnam;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.util.Scanner;

public class NQueen {
    public static void main(String[] args) {
        Model model = new Model("Queen");
        Scanner scanner = new Scanner(System.in);
        int n;
        System.out.println("Nhap vao so n:");
        n = scanner.nextInt();
        IntVar[] x = new IntVar[n];
        IntVar[] d1 = new IntVar[n];
        IntVar[] d2 = new IntVar[n];
        for(int i = 0; i < n; i++){
            x[i] = model.intVar("X" + i,1,n);
            d1[i] = model.intOffsetView(x[i],(i+1));
            d2[i] = model.intOffsetView(x[i], -(i+1));
        }

        model.allDifferent(x).post();
        model.allDifferent(d1).post();
        model.allDifferent(d2).post();

        boolean hasResult = model.getSolver().solve();

        if (hasResult) {
            for(int i = 0; i < n; i++) {
                System.out.println("x[" + i + "] = " + x[i].getValue());
            }

            for (int i=0; i<n; ++i) {
                for (int j=0; j<n; ++j) {
                    if (x[i].getValue() == j+1) {
                        System.out.print("* ");
                    } else System.out.print("o ");
                }
                System.out.println();
            }
        } else System.out.println("No Solution!");
        model.getSolver().solve();


    }
}
