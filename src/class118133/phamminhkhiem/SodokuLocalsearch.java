package class118133.phamminhkhiem;

import core.VarInt;
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import org.jgrapht.alg.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class SodokuLocalsearch {
    public static ConstraintSystem s;
    public static VarIntLS[][] X;
    public static int N = 9;

    public static void constrainMaker() {
        LocalSearchManager localSearchManager = new LocalSearchManager();
        s = new ConstraintSystem(localSearchManager);
        X = new VarIntLS[9][9];
        for (int i = 0; i < 9; i++) {
            X[i] = new VarIntLS[9];
            for (int j = 0; j < 9; j++) {
                X[i][j] = new VarIntLS(localSearchManager,0,8);
                X[i][j].setValuePropagate(j);
            }
        }
        for (int i = 0; i < 9; i++) {
            VarIntLS[] y = new VarIntLS[N];
            s.post(new AllDifferent(X[i]));
            for (int j = 0; j < 9; j++) {
                y[j] = X[j][i];
            }
            s.post(new AllDifferent(y));
        }

        for (int i0=0; i0<3; ++i0)
            for(int j0 = 0 ; j0 < 3 ; j0++  ){
            VarIntLS[] z = new VarIntLS[N];
            int count = 0;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++) {
                    z[count] = X[i0 * 3 + i][j0 * 3 + j];
//                    System.out.println(String.valueOf(k*3+i) + " " + String.valueOf(k*3+j) );
                    count++;
                }
            s.post(new AllDifferent(z));
        }
        s.close();
        localSearchManager.close();
    }

    public static void search() {
        int it = 0;
        System.out.println("Violations : " + s.violations());
        while (it < 10000) {
            int minDelta = Integer.MAX_VALUE;
            ArrayList<Pair> cand = new ArrayList<>();
            for (int k = 0; k < 9; k++)
                for (int i = 0; i < 8; i++)
                    for (int j = i + 1; j < 9; j++) {
                        int d = s.getSwapDelta(X[k][i], X[k][j]);
                        if (d < minDelta) {
                            cand.clear();
                            cand.add(new Pair(k, i, j)) ;
                            minDelta = d ;
                        } else if (d == minDelta){
                            cand.add(new Pair(k,i,j));
                        }
                    }

            Pair m =
                    cand.get(new Random().nextInt(cand.size()));
            X[m.k][m.i].swapValuePropagate(X[m.k][m.j]);
            it++;
            System.out.println("Step " + it + " " + "S.violations : " + s.violations());
            if (s.violations() == 0 ) {
                for (int i = 0 ; i < N ; i ++) {
                    for (int j = 0; j < N; j++) {
                        System.out.print(X[i][j].getValue() + "\t");
                    }
                    System.out.println("\n");
                }
                return;
            }
        }
       // if (s.violations() == 0 ) {
            for (int i = 0 ; i < N ; i ++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(X[i][j].getValue() + "\t");
                }
                System.out.println("\n");
            }
            return;
       // }
    }

    public static void main(String[] args) {
        constrainMaker();
        search();
    }
    static class Pair{
        int k ;
        int i;
        int j ;

        public Pair(int k, int i, int j) {
            this.k = k;
            this.i = i;
            this.j = j;
        }
    }
}
