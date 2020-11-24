package class118133.vutrungnghia.tsp;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.util.Scanner;

public class TSP_v3 {
    Model model = new Model("example");
    IntVar[] X;
    IntVar[] Y;
    int N;
    int[][] d;

    public void readInput() {
        try {
            Scanner in = new Scanner(new File("data/TSP/tsp-13.txt"));
            N = in.nextInt();
            d = new int[N + 1][N + 1];
            for(int i = 0; i < N; ++i){
                for(int j = 0; j < N; ++j){
                    d[i][j] = in.nextInt();
                }
            }
            for(int i = 0; i <= N; ++i)
                d[i][N] = d[0][N];
            for(int j = 0; j <= N; ++j)
                d[N][j] = 0;
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setConstraints(){
        X = new IntVar[N];
        Y = new IntVar[N + 1];
        for (int i = 0; i < N; ++i) {
            if(i == 0)
                X[i] = model.intVar("X[" + i + "]", 1, N-1);
            else
                X[i] = model.intVar("X[" + i + "]", 1, N);
        }

        int sum = 0;
        for(int i = 0; i <= N; ++i)
            for(int j = 0; j <= N; ++j)
                sum += d[i][j];
        for(int i = 0; i <= N; ++i){
            if(i == 0)
                Y[i] = model.intVar("Y[" + i + "]", 0, 0);
            else
                Y[i] = model.intVar("Y[" + i + "]", 0, sum);
        }

        // X[i] != i
        for(int i = 0; i < N; ++i)
            model.arithm(X[i], "!=", i).post();

        model.allDifferent(X).post();

        // X[i] = j => Y[j] = Y[i] + d[i, j]
        for(int i = 0; i < N; ++i){
            for(int j = 1; j <= N; ++j){
                model.ifThen(model.arithm(X[i], "=", j), model.arithm(Y[j], "=", model.intOffsetView(Y[i], d[i][j])));
            }
        }
    }

    public void solve(){
        model.setObjective(Model.MINIMIZE, Y[N]);
        while(model.getSolver().solve()){
            System.out.println("Y[N]: " + Y[N].getValue());
        }
//        model.getSolver().solve();
        for (int i = 0; i < N; ++i) {
            System.out.println("X[" + i + "] = " + X[i].getValue());
        }
        System.out.println("Y[N]: " + Y[N].getValue());
    }

    public static void main(String[] args) {
        TSP_v3 tsp_v3 = new TSP_v3();
        tsp_v3.readInput();
        tsp_v3.setConstraints();
        tsp_v3.solve();
    }
}
