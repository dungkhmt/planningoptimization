package class118133.vuducnhi.localsearch;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class Example {
    public static void main(String[] args) {
        int N = 1000;
        LocalSearchManager manager = new LocalSearchManager();
        VarIntLS[] X = new VarIntLS[N];
        for (int i = 0; i < N; ++i) {
            X[i] = new VarIntLS(manager, 0, N - 1);
        }
        ConstraintSystem S = new ConstraintSystem(manager);
        S.post(new AllDifferent(X));

        IFunction[] f1 = new IFunction[N];
        for (int i = 0; i < N; ++i) {
            f1[i] = new FuncPlus(X[i], i);
        }
        S.post(new AllDifferent(f1));

        IFunction[] f2 = new IFunction[N];
        for (int i = 0; i < N; ++i) {
            f2[i] = new FuncPlus(X[i], -i);
        }
        S.post(new AllDifferent(f2));
        manager.close();

        MinMaxSelector mms = new MinMaxSelector(S);
        for (int it = 0; it < 1000000; ++it) {
            VarIntLS sel_x = mms.selectMostViolatingVariable();
            int sel_v = mms.selectMostPromissingValue(sel_x);
            sel_x.setValuePropagate(sel_v);
            System.out.println("Step " + it + ", violations = " + S.violations());
            if (S.violations() == 0) break;
        }
    }
}
