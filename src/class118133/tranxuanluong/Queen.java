import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.util.Random;

public class CSP {

    public void queenSolver(){
        int n = 8;
        Model model = new Model(n + "-queens problem");
        IntVar[] vars = new IntVar[n];
        for(int q = 0; q < n; q++){
            vars[q] = model.intVar("Q_"+q, 1, n);
        }
        model.allDifferent(vars).post();
        for(int i  = 0; i < n-1; i++){
            for(int j = i + 1; j < n; j++){
                model.arithm(vars[i], "!=", vars[j], "-", j - i).post();
                model.arithm(vars[i], "!=", vars[j], "+", j - i).post();
            }
        }
        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
        }
    }
    public void nQueenSolve(int n){
        Model model = new Model("arrange n queens that no queen can attack another");
        IntVar []vars = new IntVar[n];
        for (int q = 0; q < n; q++){
            vars[q] = model.intVar("Queen"+q,1,n);
        }
        model.allDifferent(vars).post();
        for(int i = 0; i < n-1; i++){
            for(int j = i+1; j < n; j++){
                model.arithm(vars[i],"!=",vars[j],"-",i-j).post();
                model.arithm(vars[i],"!=",vars[j],"-",j-i).post();
            }
        }
        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
        }

    }
    public void nQueenAlldiff(int n){
        Model model = new Model("arrange n queens that no queen can attack another");
        IntVar []vars = new IntVar[n];
        for (int q = 0; q < n; q++){
            vars[q] = model.intVar("Queen"+q,1,n);
        }
        model.allDifferent(vars).post();
        IntVar []temp1 = new IntVar[n];
        IntVar []temp2 = new IntVar[n];
        for (int i = 0 ; i < n; i++){
            temp1[i] = model.intOffsetView(vars[i],i);
        }
        for (int i = 0 ; i < n; i++){
            temp2[i] = model.intOffsetView(vars[i],-i);
        }
        model.allDifferent(temp1).post();
        model.allDifferent(temp2).post();
        Solution solution = model.getSolver().findSolution();
        if(solution != null){
            System.out.println(solution.toString());
        }
    }
    
    public static void main(String[] args) {
        CSP csp = new CSP();
        csp.nQueenAlldiff(8);
    }
}

