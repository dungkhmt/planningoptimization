package Project;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class AssignReviewer {
    static {
        System.loadLibrary("jniortools");
    }
    int N; //paper 0..N-1
    int M; //Reviewer  0..M-1
    int K; //the minium number of reviewers to review each paper
    int[][] L ; //L[i][j] = 1 when reviewer j can review paper i

    public void readInput(){
        for (int i= 0 ; i < N ; i ++){
            Arrays.fill(L[i],0);
        }

        File fi = new File("src/Project/data500.txt");
        try {
            Scanner scan = new Scanner(fi);
            N = scan.nextInt();
            M = scan.nextInt();
            L = new int[N][M];
            K = scan.nextInt();
            String line = scan.nextLine();
            int i = 0;
            while (scan.hasNext()){
                line = scan.nextLine();
                String[] temp =  line.split(" ");
                for (String s: temp){
                    int j = Integer.parseInt(s);
                    L[i][j-1] = 1;
                }
                i++;
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
    public void solver(){
        MPSolver solver = new MPSolver("BCA", MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
        //paper i is reviewed by j
        MPVariable[][] assign = new MPVariable[N][M];
        for(int i = 0 ; i < N ; i ++){
            for (int j = 0 ; j < M ; j++){
                assign[i][j] = solver.makeIntVar(0,1, "");
            }
        }
        //number of time the paper i was evaluated
        MPVariable[] nViewed = new MPVariable[N];
        for (int i = 0 ; i < N ; i++){
            nViewed[i] = solver.makeIntVar(K,M,"");
        }
        //number of times the scientist rate the paper
        MPVariable[] load = new MPVariable[M];
        for (int j = 0 ; j < M ; j++){
            load[j] = solver.makeIntVar(0,N,"");
        }
        MPVariable y = solver.makeIntVar(0,N,"y");

        //Scientist are only allowed to review paper in their major
        for (int i= 0 ; i < N; i++){
            for (int j = 0 ; j < M ; j++){
                if (L[i][j] == 0){
                    MPConstraint constraint = solver.makeConstraint(0,0);
                    constraint.setCoefficient(assign[i][j],1);
                }
            }
        }
        //constraint: number of time the paper i was evaluated
        for (int i = 0 ; i < N ; i++){
            MPConstraint constraint = solver.makeConstraint(0,0);
            for (int j =0 ; j < M; j++){
                constraint.setCoefficient(assign[i][j], 1);
            }
            constraint.setCoefficient(nViewed[i],-1);
        }
        //constraint: number of times the scientist rate the paper
        for (int j = 0 ; j < M ; j++){
            MPConstraint constraint = solver.makeConstraint(0,0);
            for (int i = 0 ; i < N ; i++){
                constraint.setCoefficient(assign[i][j], 1);
            }
            constraint.setCoefficient(load[j], -1);
        }
        for (int j = 0 ; j < M ; j++){
            MPConstraint constraint = solver.makeConstraint(0,N);
            constraint.setCoefficient(y,1);
            constraint.setCoefficient(load[j],-1);
        }
        MPObjective obj = solver.objective();
        obj.setCoefficient(y, 1);
        obj.setMinimization();
        //solver.setTimeLimit(100000);
        final MPSolver.ResultStatus resultStatus = solver.solve();

        if (resultStatus == MPSolver.ResultStatus.NOT_SOLVED){
            System.err.println("The Time it takes to slove this problem is too long");
        }
        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
            System.err.println("The problem does not have an optimal solution!");
            return;
        }
        System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");
        // The objective value of the solution.
        System.out.println("Optimal objective value = " +  (int) obj.value());
//        for (int j = 0 ; j < M ; j++){
//            System.out.print(j + " : ");
//            for (int i = 0; i < N ; i++){
//                if (assign[i][j].solutionValue() == 1){
//                    System.out.print(i+" ");
//                }
//                else System.out.print("  ");
//            }
//            System.out.println();
//        }
    }
    public static void main(String[] args) {
        AssignReviewer model = new AssignReviewer();
        model.readInput();
        model.solver();
    }

}
