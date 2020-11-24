package class118133.phamminhkhiem;

import com.google.ortools.constraintsolver.Constraint;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.jgrapht.alg.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BinPacking2D {
    int W ;
    int L ;
    Model model = new Model("Binpacking2d");
    List<Pair<Integer, Integer>>  bin = new ArrayList<>();
    int N ;
    public void readInput(){
        String fileName = "data/BinPacking2D/bin-packing-2D-W10-H7-I6.txt" ;
        try {
            Scanner scanner = new Scanner(new File(fileName));
            W = scanner.nextInt() ;
            L = scanner.nextInt() ;
            while (scanner.hasNext()){
                int x = scanner.nextInt() ;
                if (x < 0 ) break ;
                int y = scanner.nextInt() ;
                if (y < 0 ) break;
                bin.add(new Pair<>(x,y));
            }
            N = bin.size() ;
        } catch (Exception e){

        }
    }
    public void constrainBuilder(){
        IntVar[] x = new IntVar[N] ;
        IntVar[] y = new IntVar[N] ;
        IntVar[] z = new IntVar[N] ;
        for (int i = 0 ; i < N ; i ++){
            x[i] = model.intVar("x["+ i + "]", 0,W-1) ;
            y[i] = model.intVar("y["+ i + "]", 0,L-1);
            z[i] = model.intVar("z[i"+ i + "]", 0,1);
        }
        for (int i = 0 ; i < N ; i++){
            for (int j = i+1 ; j < N ; j++) {
                BoolVar cx = z[i].eq(0).boolVar();
                BoolVar cy = z[j].eq(0).boolVar();
                BoolVar c1 = x[i].add(bin.get(i).getFirst()).intVar().le(x[j]).boolVar();
                BoolVar c2 = x[j].add(bin.get(j).getFirst()).intVar().le(x[i]).boolVar();
                BoolVar c3 = y[i].add(bin.get(i).getSecond()).intVar().le(y[j]).boolVar();
                BoolVar c4 = y[j].add(bin.get(j).getSecond()).intVar().le(y[i]).boolVar();
                model.ifThen(
                        model.and(cx, cy),
                        model.or(c1, c2, c3, c4)
                );

                BoolVar cx2 = z[i].eq(0).boolVar();
                BoolVar cy2 = z[j].eq(1).boolVar();
                BoolVar c12 = x[i].add(bin.get(i).getFirst()).intVar().le(x[j]).boolVar();
                BoolVar c22 = x[j].add(bin.get(j).getSecond()).intVar().le(x[i]).boolVar();
                BoolVar c32 = y[i].add(bin.get(i).getSecond()).intVar().le(y[j]).boolVar();
                BoolVar c42 = y[j].add(bin.get(j).getFirst()).intVar().le(y[i]).boolVar();
                model.ifThen(
                        model.and(cx2, cy2),
                        model.or(c12, c22, c32, c42)
                );

                BoolVar cx3 = z[i].eq(1).boolVar();
                BoolVar cy3 = z[j].eq(0).boolVar();
                BoolVar c13 = x[i].add(bin.get(i).getSecond()).intVar().le(x[j]).boolVar();
                BoolVar c23 = x[j].add(bin.get(j).getFirst()).intVar().le(x[i]).boolVar();
                BoolVar c33 = y[i].add(bin.get(i).getFirst()).intVar().le(y[j]).boolVar();
                BoolVar c43 = y[j].add(bin.get(j).getSecond()).intVar().le(y[i]).boolVar();
                model.ifThen(
                        model.and(cx3, cy3),
                        model.or(c13, c23, c33, c43)
                );

                BoolVar cx4 = z[i].eq(1).boolVar();
                BoolVar cy4 = z[j].eq(1).boolVar();
                BoolVar c14 = x[i].add(bin.get(i).getSecond()).intVar().le(x[j]).boolVar();
                BoolVar c24 = x[j].add(bin.get(j).getSecond()).intVar().le(x[i]).boolVar();
                BoolVar c34 = y[i].add(bin.get(i).getFirst()).intVar().le(y[j]).boolVar();
                BoolVar c44 = y[j].add(bin.get(j).getFirst()).intVar().le(y[i]).boolVar();
                model.ifThen(
                        model.and(cx4, cy4),
                        model.or(c14, c24, c34, c44)
                );
            }
        }
        for (int i = 0 ; i < N ; i++){
            BoolVar cx3 = z[i].eq(0).boolVar();
            BoolVar cx4 = z[i].eq(1).boolVar();
            model.ifThen(
                    model.and(cx3),
                    model.and(x[i].add(bin.get(i).getFirst()).intVar().le(W).boolVar())
            );
            model.ifThen(
                    model.and(cx3),
                    model.and(y[i].add(bin.get(i).getSecond()).intVar().le(L).boolVar())
            );

            model.ifThen(
                    model.and(cx4),
                    model.and(x[i].add(bin.get(i).getSecond()).intVar().le(W).boolVar())
            );
            model.ifThen(
                    model.and(cx4),
                    model.and(y[i].add(bin.get(i).getFirst()).intVar().le(L).boolVar())
            );

        }
        if (model.getSolver().solve()){
            System.out.println("Solved !!");
            System.out.println("X[i]\tY[i]\tO[i]\n" );
            for (int i = 0 ; i < N ; i ++)
                System.out.println(x[i].getValue() + "\t" + y[i].getValue() + "\t" + z[i].getValue() + "\n");
        } else {
            System.out.println("Solution not found !!!");
        }


    }

    public static void main(String[] args) {
        BinPacking2D binPacking2D = new BinPacking2D();
        binPacking2D.readInput();
        binPacking2D.constrainBuilder();
    }
}
