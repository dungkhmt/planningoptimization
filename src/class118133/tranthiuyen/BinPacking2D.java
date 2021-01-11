package class118133.tranthiuyen;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class BinPacking2D {
    int W ;
    int H ;
    int N;
    List<Integer> listW = new ArrayList<>();
    List<Integer> listH = new ArrayList<>();
    Model model = new Model("BinPacking2D");
    public void solve(){
        IntVar[] X = new IntVar[N];
        IntVar[] Y = new IntVar[N];
        IntVar[] O = new IntVar[N];
        for(int i = 0; i < N; i++){
            X[i] = model.intVar("X" + i,0,W-1);
            Y[i] = model.intVar("Y" + i,0,H-1);
            O[i] = model.intVar("O" + i,0,1);
        }

        for(int i=0; i<N; i++){
            for(int j=i+1; j<N; j++){
                BoolVar cx = O[i].eq(0).boolVar();
                BoolVar cy = O[j].eq(0).boolVar();
                BoolVar c1 = X[i].add(listW.get(i)).intVar().le(X[j]).boolVar();
                BoolVar c2 = X[j].add(listW.get(j)).intVar().le(X[i]).boolVar();
                BoolVar c3 = Y[i].add(listH.get(i)).intVar().le(Y[j]).boolVar();
                BoolVar c4 = Y[j].add(listH.get(j)).intVar().le(Y[i]).boolVar();
                model.ifThen(
                        model.and(cx, cy),
                        model.or(c1, c2, c3, c4)
                );

                BoolVar cx2 = O[i].eq(0).boolVar();
                BoolVar cy2 = O[j].eq(1).boolVar();
                BoolVar c12 = X[i].add(listW.get(i)).intVar().le(X[j]).boolVar();
                BoolVar c22 = X[j].add(listH.get(j)).intVar().le(X[i]).boolVar();
                BoolVar c32 = Y[i].add(listH.get(i)).intVar().le(Y[j]).boolVar();
                BoolVar c42 = Y[j].add(listW.get(j)).intVar().le(Y[i]).boolVar();
                model.ifThen(
                        model.and(cx2, cy2),
                        model.or(c12, c22, c32, c42)
                );

                BoolVar cx3 = O[i].eq(1).boolVar();
                BoolVar cy3 = O[j].eq(0).boolVar();
                BoolVar c13 = X[i].add(listH.get(i)).intVar().le(X[j]).boolVar();
                BoolVar c23 = X[j].add(listW.get(j)).intVar().le(X[i]).boolVar();
                BoolVar c33 = Y[i].add(listW.get(i)).intVar().le(Y[j]).boolVar();
                BoolVar c43 = Y[j].add(listH.get(j)).intVar().le(Y[i]).boolVar();
                model.ifThen(
                        model.and(cx3, cy3),
                        model.or(c13, c23, c33, c43)
                );

                BoolVar cx4 = O[i].eq(1).boolVar();
                BoolVar cy4 = O[j].eq(1).boolVar();
                BoolVar c14 = X[i].add(listH.get(i)).intVar().le(X[j]).boolVar();
                BoolVar c24 = X[j].add(listH.get(j)).intVar().le(X[i]).boolVar();
                BoolVar c34 = Y[i].add(listW.get(i)).intVar().le(Y[j]).boolVar();
                BoolVar c44 = Y[j].add(listW.get(j)).intVar().le(Y[i]).boolVar();
                model.ifThen(
                        model.and(cx4, cy4),
                        model.or(c14, c24, c34, c44)
                );
            }

        }

        for (int i = 0 ; i < N ; i++){
            BoolVar cx3 = O[i].eq(0).boolVar();
            BoolVar cx4 = O[i].eq(1).boolVar();
            model.ifThen(
                    model.and(cx3),
                    model.and(X[i].add(listW.get(i)).intVar().le(W).boolVar())
            );
            model.ifThen(
                    model.and(cx3),
                    model.and(Y[i].add(listH.get(i)).intVar().le(H).boolVar())
            );

            model.ifThen(
                    model.and(cx4),
                    model.and(X[i].add(listH.get(i)).intVar().le(W).boolVar())
            );
            model.ifThen(
                    model.and(cx4),
                    model.and(Y[i].add(listW.get(i)).intVar().le(H).boolVar())
            );

        }

        System.out.println("Start solve");
        Solver solver = model.getSolver();
        if(solver.solve()){
            for(int i = 0; i < N; i++){
                System.out.println("X[" + i + "] = " + X[i].getValue() +"; Y[" + i + "] = " + Y[i].getValue() +"; O[" + i + "] = " + O[i].getValue() );
            }
        }else {
            System.out.println("No solution");
        }


    }

    public void readInput(){
        String fileName = "data/BinPacking2D/bin-packing-2D-W10-H7-I6.txt" ;
        try {
            Scanner scanner = new Scanner(new File(fileName));
            W = scanner.nextInt() ;
            H = scanner.nextInt() ;
            N = 0;
            while (true){
                int x = scanner.nextInt();
                if (x < 0 ) break ;
                listW.add(x);
                int y = scanner.nextInt() ;
                if (y < 0 ) break;
                listH.add(y);
                N = N+1;
            }
//            System.out.println(N);
//            for (int i=0; i<N; ++i)
//                System.out.println(i+ "  " + listW.get(i)+ " "+listH.get(i));
        } catch (Exception e){
            System.out.println("Read fail");
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        BinPacking2D app = new BinPacking2D();
        app.readInput();
        app.solve();
    }
}
