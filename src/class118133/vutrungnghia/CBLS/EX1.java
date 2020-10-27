package class118133.vutrungnghia.CBLS;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class EX1 {
    LocalSearchManager mgr;
    VarIntLS[] X;
    ConstraintSystem S;

    private void stateModel() {
        mgr = new LocalSearchManager();

        X = new VarIntLS[5];
        for (int i = 0; i < 5; i++) {
            X[i] = new VarIntLS(mgr, 1, 5);

        }
        S = new ConstraintSystem(mgr);

        // add constraints
        S.post(new NotEqual(new FuncPlus(X[2], 3), X[1]));
        S.post(new LessOrEqual(X[3], X[4]));
        S.post(new IsEqual(new FuncPlus(X[2], X[3]), new FuncPlus(X[0], 1)));
        S.post(new LessOrEqual(X[4], 3));
        S.post(new IsEqual(new FuncPlus(X[1], X[4]), 7));
        S.post(new Implicate(new IsEqual(X[2], 1), new NotEqual(X[4], 2)));
        mgr.close(); // build dependencies graph
        }

    private void localSearch() {
        System.out.println("Init S.violations = " + S.violations());
        ArrayList<Move> candidates = new ArrayList<Move>();
        Random r = new Random();
        for (int it = 1; it <= 100; it++) {
            candidates.clear();
            int min_delta = 10000000;
//			VarIntLS sel_x = null;
//			int sel_v = -1;

            // query neighborhood
            for (int i = 0; i < X.length; i++) {
                for (int v = X[i].getMinValue(); v <= X[i].getMaxValue(); v++) {
                    int delta = S.getAssignDelta(X[i], v);
                    if (delta < min_delta) {
                        min_delta = delta;
//						sel_x = X[i];
//						sel_v = v;
                        candidates.clear();
                        candidates.add(new Move(i, v));
                    }
                    else if (delta == min_delta) {
                        candidates.add(new Move(i, v));
                    }
                }
            }

            // local move
//			sel_x.setValuePropagate(sel_v);
            Move m = candidates.get(r.nextInt(candidates.size()));
            X[m.i].setValuePropagate(m.value);
            System.out.println("Step " + it + ", S.violations = " + S.violations());
        }
    }

    class Move{
        int i;
        int value;
        public Move(int i, int value) {
            this.i = i;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        EX1 app = new EX1();
        app.stateModel();
        app.localSearch();
    }
}