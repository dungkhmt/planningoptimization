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
public class BinPacking2Dver2 {
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
                BoolVar cx0 = O[i].eq(0).boolVar();
                BoolVar cy0 = O[j].eq(0).boolVar();
                BoolVar cx1 = O[i].eq(1).boolVar();
                BoolVar cy1 = O[j].eq(1).boolVar();

                BoolVar c1 = X[i].add(listW.get(i)).intVar().le(X[j]).boolVar();
                BoolVar c2 = X[j].add(listW.get(j)).intVar().le(X[i]).boolVar();
                BoolVar c3 = Y[i].add(listH.get(i)).intVar().le(Y[j]).boolVar();
                BoolVar c4 = Y[j].add(listH.get(j)).intVar().le(Y[i]).boolVar();

                BoolVar c1d = X[i].add(listH.get(i)).intVar().le(X[j]).boolVar();
                BoolVar c2d = X[j].add(listH.get(j)).intVar().le(X[i]).boolVar();
                BoolVar c3d = Y[i].add(listW.get(i)).intVar().le(Y[j]).boolVar();
                BoolVar c4d = Y[j].add(listW.get(j)).intVar().le(Y[i]).boolVar();

                model.ifThen(
                        model.and(cx0,cy0),
                        model.or(c1,c2,c3,c4)
                );

                model.ifThen(
                        model.and(cx0,cy1),
                        model.or(c1,c2d,c3,c4d)
                );

                model.ifThen(
                        model.and(cx1,cy0),
                        model.or(c1d,c2,c3d,c4)
                );
                model.ifThen(
                        model.and(cx1,cy1),
                        model.or(c1d,c2d,c3d,c4d)
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
            System.out.println("Solution:");
            int[][] a = new int[W][H];
            for (int i=0; i<W; ++i)
                for(int j=0; j<H; ++j)
                    a[i][j] = 0;
            for(int i = 0; i < N; i++){
                System.out.println("X[" + i + "] = " + X[i].getValue() +"; Y[" + i + "] = " + Y[i].getValue() +"; O[" + i + "] = " + O[i].getValue() );
                int p = listW.get(i);
                int q = listH.get(i);
                if (O[i].getValue()==1) {
                    int tg = p;
                    p = q;
                    q = tg;
                }
                for(int j=X[i].getValue(); j<X[i].getValue()+p; ++j)
                    for(int z=Y[i].getValue(); z<Y[i].getValue()+q; ++z)
                        a[j][z] = a[j][z]+i+1;
            }
            System.out.println("\nVisualization:");
            for (int i=0; i<W; ++i) {
                for (int j = 0; j < H; ++j)
                    System.out.print(a[i][j]);
                System.out.println();
            }
        }else {
            System.out.println("No solution");
        }


    }

    public void readInput(){
        String fileName = "data/BinPacking2D/bin-packing-2D-W10-H8-I6.txt" ;
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
        BinPacking2Dver2 app = new BinPacking2Dver2();
        app.readInput();
        app.solve();
    }
}
