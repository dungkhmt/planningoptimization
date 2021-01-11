package class118133.nguyenvanchuc.practice.OrtoolsProject;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class BCAOrtools {
    static {
        System.loadLibrary("jniortools");
    }

    // input data structure
    int M= 3; // number of teachers
    int N= 13; // number odf subjects
    int [][] teacherClass= {
            { 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0 },
            { 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1 },
    };

    int[] credits = { 3, 3, 4, 3, 4, 3, 3, 3, 4, 3, 3, 4, 4 }; // number of credit in each subject
    int[][] conflict={
            { 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0 },
            { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 }
    };

    public void solve(){
        MPSolver solver = new MPSolver("BCAByOrtools",
                MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
        MPVariable[][] X= new MPVariable[N][M];
        for (int i= 0; i<N; i++){
            for (int j=0; j<M; j++){
                X[i][j]= solver.makeIntVar(0, 1, "x_"+i+"_"+j);
            }
        }

        MPVariable[] load= new MPVariable[M]; // load for each teacher
        int totalCredits= 0;
        for (int i=0; i< N; i++){
            totalCredits += credits[i];
        }
        for (int i=0; i<M; i++){
            load[i]= solver.makeIntVar(0, totalCredits, "load_"+ i);
        }
        MPVariable y= solver.makeIntVar(0, totalCredits, "y"); // save the max load among teacher

        // set constraint
        for (int i=0; i<M; i++){
            for (int j=0; j<N; j++){
                if(teacherClass[i][j] == 0){ // teacher j can not teach subject i
                    MPConstraint c= solver.makeConstraint(0,0);
                    c.setCoefficient(X[j][i],1);
                }
            }
        }

        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                if(conflict[i][j] == 1){ // if subject i and j is conflict
                    for (int k=0; k<M; k++){
                        MPConstraint c= solver.makeConstraint(0,1);
                        c.setCoefficient(X[i][k],1);
                        c.setCoefficient(X[j][k],1);
                        // teacher k can not teach both of conflict subjects
                    }
                }
            }
        }

        for (int i=0; i<N; i++){
            MPConstraint c= solver.makeConstraint(1,1);
            for (int j=0; j<M; j++){
                c.setCoefficient(X[i][j], 1);
            }
        }

        for (int i=0; i<M; i++){ // set load[i] stand for workload of teacher.
            MPConstraint c= solver.makeConstraint(0,0);
            for (int j=0; j<N; j++){
                c.setCoefficient(X[j][i], credits[j]);
            }
            c.setCoefficient(load[i], -1);
        }

        for (int i = 0; i < M; i++) {
            MPConstraint c = solver.makeConstraint(0, totalCredits);
            c.setCoefficient(load[i], -1);
            c.setCoefficient(y, 1);
        }


        // set objective
        MPObjective obj= solver.objective();
        obj.setCoefficient(y, 1);
        obj.setMinimization();
        MPSolver.ResultStatus rs = solver.solve();
        if (rs != MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("cannot find optimal solution");
        } else {
            System.out.println("obj= " + obj.value());
            for (int i = 0; i < M; i++) {
                System.out.print("teacher " + i + ": ");
                for (int j = 0; j < N; j++)
                    if (X[j][i].solutionValue() == 1)
                        System.out.print(j + " ");
                System.out.println(", load = " + load[i].solutionValue());
            }
        }

    }

    public static void main(String[] args) {
        BCAOrtools app= new BCAOrtools();
        app.solve();
    }
}
