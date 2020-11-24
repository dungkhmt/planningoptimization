package class118133.truongngocgiang.localsearch;

import localsearch.constraints.basic.AND;
import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncPlus;
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class TestLocalSearch {
    public static void main(String[] args) {
        int N = 5;
        int N_Constraint = 6;
        LocalSearchManager mng = new LocalSearchManager();
        VarIntLS[] X = new VarIntLS[N];

        for (int i = 0; i < N; ++i) {
            X[i] = new VarIntLS(mng, 1,5);
        }

        ConstraintSystem S = new ConstraintSystem(mng);
        S.post(new AllDifferent(X));

        IConstraint c;
        
        c = new NotEqual(new FuncPlus(X[2], 3), X[1]);
        S.post(c);

        c = new LessOrEqual(X[3], X[4]);
        S.post(c);
        
        c = new IsEqual(new FuncPlus(X[2], X[3]), new FuncPlus(X[0], 1));
        S.post(c);

        c = new LessOrEqual(X[4], 3);
        S.post(c);

        c = new IsEqual(new FuncPlus(X[1], X[4]), 7);
        S.post(c);
        
        c = new Implicate(new IsEqual(X[2],1), new NotEqual(X[4],2));
        S.post(c);
        
        mng.close();

        MinMaxSelector mms = new MinMaxSelector(S);
        for (int i = 0; i < 1e6; ++i) {
            VarIntLS sel_x = mms.selectMostViolatingVariable();
            int sel_v = mms.selectMostPromissingValue(sel_x);
            sel_x.setValuePropagate(sel_v);
            System.out.println("Step " + i + ", violations = " + S.violations());
            if (S.violations() == 0) break;
        }

    }
    
}
