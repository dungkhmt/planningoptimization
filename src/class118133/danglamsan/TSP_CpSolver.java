package class118133.danglamsan;

import com.google.ortools.sat.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TSP_CpSolver {
    static {
        System.loadLibrary("jniortools");
    }
    CpModel cpModel;
    IntVar[] x, y;
    int N;
    class DataModelTSP {
        public int[][] distanceMatrix;
        int len;

        public DataModelTSP(String file_name) {
            File file = new File(file_name);
            try {
                Scanner sc = new Scanner(file);
                this.len = sc.nextInt();
                this.distanceMatrix = new int[len + 1][len + 1];
                for (int i = 0; i < len; i++) {
                    for (int j = 0; j < len; j++) {
                        this.distanceMatrix[i][j] = sc.nextInt();
                    }
                }
                for (int i = 0; i < len; i++) {
                    this.distanceMatrix[i][len] = this.distanceMatrix[i][0];
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (int i = 0; i <= len; i++) {
                for (int j = 0; j <= len; j++) {
                    System.out.print(distanceMatrix[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
    DataModelTSP dataModelTSP;
    public TSP_CpSolver(String filename) {
        this.dataModelTSP = new DataModelTSP(filename);
        this.N = dataModelTSP.len;
        this.cpModel = new CpModel();
        this.x = new IntVar[N];
        this.y = new IntVar[N + 1];
    }

    private void init() {
        for (int i = 0; i < N; i++) {
            this.x[i] = this.cpModel.newIntVar(1, N, "x[" + i + "]");
            this.y[i] = this.cpModel.newIntVar(0, Integer.MAX_VALUE, "y[" + i + "]");
        }
        this.y[N] = this.cpModel.newIntVar(0, Integer.MAX_VALUE, "y[" + N + "]");
    }

    private void makeConstraint() {
        this.cpModel.addAllDifferent(this.x);
        for (int i = 0; i < N; i++) {
            this.cpModel.addDifferent(LinearExpr.sum(new IntVar[]{this.x[i]}), (long) i);
        }
        this.cpModel.addEquality(LinearExpr.sum(new IntVar[]{this.y[0]}), 0);

        for (int i = 0; i < N; i++) {
            for (int j = 1; j <= N; j++) {
                if (i != j) {
                    IntVar a = cpModel.newBoolVar("a" + i + "_" + j);
                    IntVar b = cpModel.newBoolVar("b" + i + "_" + j);
                    this.cpModel.addEquality(LinearExpr.sum(new IntVar[]{this.x[i]}), j).onlyEnforceIf(a);
                    this.cpModel.addDifferent(LinearExpr.sum(new IntVar[]{this.x[i]}), j).onlyEnforceIf(a.not());
                    this.cpModel.addEquality(LinearExpr.scalProd(new IntVar[]{this.y[j], this.y[i]}, new long[]{1, -1}), dataModelTSP.distanceMatrix[i][j]).onlyEnforceIf(b);
                    this.cpModel.addImplication(a, b);
                }
            }
        }
        this.cpModel.minimize(LinearExpr.sum(new IntVar[]{this.y[N]}));
    }

    public void run() {
        this.init();
        this.makeConstraint();

        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(this.cpModel);
        if (status == CpSolverStatus.OPTIMAL) {
            for (IntVar i : this.x) {
                System.out.print(solver.value(i) + " ");
            }
            System.out.println();
            System.out.println(solver.objectiveValue());
        } else {
            System.out.println(status);
        }
    }

    public static void main(String[] args) {
        TSP_CpSolver tspCpSolver = new TSP_CpSolver("./Data/tsp-13.txt");
        tspCpSolver.run();
    }
}

