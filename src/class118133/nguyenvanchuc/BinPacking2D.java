package class118133.nguyenvanchuc;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.util.Scanner;

public class BinPacking2D {
    static int L;
    static int N;
    static int W;
    static int[] w;
    static int[] l;


    public static void main(String[] args) {


        String fileName = "D:\\IdeaProject\\planningoptimization\\data\\BinPacking2D\\bin-packing-2D-W10-H7-I6.txt";
        try{
            int cnt=0;
            Scanner sc = new Scanner( new File(fileName));
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
        Model model = new Model("BinPacking2D");
        IntVar[] x= new IntVar[N];
        IntVar[] y= new IntVar[N];
        IntVar[] o= new IntVar[N];

        for (int i=0; i<N;i++){
            x[i]= model.intVar("x_"+i, 0, W);
            y[i]= model.intVar("y_"+i, 0, L);
            o[i]= model.intVar("o_"+i, 0, 1);
        }
        System.out.println("initial done");
        System.out.println("N: "+ N);
        System.out.println("W: "+ W);
        System.out.println("L: "+ L);

        // add constraint
        // oi = 0  xi + wi ≤ W  yi + li ≤ L
        // oi = 1  xi + li ≤ W  yi + wi ≤ L
        for (int i=0; i<N; i++){
            (o[i].eq(0).imp(x[i].add(w[i]).le(W)).and(y[i].add(l[i]).le(L))).post();
            (o[i].eq(1).imp(x[i].add(l[i]).le(L).and(y[i].add(w[i]).le(W)))).post();
        }

//        (oi = 0  oj = 0)  (xi + wi ≤ xj  xj + wj ≤ xi   yi + hi ≤ yj  yj + hj ≤ yi )
//        (oi = 0  oj = 1)  (xi + wi ≤ xj  xj + hj ≤ xi   yi + hi ≤ yj  yj + wj ≤ yi )
//        (oi = 1  oj = 0)  (xi + hi ≤ xj  xj + wj ≤ xi   yi + wi ≤ yj  yj + hj ≤ yi )
//        (oi = 1  oj = 1)  (xi + hi ≤ xj  xj + hj ≤ xi   yi + wi ≤ yj  yj + wj ≤ yi )
        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                if(i!=j){
                    (o[i].eq(0).and(o[j].eq(0))).imp((x[i].add(w[i]).le(x[j])).or(x[j].add(w[j]).le(x[i])).or(y[i].add(l[i]).le(y[j])).or(y[j].add(l[j]).le(y[i]))).post();
                    (o[i].eq(0).and(o[j].eq(1))).imp((x[i].add(w[i]).le(x[j])).or(x[j].add(l[j]).le(x[i])).or(y[i].add(l[i]).le(y[j])).or(y[j].add(w[j]).le(y[i]))).post();
                    (o[i].eq(1).and(o[j].eq(0))).imp((x[i].add(l[i]).le(x[j])).or(x[j].add(w[j]).le(x[i])).or(y[i].add(w[i]).le(y[j])).or(y[j].add(l[j]).le(y[i]))).post();
                    (o[i].eq(1).and(o[j].eq(1))).imp((x[i].add(l[i]).le(x[j])).or(x[j].add(l[j]).le(x[i])).or(y[i].add(w[i]).le(y[j])).or(y[j].add(w[j]).le(y[i]))).post();
                }
            }
        }
        System.out.println("add constrain done");

        if(model.getSolver().solve()){
            for (int i=0; i<N; i++){
                System.out.println("item " + i + ": " + x[i].getValue() + "," +
                        y[i].getValue() + "," + o[i].getValue());
            }
        }else{
            System.out.println("-1");
        }

    }
}
