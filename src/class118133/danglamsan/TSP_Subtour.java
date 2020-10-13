package class118133.danglamsan;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;

import java.util.HashSet;

public class TSP_Subtour extends class118133.danglamsan.TSP {
//    DataModelTSP dataModelTSP = new DataModelTSP("./Data/tsp-50.txt");
    int N;
    public TSP_Subtour(String fileName) {
        super(fileName);
        N = dataModelTSP.distanceMatrix.length;
    }
    private boolean check_optimal(int[] visited) {
        for (int i = 0; i < N; i++) {
            if (visited[i] != 1) {
                return false;
            }
        }
        return true;
    }
    private int max_arr(int[] visited) {
        int m = 0;
        for (int i = 0; i < N; i++) {
            if (visited[i] > m) {
                m = visited[i];
            }
        }
        return m;
    }
    private int[][] extract_sub_tour(int[] visited) {
        int max = max_arr(visited);
        int[][] b = new int[max][N];
//        int cnt = 1;
        for (int i = 1; i <= max; i++) {
            for (int j = 0; j < N; j++) {
                if (visited[j] == i) {
                    b[i - 1][j] = 1;
                } else {
                    b[i - 1][j] = 0;
                }
                System.out.print(b[i - 1][j] + " ");
            }
            System.out.println();
        }

        return b;
    }
    private boolean check_condition() {
        /*
        Find Cycle by BFS
         */
        int[] visited = new int[N];
        int cnt = 1;
        for (int i = 0; i < N; i++) {
            if (visited[i] == 0) {
                visited[i] = cnt;
                int ver = i;
                do {
                    ver = findNext(ver);
                    if (visited[ver] == 0)
                        visited[ver] = cnt;
                    else
                        break;
                } while (true);
                cnt += 1;
            }
        }
//        for (int i = 0; i < N; i++)
//            System.out.print(visited[i] + " ");
        if (check_optimal(visited)) {
            return true;
        } else {
            int[][] b = extract_sub_tour(visited);
            int max = b.length;
            System.out.println("Number of sub tour: " + max);
            HashSet<Integer> S = new HashSet<Integer>();
            for (int i = 0; i < max; i++) {
                S.clear();
                for (int j = 0; j < N; j++) {
                    if (b[i][j] == 1) {
                        S.add(j);
                    }
                }
                MPConstraint c = this.solver.makeConstraint(0, S.size() - 1);
                for (int k : S) {
                    for (int h : S) {
                        if (k != h) {
                            c.setCoefficient(this.x[k][h], 1);
                        }
                    }
                }
            }
            return false;
        }
    }
    @Override
    public void solve() {
        System.out.println("Solver is starting ...");
        int N = dataModelTSP.distanceMatrix.length;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i != j)
                    this.x[i][j] = this.solver.makeIntVar(0, 1, "X[" + i + "," + j + "]");
            }
        }
        // flow balance
        for (int i = 0; i < N; i++) {
            MPConstraint c = solver.makeConstraint(1, 1);
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    c.setCoefficient(this.x[i][j], 1);
                }
            }
            c = solver.makeConstraint(1, 1);
            for (int j = 0; j < N; j++) {
                if (i != j) {
                    c.setCoefficient(this.x[j][i], 1);
                }
            }
        }
        // objective

        MPObjective obj = solver.objective();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                obj.setCoefficient(this.x[i][j], dataModelTSP.distanceMatrix[i][j]);
            }
        }


        long sol = 0;
        long chk = 0;
        int counter = 0;

        while (true) {
            counter += 1;
            long a = System.currentTimeMillis();
            MPSolver.ResultStatus res_stat = solver.solve();
            long b = System.currentTimeMillis();
            sol += (b - a);

            System.out.println("STEP: " + counter);
            System.out.println("No.constraints: " + solver.numConstraints());
            System.out.println("Optimal solution: " + solver.objective().value());

            if (res_stat != MPSolver.ResultStatus.OPTIMAL) {
                System.err.println("The problem does not have optimal solution");
                return;
            }
            a = System.currentTimeMillis();
            boolean check = check_condition();
            b = System.currentTimeMillis();
            chk += (b - a);
            if (check) {
                super.printTour();
                System.out.println("Ok -- Optimal Objective:= " + solver.objective().value());
                System.out.println("Solve: " + sol);
                System.out.println("Check: " + chk);
                return;
            }
        }

    }
    public static void main(String[] args) {
        long a = System.currentTimeMillis();
        TSP_Subtour tsp = new TSP_Subtour("./data/TSP/tsp-50.txt");

        tsp.solve();
        long b = System.currentTimeMillis();
        System.out.println(b - a);
    }
}
