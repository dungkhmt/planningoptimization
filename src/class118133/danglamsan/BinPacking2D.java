package class118133.danglamsan;

import com.google.ortools.sat.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Long.max;

public class BinPacking2D {
    static {
        System.loadLibrary("jniortools");
    }
    int W, L, N;

    IntVar[] binVarX;
    IntVar[] binVarY;
    IntVar[] rot;
    CpModel cpModel;

    class Size {
        int w, l;
        public Size(int w, int l) {
            this.w = w;
            this.l = l;
        }
    }
    List<Size> bins = new ArrayList<>();

    public BinPacking2D(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
            this.W = sc.nextInt();
            this.L = sc.nextInt();

            int tmp = sc.nextInt();
            while (tmp != -1) {
                int l = sc.nextInt();
                Size s = new Size(tmp, l);
                this.bins.add(s);
                tmp = sc.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.binVarX = new IntVar[this.bins.size()];
        this.binVarY = new IntVar[this.bins.size()];
        this.rot = new IntVar[this.bins.size()];
        this.N = this.bins.size();
        this.initModel();
    }

    private void initModel() {
        this.cpModel = new CpModel();
        for (int i = 0; i < N; i++) {
            this.binVarX[i] = this.cpModel.newIntVar(0, W - 1, "X[" + i + "]");
            this.binVarY[i] = this.cpModel.newIntVar(0, L - 1, "Y[" + i + "]");
            this.rot[i] = this.cpModel.newIntVar(0, 1, "rot[" + i + "]");
        }
    }

    private void makeConstraint() {
        IntVar[] w_tmp = new IntVar[N];
        IntVar[] l_tmp = new IntVar[N];

        for (int i = 0; i < N; i++) {
            w_tmp[i] = this.cpModel.newIntVar(0, max(W, L), "w_tmp" + i);
            l_tmp[i] = this.cpModel.newIntVar(0, max(W, L), "l_tmp" + i);
            IntVar negr = cpModel.newIntVar(0, 1, "negr" + i);
            this.cpModel.addEquality(LinearExpr.scalProd(new IntVar[]{negr, this.rot[i]}, new long[]{1, 1}), 1);
            IntVar tempWW = cpModel.newIntVar(0, W, "tempWW" + i);
            IntVar tempWL = cpModel.newIntVar(0, L, "tempWL" + i);
            IntVar tempLW = cpModel.newIntVar(0, W, "tempLW" + i);
            IntVar tempLL = cpModel.newIntVar(0, L, "tempLL" + i);
            cpModel.addProductEquality(tempWW, new IntVar[] {cpModel.newConstant(this.bins.get(i).w), rot[i]});
            cpModel.addProductEquality(tempWL, new IntVar[] {cpModel.newConstant(this.bins.get(i).l), negr});
            cpModel.addProductEquality(tempLW, new IntVar[] {cpModel.newConstant(this.bins.get(i).w), negr});
            cpModel.addProductEquality(tempLL, new IntVar[] {cpModel.newConstant(this.bins.get(i).l), rot[i]});
            cpModel.addEquality(w_tmp[i], LinearExpr.sum(new IntVar[] {tempWW, tempWL}));
            cpModel.addEquality(l_tmp[i], LinearExpr.sum(new IntVar[] {tempLW, tempLL}));
        }

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                IntVar left = cpModel.newBoolVar("left" + i + "," + j);
                cpModel.addLessOrEqual(LinearExpr.sum(new IntVar[] {binVarX[j], w_tmp[j]}), binVarX[i]).onlyEnforceIf(left);
                IntVar right = cpModel.newBoolVar("right" + i + "," + j);
                cpModel.addLessOrEqual(LinearExpr.sum(new IntVar[] {binVarX[i], w_tmp[i]}), binVarX[j]).onlyEnforceIf(right);
                IntVar up = cpModel.newBoolVar("up" + i + "," + j);
                cpModel.addLessOrEqual(LinearExpr.sum(new IntVar[] {binVarY[j], l_tmp[j]}), binVarY[i]).onlyEnforceIf(up);
                IntVar down = cpModel.newBoolVar("down" + i + "," + j);
                cpModel.addLessOrEqual(LinearExpr.sum(new IntVar[] {binVarY[i], l_tmp[i]}), binVarY[j]).onlyEnforceIf(down);
                cpModel.addBoolOr(new Literal[] {left, right, up, down});
            }
        }
        for (int i = 0; i < N; i++) {
            cpModel.addLessOrEqual(LinearExpr.sum(new IntVar[] {binVarX[i], w_tmp[i]}), W);
            cpModel.addLessOrEqual(LinearExpr.sum(new IntVar[] {binVarY[i], l_tmp[i]}), L);
        }

    }

    public void run() {
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(this.cpModel);
        int[][] res = new int[W][L];
        if (status == CpSolverStatus.OPTIMAL) {
            for (int i = 0; i < N; i++) {
                System.out.println("Bin[" + i + "] = " + solver.value(this.binVarX[i]) + " " + solver.value(this.binVarY[i]) + " " + solver.value(rot[i]));
//                for (int k = 0; k < this.bins.get(i).w; k++) {
//                    for (int h = 0; h < this.bins.get(i).l; h++) {
//                        res[(int) solver.value(this.binVarX[i]) + k][(int) solver.value(this.binVarY[i]) + h] = i;
//                    }
//                }
            }
        } else {
            System.out.println(status);
        }
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < L; j++) {
                System.out.print(res[i][j]);
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        BinPacking2D binPacking2D = new BinPacking2D("Data/binpacking2d.txt");
        binPacking2D.makeConstraint();
//        binPacking2D.run();
    }
}
