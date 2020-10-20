package class118133.danglamsan;

import com.google.ortools.sat.*;

public class Queen {
    static {
        System.loadLibrary("jniortools");
    }
    CpModel cpModel;
    IntVar[] x, d1, d2;
    int n;
    public Queen(int n) {
        this.n = n;
        this.cpModel = new CpModel();
        this.x = new IntVar[n];
        this.d1 = new IntVar[n];
        this.d2 = new IntVar[n];
    }

    public void initVar() {
        for (int i = 0; i < this.n; i++) {
            this.x[i] = this.cpModel.newIntVar(0, this.n - 1, "x[" + i + "]");
            this.d1[i] = this.cpModel.newIntVar(i, this.n - 1 + i, "d1[" + i + "]");
            this.d2[i] = this.cpModel.newIntVar(-i, this.n - 1 - i, "d2[" + i + "]");
        }
    }

    public void makeConstraint() {
        for (int i = 0; i < this.n; i++) {
            this.cpModel.addEquality(LinearExpr.scalProd(new IntVar[]{this.d1[i], this.x[i]}, new long[]{1, -1}), i);
            this.cpModel.addEquality(LinearExpr.scalProd(new IntVar[]{this.d2[i], this.x[i]}, new long[]{1, -1}), -i);
        }
        this.cpModel.addAllDifferent(this.x);
        this.cpModel.addAllDifferent(this.d1);
        this.cpModel.addAllDifferent(this.d2);
    }

    public void run() {
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(this.cpModel);
        if (status == CpSolverStatus.OPTIMAL) {
            for (IntVar i : this.x) {
                System.out.println(solver.value(i));
            }
        } else {
            System.out.println(status);
        }
    }

    public static void main(String[] args) {
        Queen queen = new Queen(8);
        queen.initVar();
        queen.makeConstraint();
        queen.run();
    }
}
