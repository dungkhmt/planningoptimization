package Project;

import core.HillClimbingSearch;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.functions.basic.ConstraintViolations;
import localsearch.functions.basic.FuncMult;
import localsearch.functions.basic.FuncPlus;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.functions.max_min.Max;
import localsearch.model.*;
import localsearch.selectors.MinMaxSelector;


import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

public class AEHeuristic {
    int N; //paper 0..N-1
    int M; //Reviewer  0..M-1
    int K; //the minium number of reviewers to review each paper
    int[][] L ; //L[i][j] = 1 when reviewer j can review paper i
    Random R = new Random();
    public void readInput(){
        for (int i= 0 ; i < N ; i ++){
            Arrays.fill(L[i],0);
        }

        File fi = new File("src/Project/data500.txt");
        try {
            Scanner scan = new Scanner(fi);
            N = scan.nextInt();
            M = scan.nextInt();
            L = new int[N][M];
            K = scan.nextInt();
            String line = scan.nextLine();
            int i = 0;
            while (scan.hasNext()){
                line = scan.nextLine();
                String[] temp =  line.split(" ");
                for (String s: temp){
                    int j = Integer.parseInt(s);
                    L[i][j-1] = 1;
                }
                i++;
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
    LocalSearchManager mgr;
    VarIntLS[][] assign;
    ConstraintSystem S;
    IFunction obj;
    IFunction F;
    IFunction[] nviewed;    //number of time the paper i was evaluated
    IFunction[] load;        //number of times the scientist rate the paper

    class Move{
        int i; // index of variable
        int v; // value to be assigned
        public Move(int i, int v){
            this.i = i; this.v = v;
        }
    }
    public void exploreNeighborhoodConstraintThenFunction(VarIntLS[][] assign, IConstraint c, IFunction f, List<Move> cand) {
        cand.clear();
        int minDeltaC = Integer.MAX_VALUE;
        int minDeltaF = Integer.MAX_VALUE;
        for (int i = 0; i < assign.length; i++) {
            for (int j = 0; j < assign[i].length; j++) {
                for (int v = assign[i][j].getMinValue(); v <= assign[i][j].getMaxValue(); v++) {
                    int deltaC = c.getAssignDelta(assign[i][j], v);
                    int deltaF = f.getAssignDelta(assign[i][j], v);
                    if (deltaC < 0 || deltaC == 0 && deltaF < 0) {// accept only better neighbors
                        if (deltaC < minDeltaC || deltaC == minDeltaC && deltaF < minDeltaF) {
                            cand.clear();
                            cand.add(new Move(i, v));
                            minDeltaC = deltaC;
                            minDeltaF = deltaF;
                        } else if (deltaC == minDeltaC && deltaF == minDeltaF) {
                            cand.add(new Move(i, v));
                        }

                    }
                }
            }
        }
    }
    public void move(List<Move> cand, VarIntLS[][] assign) {
        Move m = cand.get(R.nextInt(cand.size()));
        for (int j = 0 ; j < assign.length;j++) {
            assign[j][m.i].setValuePropagate(m.v);
        }
    }
    public void setModel(){
        mgr = new LocalSearchManager();
        assign = new VarIntLS[N][M];
        for (int i = 0 ; i < N ; i++){
            for (int j = 0; j < M ; j++){
                assign[i][j] = new VarIntLS(mgr);
            }
        }
        S = new ConstraintSystem(mgr);

        for (int i = 0 ; i < N ; i++) {
            for (int j = 0; j < M; j++) {
                if (L[i][j] == 0){
                    S.post(new IsEqual(assign[i][j],0));
                }
            }
        }
        nviewed = new IFunction[N];
        load = new IFunction[M];
        for (int i = 0 ; i < N ; i++){
            nviewed[i] = new ConditionalSum(assign[i],1);
            S.post(new LessOrEqual(nviewed[i],M));
            S.post(new LessOrEqual(K,nviewed[i]));
        }
        for (int j = 0 ; j < M ; j++){
            VarIntLS[] y = new VarIntLS[N];
            for (int i = 0 ; i < N ;i++){
                y[i] = assign[i][j];
            }
            load[j] = new ConditionalSum(y,1);
            S.post(new LessOrEqual(load[j],N));
            S.post(new LessOrEqual(0,load[j]));
        }
        obj = new Max(load);
        F = new FuncPlus(new FuncMult(new ConstraintViolations(S), 100), new FuncMult(obj, 1));
        mgr.close();


    }
    public void search() {
        MinMaxSelector mms = new MinMaxSelector(S);
        int it = 0;
        while (it < 10000 && S.violations() > 0) {
            VarIntLS sel_x = mms.selectMostViolatingVariable();
            int sel_v = mms.selectMostPromissingValue(sel_x);
            sel_x.setValuePropagate(sel_v);// local move
            //System.out.println("Step " + it + ", violations = " + S.violations());
            it++;
        }
        List<Move> cand = new ArrayList<Move>();
        //System.out.println("phase 2");
        while (it < 1000) {
            exploreNeighborhoodConstraintThenFunction(assign, S, F, cand);
            if (cand.size() == 0) {
                //System.out.println("local optimum");
                break;
            }
            move(cand, assign);
            //System.out.println("Step " + it + ": violations = " + S.violations() + ", number of periods = " + obj.getValue());
            it++;

        }
    }
    public void printResult(){
//        for (int j = 0 ; j < M ; j++){
//            System.out.print( "Scientist " + j + ": ");
//            for (int i = 0; i < N ; i++){
//                if (assign[i][j].getValue() == 1){
//                    System.out.print(i+" ");
//                }
//                else System.out.print("  ");
//            }
//            System.out.println();
//        }
        System.out.println("obj = "+obj.getValue());
    }
    public static void main(String[] args) {
        AEHeuristic app = new AEHeuristic();
        app.readInput();
        app.setModel();
        long t = ZonedDateTime.now().toInstant().toEpochMilli();

        app.search();
        System.out.println("Problem solved in " + (ZonedDateTime.now().toInstant().toEpochMilli()-t) + " milliseconds " );
        app.printResult();

    }
}
