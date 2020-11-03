package class118133.truongngocgiang.tbl;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;
import localsearch.selectors.MinMaxSelector;

public class Example {
    public static void main(String[] args) {
        int N_Constraint = 11;
        int N_Subject = 12;
        int M = 4;
        
        LocalSearchManager mng = new LocalSearchManager();
        VarIntLS[] X = new VarIntLS[N_Subject];
        int[] W = new int[] { 2,1,2,1,3,2,1,3,2,3,1,3};
        int[] W1 = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        ConstraintSystem S = new ConstraintSystem(mng);

        
        for (int it = 0; it < N_Subject; it++) {
            X[it] = new VarIntLS(mng, 1, M);
        }

        for (int it = 0; it < M; ++it) {
            IFunction f = new ConditionalSum(X, W, it+1);
            IFunction f_n_object = new ConditionalSum(X, W1, it+1);
            
            
            S.post(new LessOrEqual(f, 7));
            S.post(new LessOrEqual(5, f));

            S.post(new LessOrEqual(f_n_object, 3));
            S.post(new LessOrEqual(3, f_n_object));
        }

        


        int[] x1 = new int[] {2, 6, 5, 5, 4, 6, 2, 3, 5, 8, 4};
        int[] x2 = new int[] {1, 9, 6, 8, 11, 12, 7, 10, 7, 11, 12};

        for (int i = 0; i < N_Subject; ++i)
        for (int j = 0; j < N_Subject; ++j) {
            for (int it = 0; it < N_Constraint; ++it)
            if (x1[it] == i+1 && x2[it] == j+1) {
                S.post(new NotEqual(X[i], X[j]));
            }
        }

        mng.close();
        
        HillClimbing hc = new HillClimbing();
        hc.hillClimbing(S, 1000000);

        // MinMaxSelector mms = new MinMaxSelector(S);
        // for (int i = 0; i < 1e4; ++i) {
        //     VarIntLS sel_x = mms.selectMostViolatingVariable();
        //     int sel_v = mms.selectMostPromissingValue(sel_x);
        //     sel_x.setValuePropagate(sel_v);
        //     System.out.println("Step " + i + ", violations = " + S.violations());
        //     if (S.violations() == 0) break;
        // }

        for (int i = 0; i < N_Subject; ++i)
        System.out.println((i+1) + " " + X[i].getValue());


    }
}
