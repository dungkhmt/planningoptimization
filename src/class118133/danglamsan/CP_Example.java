package class118133.danglamsan;

import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;
import com.google.ortools.sat.Literal;

public class CP_Example {
    static {
        System.loadLibrary("jniortools");
    }
    CpModel cpModel;
    IntVar[] x;
    public CP_Example() {
        this.cpModel = new CpModel();
        this.x = new IntVar[5];
    }
    public void initVar() {
        for (int i = 0; i < 5; i++) {
            this.x[i] = this.cpModel.newIntVar(1, 5, "x[" + i + "]");
//            LinearExpr.scalProd()
        }
    }
    public void makeConstraint() {
//        x2 + 3 != x1
        this.cpModel.addDifferentWithOffset(this.x[2], this.x[1], 3);
//        x3 <= x4
        this.cpModel.addLessOrEqual(LinearExpr.scalProd(new IntVar[]{this.x[3], this.x[4]}, new long[]{1, -1}), 0);
//        x2 + x3 = x0 + 1
        this.cpModel.addEquality(LinearExpr.scalProd(new IntVar[]{this.x[2], this.x[3], this.x[0]}, new long[]{1, 1, -1}), 1);
//        x4 <=3
        this.cpModel.addLessOrEqual(LinearExpr.scalProd(new IntVar[]{this.x[4]}, new long[]{1}), 3);
//        x1 + x4 = 7
        this.cpModel.addEquality(LinearExpr.scalProd(new IntVar[]{this.x[1], this.x[4]}, new long[]{1, 1}), 7);

//        b true <=> x2 = 1
        IntVar b = this.cpModel.newBoolVar("b");
        this.cpModel.addEquality(LinearExpr.scalProd(new IntVar[]{this.x[2]}, new long[]{1}), 1).onlyEnforceIf(b);
        this.cpModel.addDifferent(LinearExpr.scalProd(new IntVar[]{this.x[2]}, new long[]{1}), 1).onlyEnforceIf(b.not());
//        c true <=> x4 != 2
        IntVar c = this.cpModel.newBoolVar("c");
        this.cpModel.addEquality(LinearExpr.scalProd(new IntVar[]{this.x[4]}, new long[]{1}), 2).onlyEnforceIf(c.not());
        this.cpModel.addDifferent(LinearExpr.scalProd(new IntVar[]{this.x[4]}, new long[]{1}), 2).onlyEnforceIf(c);
//        not b or c
        this.cpModel.addBoolOr(new Literal[]{b.not(), c});
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
        CP_Example example = new CP_Example();
        example.initVar();
        example.makeConstraint();
        example.run();
    }
}
