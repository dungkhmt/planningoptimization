package class118133.nguyenmaiphuong.java;

import localsearch.constraints.basic.AND;
import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.OR;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;
import java.util.Scanner;

public class baitap {
    int W;
    int L;
    int N;
    int[] w = new int[200];
    int[] l = new int[200];
    LocalSearchManager mgr;
    VarIntLS[] x;
    VarIntLS[] y;
    VarIntLS[] o;
    ConstraintSystem S;

    public void input() {
        Scanner scanner = new Scanner(System.in);
        
        try{
            W = scanner.nextInt();
            L = scanner.nextInt();

            N = -1;
            while (1 != 0) {
                int ww = scanner.nextInt();
                if (ww == -1)
                    break;
                
                w[++N] = ww;
                l[N] = scanner.nextInt();
            }
            N++;
        } finally{
            
        }
        scanner.close();
    }

    public void model() {
        mgr = new LocalSearchManager();
        x = new VarIntLS[N];
        y = new VarIntLS[N];
        o = new VarIntLS[N];
        for (int i = 0; i < N; i++) {
            x[i] = new VarIntLS(mgr, 0, W);
            y[i] = new VarIntLS(mgr, 0, L);
            o[i] = new VarIntLS(mgr, 0, 1);
        }
        S = new ConstraintSystem(mgr);

        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                // items i and j cannot overlap
                IConstraint[] c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 0);
                c1[1] = new IsEqual(o[j], 0);
                IConstraint c2 = new AND(c1);
                IConstraint[] c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], l[i]), y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], l[j]), y[i]);
                IConstraint c4 = new OR(c3);
                S.post(new Implicate(c2, c4));
                c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 0);
                c1[1] = new IsEqual(o[j], 1);
                c2 = new AND(c1);
                c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], l[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], l[i]), y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), y[i]);
                c4 = new OR(c3);
                S.post(new Implicate(c2, c4));

                c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 1);
                c1[1] = new IsEqual(o[j], 0);
                c2 = new AND(c1);
                c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], l[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], w[i]), y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], l[j]), y[i]);
                c4 = new OR(c3);
                S.post(new Implicate(c2, c4));
                c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 1);
                c1[1] = new IsEqual(o[j], 1);
                c2 = new AND(c1);
                c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], l[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], l[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], w[i]), y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), y[i]);
                c4 = new OR(c3);
                S.post(new Implicate(c2, c4));
            }
        }

        for (int i = 0; i < N; i++) {
            S.post(new Implicate(new IsEqual(o[i], 0), new LessOrEqual(new FuncPlus(x[i], w[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 0), new LessOrEqual(new FuncPlus(y[i], l[i]), L)));

            S.post(new Implicate(new IsEqual(o[i], 1), new LessOrEqual(new FuncPlus(x[i], l[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 1), new LessOrEqual(new FuncPlus(y[i], w[i]), L)));
        }
        mgr.close();
    }

    public void solve() {
        HillClimbing searcher = new HillClimbing();
        searcher.hillClimbing(S, 100000);
        for (int i = 0; i < N; i++) {
            System.out.println("item " + i + ": " + x[i].getValue() + "," + y[i].getValue() + "," + o[i].getValue());
        }
    }

    public static void main(String[] args) {
        baitap binpacking2D = new baitap();
        binpacking2D.input();
        binpacking2D.model();
        binpacking2D.solve();
    }
}
