package class118133.phamminhkhiem;

import core.VarInt;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncMult;
import localsearch.functions.basic.FuncVarConst;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.functions.max_min.Max;
import localsearch.functions.max_min.Min;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;
import org.jgrapht.alg.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BCTA {
    static int N = 13 ;// Số lớp
    static int M = 3 ; // Số giáo viên
    static int K = 11 ; // Số cặp lớp trùng lặp
    static List<Pair<Integer, Integer>> class_conflic = new ArrayList<>();
    static int[] w = new int[N] ;// số tiết
    static int[][] map = new int[M][N] ;
    public static void initValue(){
        w = new int[]{3,3,4,3,4,3,3,3,4,3,3,4,4} ;
        map = new int[][] {
                {0,2,3,4,8,10},
                {0,1,3,5,6,7,8},
                {1,2,3,7,9,11,12}
        };
        class_conflic.add(new Pair<>(0,0));
        class_conflic.add(new Pair<>(0,4));
        class_conflic.add(new Pair<>(0,8));
        class_conflic.add(new Pair<>(1,4));
        class_conflic.add(new Pair<>(1,10));
        class_conflic.add(new Pair<>(3,7));
        class_conflic.add(new Pair<>(3,9));
        class_conflic.add(new Pair<>(5,11));
        class_conflic.add(new Pair<>(5,12));
        class_conflic.add(new Pair<>(6,8));
        class_conflic.add(new Pair<>(6,12));
    }
    public static void climbingSearch(){

    }
    public static void main(String[] args) {
        initValue();
        LocalSearchManager mgr = new LocalSearchManager();
        VarIntLS[] X = new VarIntLS[N] ;
        for (int i = 0 ; i < N ; i ++){
            X[i] = new VarIntLS(mgr,0,M-1);
        }
        ConstraintSystem S = new ConstraintSystem(mgr);
        for (int i = 0 ; i < class_conflic.size() ; i++){
            System.out.println(class_conflic.get(i).getFirst() + " " + class_conflic.get(i).getSecond());
            S.post(new NotEqual(X[class_conflic.get(i).getFirst()] , X[class_conflic.get(i).getSecond()]));
        }
        IFunction[] sf = new IFunction[M] ;
        for (int i = 0 ; i < M ; i ++) {
            sf[i] = new ConditionalSum(X, w, i);
//            sf[i] = new FuncMult(sf[i], w[i]) ;
        }
        for (int i =0 ; i< M ; i++){
            S.post(new LessOrEqual(sf[i], 15));
            S.post(new LessOrEqual(14, sf[i]));
        }

        S.close();
        mgr.close();
        //
        ArrayList<Pair<Integer, Integer>> cand = new ArrayList<>();
        Random rd = new Random();
        int it = 0 ;
        while (it < 1000000 ){
            //explore
            cand.clear();
            int minDelta = Integer.MAX_VALUE;
            for(int i = 0; i < X.length; i++){
                for(int v = X[i].getMinValue(); v <= X[i].getMaxValue(); v++){
                    int d = S.getAssignDelta(X[i], v);
                    if(d < minDelta){
                        cand.clear();
                        cand.add(new Pair<>(i,v));
                        minDelta = d;
                    }else if(d == minDelta){
                        cand.add(new Pair<>(i,v));
                    }
                }
            }


            //
            Pair<Integer, Integer> m =
                    cand.get(rd.nextInt(cand.size()));
            X[m.getFirst()].setValuePropagate(m.getSecond());
            it++;
            System.out.println("S.violations : " + S.violations());
            for ( int i =  0 ; i < N ; i ++){
                System.out.print(X[i].getValue() + "--");
            }
            System.out.println();
        }
    }
}
