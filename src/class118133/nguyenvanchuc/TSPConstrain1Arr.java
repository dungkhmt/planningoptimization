package class118133.nguyenvanchuc;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.jgrapht.alg.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class TSPConstrainModel {
    int N ;
    int c[][] ;
    Model model = new Model("TSPConstrain1Arr");
    int ub = 0 ;
    private void readFile(){
        try {
            Scanner scanner = new Scanner(new File(".\\data\\TSP\\tsp-5.txt"));
            N = scanner.nextInt() ;
            c = new int[N+1][N+1] ;
            for (int i = 0 ; i < N ; i ++ )
                for (int j = 0 ; j < N ; j++){
                    c[i][j] = scanner.nextInt() ;
                }
            for (int j = 0 ; j < N ; j++){
                c[N][j] = c[0][j];
                c[j][N] = c[j][0];
            }
            for (int i = 0 ; i < N ; i++){
                ub += c[i][i+1] ;
            }
        }catch (Exception e){

        }
    }
    IntVar[] x;
    IntVar[] y;
    public void constrainBuilder(){
        x  = new IntVar[N] ;
        y = new IntVar[N+1];
        for (int i = 0 ; i < N ; i++){
            x[i]= model.intVar("X"+ i, 0, N);
            model.arithm(x[i], "!=", i).post();
        }
        model.allDifferent(x).post();
        for (int i = 0 ; i < N+1 ; i++){
            y[i] = model.intVar("Y"+ i, 0, ub);
        }
        model.arithm(y[0], "=", 0).post();
        for (int i = 0 ; i < N ; i++)
            for (int j = 0 ; j < N+1 ; j++){
                model.ifThen(
                        model.arithm(x[i],"=", j),
                        model.arithm(y[j], "=", model.intOffsetView(y[i],c[i][j]))
                );
            }
    }
    public void solve(){
        model.setObjective(Model.MINIMIZE, y[N]);
        Solution best = model.getSolver().findOptimalSolution(y[N], false);
        System.out.println("Min: " + best.getIntVal(y[N]));
        System.out.println("Found solution");
        for (int i = 0; i < N; i++) {
                System.out.print("X[" + i + "] = " + best.getIntVal(x[i]));
                System.out.println("");
            }

            for (int i = 0; i < N + 1; i++) {
                System.out.print(best.getIntVal(y[i]) + "=>>");
            }

    }
    public static void main(String[] args) {
        TSPConstrainModel tspConstrainModel = new TSPConstrainModel();
        tspConstrainModel.readFile();
        tspConstrainModel.constrainBuilder();
        tspConstrainModel.solve();
    }
}
