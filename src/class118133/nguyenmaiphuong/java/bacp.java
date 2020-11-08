package class118133.nguyenmaiphuong.java;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.LessThan;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;
import localsearch.search.TabuSearch;

public class bacp {
    int N = 12;
    int P = 4;
    int[] credits = { 2, 1, 2, 1, 3, 2, 1, 3, 2, 3, 1, 3 };
    int alpha = 3;
    int beta = 3;
    int lamda = 5;
    int gamma = 7;
    int[][] pre = { { 1, 0 }, { 5, 8 }, { 4, 5 }, { 4, 7 }, { 3, 10 }, { 5, 11 } };

    LocalSearchManager mgr;
    VarIntLS[] x;
    ConstraintSystem constraintSystem;
    IFunction[] numberCoursesPeriod;// so mon hoc trong moi hoc ky
    IFunction[] numberCreditsPeriod;// so tin chi cac mon hoc trong moi hoc ky

    public void model() {
        mgr = new LocalSearchManager();
        x = new VarIntLS[N];
        for (int i = 0; i < N; ++i)
            x[i] = new VarIntLS(mgr, 0, P - 1);

        // Khoi tao rang buoc
        constraintSystem = new ConstraintSystem(mgr);
        for (int k = 0; k < pre.length; ++k)
            constraintSystem.post(new LessThan(x[pre[k][0]], x[pre[k][1]]));

        numberCoursesPeriod = new IFunction[P];
        numberCreditsPeriod = new IFunction[P];
        for (int i = 0; i < P; ++i) {
            numberCoursesPeriod[i] = new ConditionalSum(x, i);
            numberCreditsPeriod[i] = new ConditionalSum(x, credits, i);

            constraintSystem.post(new LessOrEqual(alpha, numberCoursesPeriod[i]));
            constraintSystem.post(new LessOrEqual(numberCoursesPeriod[i], beta));
            constraintSystem.post(new LessOrEqual(lamda, numberCreditsPeriod[i]));
            constraintSystem.post(new LessOrEqual(numberCreditsPeriod[i], gamma));
        }

        mgr.close();
    }

    public void search() {
        TabuSearch ts = new TabuSearch();
        ts.search(S, 20, 10000, 10000, 100);
        /*HillClimbing searcher = new HillClimbing();
        searcher.hillClimbing(constraintSystem, 10000);*/
    }

    public void printResult() {
        for (int j = 0; j < P; j++) {
            System.out.print("HK " + j + ": ");

            for (int i = 0; i < N; i++)
                if (x[i].getValue() == j)
                    System.out.print(i + ", ");

            System.out.println("number courses = " + numberCoursesPeriod[j].getValue() + ", number credits = " + numberCreditsPeriod[j].getValue());
        }
    }

    public static void main(String[] args) {
        bacp balancedAcademicCurriculumProblem = new bacp();
        balancedAcademicCurriculumProblem.model();
        balancedAcademicCurriculumProblem.search();
        balancedAcademicCurriculumProblem.printResult();
    }
}
