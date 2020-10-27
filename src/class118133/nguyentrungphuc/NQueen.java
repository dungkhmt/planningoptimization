import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.constraints.Constraint;

public class NQueen {
    public static void main(String[] args){
        Model model = new Model("N-Queen");
        int N = 8;
        IntVar[] X = new IntVar[N];

        for(int i=0;i<N;i++){
            X[i]=model.intVar("X["+i+"]",1,N);
        }

        for(int i=0;i<N-1;i++){
            for(int j=i+1;j<N;j++){
                model.arithm(X[i],"!=",X[j]).post();
                model.arithm(X[i],"!=",X[j],"+",j-i).post();
                model.arithm(X[i],"!=",X[j],"+",i-j).post();
            }
        }

        Solution solution = model.getSolver().findSolution();

        if(solution!=null){
            System.out.println(solution.toString());
        }
//        for(int i=0;i<N;i++){
//            System.out.println();
//        }
    }
}
