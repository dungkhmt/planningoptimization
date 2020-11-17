package class118133.vutrungnghia.backpacking;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.util.Scanner;

public class UseTools {
    class Box{
        int w, l;
        Box(int w, int l){
            this.w = w;
            this.l = l;
        }
    }
    int W, L,  N;
    Model model = new Model("example");
    IntVar[] X;
    IntVar[] Y;
    IntVar[] F;
    Box[] Boxes;
    String filename = "data/BinPacking2D/bin-packing-2D-W10-H7-I6.txt";
    public void readN(){
        N = 0;
        try{
            Scanner in = new Scanner(new File(this.filename));
            int counter = 0;
            while(in.nextInt() != -1){
                counter++;
            }
            N = (int)((counter - 2) / 2);
            in.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readInput(){
        try{
            Scanner in = new Scanner(new File(this.filename));
            W = in.nextInt();
            L = in.nextInt();
            Boxes = new Box[N];
            for(int i = 0; i < N; ++i){
                int w = in.nextInt();
                int l = in.nextInt();
                Boxes[i] = new Box(w, l);
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setConstraints(){
        X = new IntVar[N];
        Y = new IntVar[N];
        F = new IntVar[N];
        for(int i = 0; i < N; ++i){
            X[i] = model.intVar("X[" + i + "]", 0, W);
            Y[i] = model.intVar("Y[" + i + "]", 0, L);
            F[i] = model.intVar("F[" + i + "]", 0, 1);
        }

        for(int i = 0; i < N; ++i){
            model.arithm(model.intOffsetView(X[i], Boxes[i].w - W), "+", model.intScaleView(F[i], Boxes[i].l), "<=", model.intScaleView(F[i], Boxes[i].w)).post();
            model.arithm(model.intOffsetView(Y[i], Boxes[i].l - L), "+", model.intScaleView(F[i], Boxes[i].w), "<=", model.intScaleView(F[i], Boxes[i].l)).post();
        }

        for(int i = 0; i < N - 1; ++i){
            for(int j = i + 1; j < N; ++j){
                model.ifThen(
                        model.and(model.arithm(F[i], "=", 0), model.arithm(F[j], "=", 0)),
                        model.or(
                                model.arithm(model.intOffsetView(X[i], Boxes[i].w), "<=", X[j]),
                                model.arithm(model.intOffsetView(X[j], Boxes[j].w), "<=", X[i]),
                                model.arithm(model.intOffsetView(Y[i], Boxes[i].l), "<=", Y[j]),
                                model.arithm(model.intOffsetView(Y[j], Boxes[j].l), "<=", Y[i])
                        ));
                model.ifThen(
                        model.and(model.arithm(F[i], "=", 0), model.arithm(F[j], "=", 1)),
                        model.or(
                                model.arithm(model.intOffsetView(X[i], Boxes[i].w), "<=", X[j]),
                                model.arithm(model.intOffsetView(X[j], Boxes[j].l), "<=", X[i]),
                                model.arithm(model.intOffsetView(Y[i], Boxes[i].l), "<=", Y[j]),
                                model.arithm(model.intOffsetView(Y[j], Boxes[j].w), "<=", Y[i])
                                ));
                model.ifThen(
                        model.and(model.arithm(F[i], "=", 1), model.arithm(F[j], "=", 0)),
                        model.or(
                                model.arithm(model.intOffsetView(X[i], Boxes[i].l), "<=", X[j]),
                                model.arithm(model.intOffsetView(X[j], Boxes[j].w), "<=", X[i]),
                                model.arithm(model.intOffsetView(Y[i], Boxes[i].w), "<=", Y[j]),
                                model.arithm(model.intOffsetView(Y[j], Boxes[j].l), "<=", Y[i])
                                ));
                model.ifThen(
                        model.and(model.arithm(F[i], "=", 1), model.arithm(F[j], "=", 1)),
                        model.or(
                                model.arithm(model.intOffsetView(X[i], Boxes[i].l), "<=", X[j]),
                                model.arithm(model.intOffsetView(X[j], Boxes[j].l), "<=", X[i]),
                                model.arithm(model.intOffsetView(Y[i], Boxes[i].w), "<=", Y[j]),
                                model.arithm(model.intOffsetView(Y[j], Boxes[j].w), "<=", Y[i])
                                ));
            }
        }
    }

    public void solve(){
        this.readN();
        System.out.println(N);
        this.readInput();
        System.out.println(W + " " + L);
        for(int i = 0; i < N; ++i){
            System.out.println(Boxes[i].w + " " + Boxes[i].l);
        }
        this.setConstraints();
        System.out.println("solve...");
        this.model.getSolver().solve();
        for (int i = 0; i < N; ++i) {
            System.out.println("Box: " + i);
            System.out.println("X: " + X[i].getValue() + ",Y: " + Y[i].getValue());
            System.out.println("F:" + F[i]);
            System.out.println("w: " + Boxes[i].w + ",l: " + Boxes[i].l);
            System.out.println("----------------------------------------------");
        }
    }

    public static void main(String[] args) {
        UseTools version1 = new UseTools();
        version1.solve();
    }
}
