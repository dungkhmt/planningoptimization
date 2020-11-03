package class118133.tranthiuyen;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import class118133.tranthiuyen.HillClimbing;

import java.sql.SQLOutput;
import java.util.Scanner;

public class SudokoHillClimbing {
    int N = 9;
    public void solve(){
        LocalSearchManager mgr = new LocalSearchManager();
        ConstraintSystem S = new ConstraintSystem(mgr);
        VarIntLS[][] X= new VarIntLS[N][N];
        for(int i = 0; i < N; i++)
            for(int j =0; j < N; j++) {
                X[i][j] = new VarIntLS(mgr, 1, N);
            }

        for(int i = 0; i < N; i++)
            for(int j =0; j < N; j++) {
                X[i][j].setValue(j);
            }


        for(int i=0; i<N; i++) {
            VarIntLS[] d = new VarIntLS[N];
            for (int j = 0; j < N; ++j) {
                d[j] = X[j][i];

            }
            S.post(new AllDifferent(X[i]));
            S.post(new AllDifferent(d));
        }


        for(int i=0; i<3; i++)
            for (int j = 0; j <3; ++j) {
                VarIntLS[] d = new VarIntLS[9];
                for(int i2=0; i2<3; ++i2)
                    for (int j2 = 0; j2 < 3; ++j2) {
                        int id2 = i2*3+j2;
//                        System.out.println(id2);
                        d[id2] = X[3*i+i2][3*j+j2];
                    }
                S.post(new AllDifferent(d));
            }
        mgr.close();
        HillClimbing searcher = new HillClimbing();
        searcher.hillClimbing(S, X, 100000);
        if (S.violations()==0) {
            System.out.println("Solution: ");
            for(int i=0; i<N; ++i) {
                for (int j = 0; j < N; ++j) {
                    if (j%3==0)
                        System.out.print("|");
                    System.out.print(X[i][j].getValue()+1);
                }
                System.out.println("|");
                if (i%3==2)
                    System.out.print("------------\n");
            }
        }
    }
    public static void main(String[] args) {
        SudokoHillClimbing app = new SudokoHillClimbing();
        System.out.print("N =");
        Scanner sc = new Scanner(System.in);
        //        app.loadData("data/TSP/tsp-100.txt");
        app.solve();
        //        app.solveDynSEC();
    }
}
