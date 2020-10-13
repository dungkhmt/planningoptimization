package class118133.vuducnhi.csp;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.util.Scanner;

public class NQueen {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("N = ? ");
        int N = sc.nextInt();
        Model model = new Model(N + "-queens problem");
        IntVar[] vars = new IntVar[N];
        for(int q = 0; q < N; q++){
            vars[q] = model.intVar("Q_"+q, 1, N);
        }
        for(int i  = 0; i < N - 1; i++){
            for(int j = i + 1; j < N; j++){
                model.arithm(vars[i], "!=",vars[j]).post();
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }

        Solution solution = model.getSolver().findSolution();
        if (solution != null) {
            System.out.println(solution.toString());
        }
    }
}
