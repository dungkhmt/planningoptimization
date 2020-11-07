package class118133.nguyenmaiphuong.java;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class Nqueen {
    int N = 10;
    LocalSearchManager mgr;
    VarIntLS[] x;
    ConstraintSystem constraintSystem;

    public void setConstraint()
    {
        mgr = new LocalSearchManager();
        x = new VarIntLS[N];
        for(int i = 0; i < N; i++)
            x[i] = new VarIntLS(mgr,0,N-1);

        constraintSystem = new ConstraintSystem(mgr);

        constraintSystem.post(new AllDifferent(x));
        IFunction[] f1 = new IFunction[N];
        for(int i = 0; i < N; ++i)
            f1[i] = new FuncPlus(x[i], i);
        constraintSystem.post(new AllDifferent(f1));

        IFunction[] f2 = new IFunction[N];
        for(int i = 0; i < N; ++i)
            f2[i] = new FuncPlus(x[i], -i);
        constraintSystem.post(new AllDifferent(f2));

        mgr.close();

    }

    public void localsearch() {
        MinMaxSelector mms = new MinMaxSelector(constraintSystem);
        int it = 0;
        while(it < 100000 && constraintSystem.violations() > 0)
        {
            VarIntLS sel_x = mms.selectMostViolatingVariable(); //Tìm con gây ra conflict max
            int sel_v = mms.selectMostPromissingValue(sel_x); //Tìm vị trí mới giảm conflict nhiều nhất
            sel_x.setValuePropagate(sel_v);// local move
            System.out.println("Step " + it + ", violations = " + constraintSystem.violations());
            it++;
        }

        for(int i = 0; i < N; ++i)
        {
            for(int j = 0 ; j < N; ++j)
            if(x[i].getValue() != j)
                System.out.print("__");
            else
                System.out.print("X");

            System.out.println();
        }
    }

    public static void main(String[] args) {
        Nqueen nqueen_heuristics = new Nqueen();
        nqueen_heuristics.setConstraint();
        nqueen_heuristics.localsearch();
    }
}
