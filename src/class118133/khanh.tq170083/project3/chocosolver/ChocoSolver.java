package chocosolver;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.RealVar;

public class ChocoSolver {
    int k;
    int n;
    int[] W;
    int[] L;
    int[] C;
    int[] w;
    int[] l;
    Model model;
    Solver solver;
    IntVar target;

    IntVar[][] p;
    IntVar[] r;
    IntVar[] x;
    IntVar[] y;

    public void solve(int k, int n, int W[], int L[], int w[], int l[], int C[]) {
        this.k = k;
        this.n = n;
        this.W = W;
        this.L = L;
        this.w = w;
        this.l = l;
        this.C = C;

        model = build_model();
        model.setObjective(Model.MINIMIZE, target);
        solver = model.getSolver();
        System.out.println("Get solver done!");

        if (solver.solve()) {
            print_solution();
        }

    }

    private void print_solution() {
        Solution sol = new Solution(model);
        sol.record();
        // System.out.println(sol.toString());
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < k; ++j) 
            if (p[i][j].getValue() == 1){
                System.out.println(String.format("Bin %d đặt vào thùng %d %d %d %d", i, j, r[i].getValue(), x[i].getValue(), y[i].getValue()));

            }
        }

        System.out.println(target.getValue());

    }

    public Model build_model() {
        
        p = new IntVar[n][k];
        r = new IntVar[n];
        x = new IntVar[n];
        y = new IntVar[n];

        int maxW = 0;
        int maxL = 0;
        for (int i = 0; i < k; ++i) {
            maxW = Math.max(maxW, W[i]);
            maxL = Math.max(maxL, L[i]);
        }
        System.out.println(maxW + " " + maxL);
        int maxsize = Math.max(maxW, maxL);

        Model model = new Model();

        for (int i = 0; i < n; ++i) {
            r[i] = model.intVar(String.format("bin_%d_rotation", i), 0, 1);
            x[i] = model.intVar(String.format("x_%d_coord", i), 0, maxsize);
            y[i] = model.intVar(String.format("y_%d_coord", i), 0, maxsize);
        }

        for (int i = 0; i < n; ++i)
        for (int j = 0; j < k; ++j) {
            p[i][j] = model.intVar(String.format("bin_%d_container_%d", i, j), 0, 1);

            BoolVar condX0 = x[i].add(w[i]).le(W[j]).boolVar();
            BoolVar condY0 = y[i].add(l[i]).le(L[j]).boolVar();

            BoolVar condX1 = x[i].add(l[i]).le(W[j]).boolVar();
            BoolVar condY1 = y[i].add(w[i]).le(L[j]).boolVar();

            p[i][j].eq(0).or(r[i].eq(0).and(condX0).and(condY0)).or(r[i].eq(1).and(condX1).and(condY1)).decompose().post();
        }

        for (int i = 0; i < n; ++i) {
            IntVar z = model.intVar(String.format("abc_%d", i), 0);
            for (int j = 0; j < k; ++j) {
                z = z.add(p[i][j]).intVar();
            }
            z.eq(1).decompose().post();
            // System.out.println(z);
        }

        for (int j = 0; j < k; ++j) {
            for (int i = 0; i < n-1; ++i)
            for (int t = i+1; t < n; ++t) {
            // BoolVar no_it = model.boolVar(String.format("bin_%d_bin_%d_no_overlap", i, t), false);
            
                BoolVar r00 = r[i].eq(0).and(r[t].eq(0)).boolVar();
                BoolVar cond00_W_i = x[i].add(w[i]).le(x[t]).boolVar();
                BoolVar cond00_W_t = x[t].add(w[t]).le(x[i]).boolVar();
                BoolVar cond00_L_i = y[i].add(l[i]).le(y[t]).boolVar();
                BoolVar cond00_L_t = y[t].add(l[t]).le(y[i]).boolVar();
                BoolVar cond00 = r00.and(cond00_W_i.or(cond00_W_t).or(cond00_L_i.or(cond00_L_t))).boolVar();
                
                BoolVar r01 = r[i].eq(0).and(r[t].eq(1)).boolVar();
                BoolVar cond01_W_i = x[i].add(w[i]).le(x[t]).boolVar();
                BoolVar cond01_W_t = x[t].add(l[t]).le(x[i]).boolVar();
                BoolVar cond01_L_i = y[i].add(l[i]).le(y[t]).boolVar();
                BoolVar cond01_L_t = y[t].add(w[t]).le(y[i]).boolVar();
                BoolVar cond01 = r01.and(cond01_W_i.or(cond01_W_t).or(cond01_L_i.or(cond01_L_t))).boolVar();


                BoolVar r10 = r[i].eq(1).and(r[t].eq(0)).boolVar();
                BoolVar cond10_W_i = x[i].add(l[i]).le(x[t]).boolVar();
                BoolVar cond10_W_t = x[t].add(w[t]).le(x[i]).boolVar();
                BoolVar cond10_L_i = y[i].add(w[i]).le(y[t]).boolVar();
                BoolVar cond10_L_t = y[t].add(l[t]).le(y[i]).boolVar();
                BoolVar cond10 = r10.and(cond10_W_i.or(cond10_W_t).or(cond10_L_i.or(cond10_L_t))).boolVar();


                BoolVar r11 = r[i].eq(1).and(r[t].eq(1)).boolVar();
                BoolVar cond11_W_i = x[i].add(l[i]).le(x[t]).boolVar();
                BoolVar cond11_W_t = x[t].add(l[t]).le(x[i]).boolVar();
                BoolVar cond11_L_i = y[i].add(w[i]).le(y[t]).boolVar();
                BoolVar cond11_L_t = y[t].add(w[t]).le(y[i]).boolVar();
                BoolVar cond11 = r11.and(cond11_W_i.or(cond11_W_t).or(cond11_L_i.or(cond11_L_t))).boolVar();

                BoolVar no_overlap = cond00.or(cond01).or(cond10).or(cond11).boolVar();
                BoolVar same_truck = p[i][j].eq(1).and(p[t][j].eq(1)).boolVar();

                no_overlap.imp(same_truck).decompose().post();
                // System.out.println(j + " " + i + " " + t + " " + p[i][j].eq(1).and(p[t][j].eq(1)).and(cond00.or(cond01).or(cond10).or(cond11)).decompose());
                // System.out.println(j + " " + i + " " + t + " " + no_overlap);
            }
        }

        target = model.intVar("Target", 0);
        for (int j = 0; j < k; ++j) {
            IntVar tmp = model.intVar("tmp", 0);
            for (int i = 0; i < n; ++i) {
                tmp = tmp.add(p[i][j]).intVar();
            }

            // System.out.println(tmp);
            tmp = model.intScaleView(tmp.gt(0).intVar(), C[j]);
            // System.out.println(tmp);
            target = target.add(tmp).intVar();
            
        }

        return model;
    }
}
