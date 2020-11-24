package class118133.vutrungnghia.csp;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class NQueenHeuristicUseLib {
    public static void main(String[] args) {
        int n = 2000;
        LocalSearchManager mgr = new LocalSearchManager();
        VarIntLS[] X = new VarIntLS[n];
        for (int i = 0; i < n; i++) {
            X[i] = new VarIntLS(mgr, 0, n - 1);
        }

        ConstraintSystem S = new ConstraintSystem(mgr);

        S.post(new AllDifferent(X));

        IFunction[] f1 = new IFunction[n];
        for (int i = 0; i < n; i++) {
            f1[i] = new FuncPlus(X[i], i);
        }
        S.post(new AllDifferent(f1));

        IFunction[] f2 = new IFunction[n];
        for (int i = 0; i < n; i++) {
            f2[i] = new FuncPlus(X[i], -i);
        }
        S.post(new AllDifferent(f2));

        mgr.close();

        MinMaxSelector mms = new MinMaxSelector(S);
        for (int it = 0; it <= 1000000; it++) {
            VarIntLS sel_x = mms.selectMostViolatingVariable();
            int sel_v = mms.selectMostPromissingValue(sel_x);
            sel_x.setValuePropagate(sel_v);

            System.out.println("Step " + it + ", violent = " + S.violations());
            if (S.violations() == 0) break;
        }
    }
}
