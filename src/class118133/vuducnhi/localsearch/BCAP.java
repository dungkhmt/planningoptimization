package class118133.vuducnhi.localsearch;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.Random;

public class BCAP {
    class Move {
        int index;
        int value;

        public Move(int index, int value) {
            this.index = index;
            this.value = value;
        }
    }

    class CourseConflict {
        int first;
        int second;

        public CourseConflict(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    LocalSearchManager mgr;
    int numCourse;
    int numTeacher;
    int numConflict;
    int[] lessons;
    VarIntLS[] X;
    CourseConflict[] CC;
    ConstraintSystem S;

    public void initializeData() {
        numCourse = 13;
        numTeacher = 3;
        numConflict = 11;
        lessons = new int[]{3, 3, 4, 3, 4, 3, 3, 3, 4, 3, 3, 4, 4};
        X = new VarIntLS[numCourse];
        CC = new CourseConflict[numConflict];
        int[] CX = new int[]{0, 0, 0, 1, 1, 3, 3, 5, 5, 6, 6};
        int[] CY = new int[]{2, 4, 8, 4, 10, 7, 9, 11, 12, 8, 12};
        for (int i = 0; i < numConflict; ++i) {
            CC[i] = new CourseConflict(CX[i], CY[i]);
        }
    }

    public void stateModel() {
        mgr = new LocalSearchManager();
        S = new ConstraintSystem(mgr);
        for (int i = 0; i < numCourse; ++i) {
            X[i] = new VarIntLS(mgr, 0, numCourse - 1);
        }

        for (int i = 0; i < numConflict; ++i) {
            S.post(new NotEqual(X[CC[i].first], X[CC[i].second]));
        }

        for (int i = 0; i < numTeacher; ++i) {
            ConditionalSum load = new ConditionalSum(X, lessons, i);
            S.post(new LessOrEqual(14, load));
            S.post(new LessOrEqual(load, 15));
        }

        mgr.close();
    }

    public void localSearch() {
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
        for (int i = 0; i < X.length; ++i) {
            System.out.println("X[" + i + "].getValue() = " + X[i].getValue());
        }
    }


    public static void main(String[] args) {
        BCAP app = new BCAP();
        app.initializeData();
        app.stateModel();
        app.localSearch();
    }
}
