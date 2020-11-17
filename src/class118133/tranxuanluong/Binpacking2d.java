import localsearch.constraints.basic.*;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Binpacking2d {
    int W = 4;
    int L = 6;
    int n = 3;
    int[] w ;
    int[] l ;
    LocalSearchManager mgr;
    VarIntLS[] x;
    VarIntLS[] y;
    VarIntLS[] o;
    ConstraintSystem S;
    private void initData() throws FileNotFoundException {
        File input = new File("/home/luong/Desktop/bin-packing-2D.txt");
        Scanner sc = new Scanner(input);
        if (sc.hasNext()){
            W = sc.nextInt();
            L = sc.nextInt();
        }
        ArrayList<Integer> arrW = new ArrayList<>();
        ArrayList<Integer> arrL = new ArrayList<>();
        while (sc.hasNext()){
            int temp = sc.nextInt();
            if(temp==-1) break;
            arrW.add(temp);
            arrL.add(sc.nextInt());
        }
        n = arrL.size() -1;
        w = new int[n];
        l = new int[n];
        for (int i  = 0; i < n; i++){
            w[i] = arrW.get(i);
            l[i] = arrL.get(i);
        }
    }
    private void stateModel() {
        mgr = new LocalSearchManager();
        x = new VarIntLS[n];
        y = new VarIntLS[n];
        o = new VarIntLS[n];
        for (int i = 0; i < n; i++) {
            x[i] = new VarIntLS(mgr, 0, W);
            y[i] = new VarIntLS(mgr, 0, L);
            o[i] = new VarIntLS(mgr, 0, 1);
        }
        S = new ConstraintSystem(mgr);
        // rang buoc khong bi overlap khi khong xoay
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                IConstraint[] c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 0);//
                c1[1] = new IsEqual(o[j], 0);
                IConstraint c2 = new AND(c1);
                IConstraint[] c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], l[i]), y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], l[j]), y[i]);
                IConstraint c4 = new OR(c3);
                S.post(new Implicate(c2, c4));
            }
        }
        // rang buoc khong bi overlap khi xoay j
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                IConstraint[] c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 0);//
                c1[1] = new IsEqual(o[j], 1);
                IConstraint c2 = new AND(c1);
                IConstraint[] c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], w[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], l[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], l[i]), y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), y[i]);
                IConstraint c4 = new OR(c3);
                S.post(new Implicate(c2, c4));
            }
        }
        // rang buoc khong bi overlap khi xoay i
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                IConstraint[] c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 1);//
                c1[1] = new IsEqual(o[j], 0);
                IConstraint c2 = new AND(c1);
                IConstraint[] c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], l[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], w[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], w[i]), y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], l[j]), y[i]);
                IConstraint c4 = new OR(c3);
                S.post(new Implicate(c2, c4));
            }
        }
        // rang buoc khong bi overlap khi xoay i
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                IConstraint[] c1 = new IConstraint[2];
                c1[0] = new IsEqual(o[i], 1);//
                c1[1] = new IsEqual(o[j], 1);
                IConstraint c2 = new AND(c1);
                IConstraint[] c3 = new IConstraint[4];
                c3[0] = new LessOrEqual(new FuncPlus(x[i], l[i]), x[j]);
                c3[1] = new LessOrEqual(new FuncPlus(x[j], l[j]), x[i]);
                c3[2] = new LessOrEqual(new FuncPlus(y[i], w[i]), y[j]);
                c3[3] = new LessOrEqual(new FuncPlus(y[j], w[j]), y[i]);
                IConstraint c4 = new OR(c3);
                S.post(new Implicate(c2, c4));
            }
        }
        // rang buoc khong vuot qua kich thuoc container
        for (int i = 0; i < n; i++) {
            S.post(new Implicate(new IsEqual(o[i], 0), new LessOrEqual(new FuncPlus(x[i], w[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 0), new LessOrEqual(new FuncPlus(y[i], l[i]), L)));
            S.post(new Implicate(new IsEqual(o[i], 1), new LessOrEqual(new FuncPlus(x[i], l[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 1), new LessOrEqual(new FuncPlus(y[i], w[i]), L)));
        }
        mgr.close();
    }
    private void search(){
        GenericHillClimbingSearch searcher = new GenericHillClimbingSearch(S);
        searcher.search(100000);
        for(int i = 0; i < n; i++){
            System.out.println("item " + i + ": " + x[i].getValue() + "," +
                    y[i].getValue() + "," + o[i].getValue());
        }
    }
    public void solve() throws FileNotFoundException {
        initData();
        stateModel();
        search();
    }

    public static void main(String[] args) throws FileNotFoundException {
        Binpacking2d app = new Binpacking2d();
        app.solve();
    }
}

