package class118133.vuducnhi.localsearch;

import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.Random;

public class CSPCBLS {
    class Move {
        int index;
        int value;

        public Move(int index, int value) {
            this.index = index;
            this.value = value;
        }
    }

    LocalSearchManager mgr;
    VarIntLS[] X;
    ConstraintSystem S;

    public void stateModel() {
        mgr = new LocalSearchManager();
        X = new VarIntLS[5];
        for (int i = 0; i < 5; ++i) {
            X[i] = new VarIntLS(mgr, 1, 5);
        }
        S = new ConstraintSystem(mgr);
        S.post(new NotEqual(new FuncPlus(X[2], 3), X[1]));
        S.post(new LessOrEqual(X[3], X[4]));
        S.post(new IsEqual(new FuncPlus(X[2], X[3]), new FuncPlus(X[0], 1)));
        S.post(new LessOrEqual(X[4], 3));
        S.post(new IsEqual(new FuncPlus(X[1], X[4]), 7));
        S.post(new Implicate(new IsEqual(X[2], 1), new NotEqual(X[4], 2)));
        mgr.close();
    }

    public void localSearch() {
//        for (int i = 0; i < X.length; ++i) {
//            System.out.println("X[" + i + "] = " + X[i].getValue());
//        }
//        int delta = S.getAssignDelta(X[3], 4);
//        System.out.println("delta = " + delta);
//        X[3].setValuePropagate(4);
        System.out.println("S.violations() = " + S.violations());
        Random random = new Random();
        ArrayList<Move> candidate = new ArrayList<>();

        for (int loop = 1; loop <= 100000 && S.violations() != 0; ++loop) {
            candidate.clear();
            int minDelta = 1000000000;
            for (int i = 0; i < X.length; ++i) {
                for (int v = X[i].getMinValue(); v <= X[i].getMaxValue(); ++v) {
                    int delta = S.getAssignDelta(X[i], v);
                    if (delta < minDelta) {
                        minDelta = delta;
                        candidate.clear();
                    }
                    if (delta == minDelta) {
                        candidate.add(new Move(i, v));
                    }
                }
            }

            Move chosenOne = candidate.get(random.nextInt(candidate.size()));
            X[chosenOne.index].setValuePropagate(chosenOne.value);
            System.out.println("Step " + loop + ", S.violations = " + S.violations());
        }
    }

    public static void main(String[] args) {
        CSPCBLS app = new CSPCBLS();
        app.stateModel();
        app.localSearch();
    }
}
