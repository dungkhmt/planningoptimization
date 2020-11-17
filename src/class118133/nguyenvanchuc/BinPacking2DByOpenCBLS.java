package class118133.nguyenvanchuc;

import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.constraints.basic.AND;
import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.OR;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.search.HillClimbing;

import java.io.File;
import java.util.Scanner;

public class BinPacking2DByOpenCBLS {
    int W;
    int L;
    int N;

    int[] w;
    int[] l;


    public void solve(){
        String fileName = "D:\\IdeaProject\\planningoptimization\\data\\BinPacking2D\\bin-packing-2D-W10-H7-I6.txt";
        try{
            int cnt=0;
            Scanner sc = new Scanner(fileName);
            while(sc.hasNextLine()) {
                sc.nextLine();
                cnt++;
            }
            N= cnt-1;
            w= new int[N];
            l= new int[N];

            sc.close();
        }catch (Exception e){

        }

        try {
            Scanner scanner = new Scanner(new File(fileName));
            W = scanner.nextInt();
            L = scanner.nextInt();
            int count =0;
            while (true) {
                int w_i = scanner.nextInt();
                if (w_i < 0) break;

                int l_i = scanner.nextInt();
                if (l_i < 0) break;
                count++;
                l[count]=l_i;
                w[count]=w_i;
            }
            scanner.close();
        } catch (Exception e) {

        }
        System.out.println("initial done");
        System.out.println("N: "+ N);
        System.out.println("W: "+ W);
        System.out.println("L: "+ L);


        LocalSearchManager mgr;
        VarIntLS[] x;
        VarIntLS[] y;
        VarIntLS[] o;
        ConstraintSystem S;

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
        for(int i = 0; i < N; i++){
            S.post(new Implicate(new IsEqual(o[i], 0),
                    new LessOrEqual(new FuncPlus(x[i], w[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 0),
                    new LessOrEqual(new FuncPlus(y[i], l[i]), L)));

            S.post(new Implicate(new IsEqual(o[i], 1),
                    new LessOrEqual(new FuncPlus(x[i], l[i]), W)));
            S.post(new Implicate(new IsEqual(o[i], 1),
                    new LessOrEqual(new FuncPlus(y[i], w[i]), L)));
        }
        mgr.close();

        //
        HillClimbing searcher = new HillClimbing();
        searcher.hillClimbing(S, 100000);
        for(int i = 0; i < N; i++){
            System.out.println("item " + i + ": " + x[i].getValue() + "," +
                    y[i].getValue() + "," + o[i].getValue());
        }
    }
    public static void main(String[] args) {
        BinPacking2DByOpenCBLS app = new BinPacking2DByOpenCBLS();
        app.solve();
    }
}





