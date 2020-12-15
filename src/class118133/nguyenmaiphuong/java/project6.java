package class118133.nguyenmaiphuong.java;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.functions.basic.FuncMult;
import localsearch.functions.sum.Sum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.TabuSearch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;

public class project6 {
    /*******************
     * N - số sản phẩm 
     * A - tổng diện tích thửa ruộng 
     * C - chi phí hiện có 
     * c - chi phí để sx 1 đơn vị sp
     * a - diện tích cần cho 1 đơn vị sp
     * f - lợi nhuận từ 1 đv sp 
     * m - số sp tối thiếu khi quyết định sx sp đó 
     *
     * x - so don vi san pham neu trong 
     * y - 0/1 trong hay khong
     */
    int N, A, C, F;
    int[] c;
    int[] a;
    int[] f;
    int[] m;

    LocalSearchManager mgr;
    VarIntLS[] x;
    VarIntLS[] y;
    ConstraintSystem constraintSystem;
    IFunction[] chosen;
    IFunction[] chiPhi;
    IFunction[] dienTich;
    IFunction[] loiIch;
    IFunction tongChiPhi;
    IFunction tongDienTich;
    IFunction tongLoiIch;

    public void doc() {
        try {
            FileReader fileReader = new FileReader(
                    "/home/phuongnguyen/git/planningoptimization/src/class118133/nguyenmaiphuong/java/data/test0.txt");
            BufferedReader br = new BufferedReader(fileReader);
            this.N = Integer.parseInt(br.readLine());
            this.A = Integer.parseInt(br.readLine());
            this.C = Integer.parseInt(br.readLine());

            c = new int[this.N];
            a = new int[this.N];
            f = new int[this.N];
            m = new int[this.N];

            String cc[] = new String[this.N];
            cc = br.readLine().split(" ");
            for (int i = 0; i < this.N; ++i)
                this.c[i] = Integer.parseInt(cc[i]);

            String aa[] = new String[this.N];
            aa = br.readLine().split(" ");
            for (int i = 0; i < this.N; ++i)
                this.a[i] = Integer.parseInt(aa[i]);

            String ff[] = new String[this.N];
            ff = br.readLine().split(" ");
            for (int i = 0; i < this.N; ++i)
                this.f[i] = Integer.parseInt(ff[i]);

            String mm[] = new String[this.N];
            mm = br.readLine().split(" ");
            for (int i = 0; i < this.N; ++i)
                this.m[i] = Integer.parseInt(mm[i]);

            br.close();
        } catch (IOException ex) {

        }
    }

    public void model() {
        mgr = new LocalSearchManager();
        x = new VarIntLS[N];
        y = new VarIntLS[N];
        for (int i = 0; i < this.N; ++i) {
            x[i] = new VarIntLS(mgr, m[i], 100);
            y[i] = new VarIntLS(mgr, 0, 1);
        }

        constraintSystem = new ConstraintSystem(mgr);
        chosen = new IFunction[N];
        for (int i = 0; i < N; ++i)
            chosen[i] = new FuncMult(x[i], y[i]);

        chiPhi = new IFunction[N];
        for(int i= 0; i<N; ++i)
            chiPhi[i] = new FuncMult(chosen[i], c[i]);
        tongChiPhi = new Sum(chiPhi);
        constraintSystem.post(new LessOrEqual(tongChiPhi, C));

        dienTich = new IFunction[N];
        for(int i= 0; i<N; ++i)
            dienTich[i] = new FuncMult(chosen[i], a[i]);
        tongDienTich = new Sum(dienTich);
        constraintSystem.post(new LessOrEqual(tongDienTich, A));

        loiIch = new IFunction[N];
        for(int i= 0; i<N; ++i)
            loiIch[i] = new FuncMult(chosen[i], f[i]);
        tongLoiIch = new Sum(loiIch);
        constraintSystem.post(new LessOrEqual(F, tongLoiIch));
        
        mgr.close();
    }

    public void solve() {
        long st = System.currentTimeMillis();

        TabuSearch tabuSearch = new TabuSearch();
        
        this.model();
            
        tabuSearch.search(constraintSystem, 20, 10000, 10000, 100);
        if(tongLoiIch.getValue() >= F && tongChiPhi.getValue() <= C && tongDienTich.getValue() <= A)
            minC = F;
        else maxC = F;

        System.out.println(System.currentTimeMillis() - st);
    }

    public void printResult() {
        System.out.println("tong F: " +tongLoiIch.getValue());
        for(int i=0; i<N; i++)
            System.out.print(chosen[i].getValue() + " ");
        System.out.println();

        System.out.println("tong C: " + tongChiPhi.getValue());
        System.out.println("tong S: " + tongDienTich.getValue());
    }

    public static void main(String[] args) {
        project6 project = new project6();
        project.doc();
        project.solve();
        project.printResult();
    }
}
