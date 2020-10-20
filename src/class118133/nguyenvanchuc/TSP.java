package class118133.nguyenvanchuc;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.File;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class TSP {
    {
        System.loadLibrary("jniortools");
    }

    int n;
    int[][] c = {
            {0, 6, 1, 1},
            {6, 0, 1, 1},
            {1, 1, 0, 5},
            {1, 1, 5, 0}
    };
    MPSolver solver;
    MPVariable[][] x;

    //    int[] b;
//    HashSet<Integer> s;
    private void get_data(String filename) {
        try {
            Scanner in = new Scanner(new File(filename));
            n = in.nextInt();
            c = new int[n][n];
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    c[i][j] = in.nextInt();
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(HashSet<Integer> s) {

//        s.clear();
//        for(int i = 0; i < n; i++) if(b[i] == 1) s.add(i);
        if (s.size() > 1 && s.size() < n) {
            MPConstraint c = solver.makeConstraint(0, s.size() - 1);
            for (int i : s)
                for (int j : s)
                    if (i != j) {
                        c.setCoefficient(x[i][j], 1);
                    }
        }
    }

    private int findNext(int i) {
        for (int j = 0; j < n; j++)
            if (i != j && x[i][j].solutionValue() == 1) {
                return j;
            }
        System.err.println("ko co duong di den i");
        return -1;
    }

    private ArrayList<HashSet<Integer>> detectMiniRoute() {
        ArrayList<HashSet<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) visited[i] = false;


        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                HashSet<Integer> h = new HashSet<>();
                visited[i] = true;
                int start = i;
                h.add(start);
                while (true) {
                    int nextnode;
                    nextnode = findNext(start);
                    if (nextnode == i) break;
                    if (nextnode >= 0 && !visited[nextnode]) {
                        visited[nextnode]= true;
                        h.add(nextnode);
                        start = nextnode;
                    }
                }
                if (h.size() < n) result.add(h);

            }

        }
        return result;
    }

    //    private void TRY(int k){// thu cac gia tri cho b[k]
//        for(int v = 0; v <= 1; v++){
//            b[k] = v;
//            if(k == n-1){
//                process();
//            }else{
//                TRY(k+1);
//            }
//        }
//    }
    public void solve() {
        System.out.println("solve start ...");
        get_data("D:\\IdeaProject\\Chuc2\\src\\tsp-100.txt");
        solver = new MPSolver("tsp_solver", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
        n = c.length;
        x = new MPVariable[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                x[i][j] = solver.makeIntVar(0, 1, "x_" + i + "_" + j);
            }
        //flow balance
        for (int i = 0; i < n; i++) {
            MPConstraint con = solver.makeConstraint(1, 1);
            for (int j = 0; j < n; j++)
                if (i != j) {
                    con.setCoefficient(x[i][j], 1);
                }
            con = solver.makeConstraint(1, 1);
            for (int j = 0; j < n; j++)
                if (i != j) {
                    con.setCoefficient(x[j][i], 1);
                }

        }

//        b= new int[n];
//        s= new HashSet<Integer>();
//        TRY(0);

        // objective
        MPObjective obj = solver.objective();
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (i != j) {
                    obj.setCoefficient(x[i][j], c[i][j]);
                }
        MPSolver.ResultStatus stat;
        int count = 1;
        while (true) {
            System.out.println("iteration :" + count);
            stat = solver.solve();
            System.out.println("optimal solution:" + obj.value());
            ArrayList<HashSet<Integer>> minitourSet = detectMiniRoute();
            System.out.println("num subtour: "+ minitourSet.size());
            if (minitourSet.size() == 0) break;
            for (HashSet<Integer> tour : minitourSet) {
                process(tour);
            }
            count++;
            System.out.println("--------------------------------------------------------------------------------------");
        }
        if (stat != MPSolver.ResultStatus.OPTIMAL) {
            System.err.println("khong co loi giai toi uu");
            return;
        }
        printRoute();
//        for (int i=0; i<n;i++)
//            for (int j=0; j<n; j++)if(i!=j && x[i][j].solutionValue() ==1){
//                System.out.println(i + "-->"+ j);
//            }
    }

    private void printRoute() {
        int[] b = new int[n];
        b[0] = 0;
        int count = 0;
        int start = 0;
        while (true) {
            int nextnode = findNext(start);
            if (nextnode <= 0) break;
            count++;
            b[count] = nextnode;
        }
        for(int i=0; i<n;i++){
            System.out.printf("%d -->",b[i]);
        }

    }

    public static void main(String[] args) {
        TSP app = new TSP();
        app.solve();
    }
}