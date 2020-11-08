package class118133.nguyenmaiphuong.java;

import localsearch.constraints.basic.NotEqual;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.TabuSearch;

public class GraphColoring {
    int N = 6;
    int K = 3;
    int[][] E = { { 0, 1 }, { 0, 4 }, { 1, 2 }, { 1, 3 }, { 1, 5 }, { 2, 5 }, { 3, 4 } };

    LocalSearchManager mgr;
    VarIntLS[] x;
    ConstraintSystem constraintSystem;

    public void model() {
        mgr = new LocalSearchManager();

        // Khoi tao bien
        x = new VarIntLS[N];
        for (int i = 0; i < N; ++i)
            x[i] = new VarIntLS(mgr, 1, K);

        // Khoi tao rang buoc
        constraintSystem = new ConstraintSystem(mgr);
        for (int i = 0; i < E.length; ++i) {
            constraintSystem.post(new NotEqual(x[E[i][0]], x[E[i][1]]));
        }

        mgr.close();
    }

    public void search() {
        TabuSearch tabuSearch = new TabuSearch();
        tabuSearch.search(constraintSystem, 20, 10000, 10000, 100);
    }

    public void printResult() {
        for (int i = 0; i < N; ++i) {
            System.out.println("x[" + i + "] = " + x[i].getValue());
        }
    }

    public static void main(String[] args) {
        GraphColoring graphColoring = new GraphColoring();
        graphColoring.model();
        graphColoring.search();
        graphColoring.printResult();
    }
}
