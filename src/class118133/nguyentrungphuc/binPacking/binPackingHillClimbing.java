package code;

import core.VarInt;
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

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class binPacking {
    int W, H;
    int N;
    static int[] w;
    static int[] h;
    LocalSearchManager mgr;
    VarIntLS[] x;
    VarIntLS[] y;
    VarIntLS[] o;
    static ConstraintSystem S;

    private void stateModel() {
        mgr = new LocalSearchManager();
        x = new VarIntLS[N];
        y = new VarIntLS[N];
        o = new VarIntLS[N];
        for (int i = 0; i < N; i++) {
            x[i] = new VarIntLS(mgr, 0, W);
            y[i] = new VarIntLS(mgr, 0, H);
            o[i] = new VarIntLS(mgr, 0, 1);
        }
        S = new ConstraintSystem(mgr);

        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                IConstraint[] c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 0);
                c1[1] = new IsEqual(o[j], 0);
                IConstraint c2 = new AND(c1);
                IConstraint[] c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], h[i]),y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], h[j]),y[i]);
                IConstraint c4 = new OR(c3);
                S.post(new Implicate(c2,c4));

                c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 0);
                c1[1] = new IsEqual(o[j], 1);
                c2 = new AND(c1);
                c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], w[i]),x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], h[j]),x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], h[i]),y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], w[j]),y[i]);
                c4 = new OR(c3);
                S.post(new Implicate(c2, c4));

                c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 1);
                c1[1] = new IsEqual(o[j], 0);
                c2 = new AND(c1);
                c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], h[i]),x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], w[j]),x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], w[i]),y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], h[j]),y[i]);
                c4 = new OR(c3);
                S.post(new Implicate(c2, c4));

                c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 1);
                c1[1] = new IsEqual(o[j], 1);
                c2 = new AND(c1);
                c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], h[i]),x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], h[j]),x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], w[i]),y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], w[j]),y[i]);
                c4 = new OR(c3);
                S.post(new Implicate(c2, c4));
            }
        }

        for(int i=0;i<N;i++){
            S.post(new Implicate(new IsEqual(o[i], 0),
                    new LessOrEqual(new FuncPlus(x[i], w[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 0),
                    new LessOrEqual(new FuncPlus(y[i], h[i]), H)));

            S.post(new Implicate(new IsEqual(o[i], 1),
                    new LessOrEqual(new FuncPlus(x[i], h[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 1),
                    new LessOrEqual(new FuncPlus(y[i], w[i]), H)));

        }
        mgr.close();
    }

    public void loadData() {
        ArrayList<Integer> w1 = new ArrayList<Integer>();
        ArrayList<Integer> h1 = new ArrayList<Integer>();
        try {
            FileReader fr = new FileReader("data\\BinPacking2D\\bin-packing-2D.txt");
            W = fr.read();
            H = fr.read();
            int i;
            while ((i = fr.read()) != -1) {
                w1.add(i);
                h1.add(fr.read());
            }
            w = new int[w1.size()];
            h = new int[w1.size()];
            N = w1.size();
            for (int j = 0; j < w1.size(); j++) {
                w[j] = w1.get(j);
                h[j] = h1.get(j);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search(){
        HillClimbing searcher = new HillClimbing();
        searcher.hillClimbing(S,100000);

        for(int i = 0; i < N; i++){
            System.out.println("item " + i + ": " + x[i].getValue() + "," +
                    y[i].getValue() + "," + o[i].getValue());
        }
    }

    public void solve(){
        stateModel();
        search();
    }


    public static void main(String[] args) {
        binPacking ins = new binPacking();
        ins.loadData();
        for(int i=0;i<ins.N;i++){
            System.out.println(w[i]);
            System.out.println(h[i]);
        }
        ins.solve();
    }
}
