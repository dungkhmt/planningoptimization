import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import chocosolver.ChocoSolver;

public class Main {
    int k, n;
    int[] w, l;
    int[] W, L, C;

    public static void main(String[] args) {
        String fn = "data1.txt";
        Main app = new Main();
        app.read_data(fn);

        ChocoSolver solver = new ChocoSolver();
        solver.solve(app.k, app.n, app.W, app.L, app.w, app.l, app.C);


    }

    void read_data(String fn) {
        File file = new File(fn);

        try {
            Scanner scanner = new Scanner(file);
            n = scanner.nextInt();
            k = scanner.nextInt();
            System.out.println(n + " " + k);

            w = new int[n];
            l = new int[n];
            for (int i = 0; i < n; ++i) {
                w[i] = scanner.nextInt();
                l[i] = scanner.nextInt();
                System.out.println(i + " " + w[i] + " " + l[i]);
            }

            W = new int[k];
            L = new int[k];
            C = new int[k];
            for (int i = 0; i < k; ++i) {
                W[i] = scanner.nextInt();
                L[i] = scanner.nextInt();
                C[i] = scanner.nextInt();
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
