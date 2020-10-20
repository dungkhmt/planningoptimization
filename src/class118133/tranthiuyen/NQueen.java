package class118133.tranthiuyen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.util.Scanner;

public class NQueen {
    int n;
    public void solve(){
        Model model = new Model("Queen");
        IntVar[] x = new IntVar[n];
        IntVar[] d1 = new IntVar[n];
        IntVar[] d2 = new IntVar[n];
        for(int i = 0; i < n; i++){
            x[i] = model.intVar("X" + i,1,n);
            d1[i] = model.intOffsetView(x[i],i);
            d2[i] = model.intOffsetView(x[i], -i);
        }

        model.allDifferent(x).post();
        model.allDifferent(d1).post();
        model.allDifferent(d2).post();

        Solver solver = model.getSolver();
        if(solver.solve()){
            for(int i = 0; i < n; i++)
                System.out.println("x[" + i + "] = " + x[i].getValue());

            for(int i =0; i<n; ++i){
                for(int j=1; j<=n; ++j)
                    if (j!=x[i].getValue()){
                        System.out.print('.');
                    }else{
                        System.out.print('x');
                    }
                System.out.print('\n');
            }
//        }else if(solver.hasReachedLimit()){
//            System.out.println("The solver could not find a solution nor prove that none exists in the given limits");
        }else {
            System.out.println("No solution");
        }


    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        NQueen app = new NQueen();
        System.out.print("N =");
        Scanner sc = new Scanner(System.in);
        app.n = sc.nextInt();
//        app.loadData("data/TSP/tsp-100.txt");
        app.solve();
//        app.solveDynSEC();
    }

}
