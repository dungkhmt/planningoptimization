package class118133.tranthiuyen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.util.Scanner;

public class TSPinCPmodel {

    private int N;
    private int[][] c ;
    Model model = new Model("TSP");
    private int MAXY = 0;

    //
    private void readFile(){
        try {
            Scanner scanner = new Scanner(new File(".\\data\\TSP\\tsp-10.txt"));
            N = scanner.nextInt() ;
            c = new int[N][N] ;
            for (int i = 0 ; i < N ; i ++ )
                for (int j = 0 ; j < N ; j++){
                    c[i][j] = scanner.nextInt() ;
                    MAXY = MAXY + c[i][j];
                }
        }catch (Exception e){
            System.out.println("Read fail");
        }
    }

    private void solve(){
        IntVar[] x = new IntVar[N];
        IntVar[] y = new IntVar[N+1];
        for(int i = 0; i < N; i++) {
            x[i] = model.intVar("X" + i, 1, N);
        }
        for(int i = 0; i <= N; i++) {
            y[i] = model.intVar("Y["+i+']', 0, MAXY);
        }
        // contraint
        y[0].eq(0).post();
        for(int i = 0; i < N; i++) {
            model.arithm(x[i],"!=", i).post();
        }
        model.allDifferent(x).post();
        for(int i=0; i<N; i++) {
            for(int j=1; j<=N; j++) {
                int d = c[i%N][j%N];
                model.ifThen(
                        model.arithm(x[i],"=",j),
                        model.arithm(y[j],"=",y[i],"+", d)
                );
            }
        }
        // solver
        System.out.println("Start solve for N = "+N );
        model.setObjective(model.MINIMIZE, y[N]);
        Solver solver = model.getSolver();
        if (solver.solve()) {
            while (solver.solve()) {
                System.out.println("Objective: " + model.getObjective());
            }
            System.out.println("Solution: ");
            for (int i = 0; i < N; i++) {
                System.out.println("x[" + i + "] = " + x[i].getValue());
            }
            System.out.println("Objective: " + model.getObjective());
        }else{
            System.out.println("Solution not found");
        }


    }

    public static void main(String[] args) {

        // TODO Auto-generated method stub
        TSPinCPmodel app = new TSPinCPmodel();
        app.readFile();
        app.solve();
    }
}
