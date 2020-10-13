package class118133.vutrungnghia.csp;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NQueen {
    private static ArrayList<ArrayList<Integer>> getCombinations(int n){
        ArrayList<ArrayList<Integer>> arr = new ArrayList<>();
        for (int i = 0; i < n - 1; ++i){
            for(int j = i+1; j <= n-1; ++j){
                ArrayList<Integer> h = new ArrayList<>();
                h.add(i);
                h.add(j);
                arr.add(h);
            }
        }
        return arr;
    }
    public static void main(String[] args) {
        int N = 8;
        Model model = new Model("NQueen");
        IntVar[] X = new IntVar[N];
        IntVar[] D1 = new IntVar[N];
        IntVar[] D2 = new IntVar[N];
        for (int i = 0; i < N; ++i) {
            X[i] = model.intVar("X[" + i + "]", 0, N-1);
            D1[i] = model.intOffsetView(X[i], i);
            D2[i] = model.intOffsetView(X[i], -i);
        }
        model.allDifferent(X).post();
        model.allDifferent(D1).post();
        model.allDifferent(D2).post();
        List<Solution> solutions = model.getSolver().findAllSolutions();

        for(Solution solution: solutions){
            System.out.println(solution);
        }
        System.out.println("No. Solutions: " + solutions.size());
    }
}
