package class118133.tranvandao;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.Scanner;

public class BinPacking {
    public static void main(String[] args) {
        int width,length;
        int[] w = new int[200];
        int[] l = new int[200];
        Scanner sc = new Scanner(System.in);
        width = sc.nextInt();
        length = sc.nextInt();
        int n = 0;
        while(true){
            w[n] = sc.nextInt();
            if (w[n] == -1) break;
            l[n] = sc.nextInt();
            n++;
        }

        sc.close();
        Model model = new Model("BinPacking_V1");
        IntVar[] x = model.intVarArray("x" , n ,0,width);
        IntVar[] y = model.intVarArray("y",n,0,length);
        BoolVar[] o = model.boolVarArray("o",n);

        
        for (int i = 0 ; i < n ; i++){
            o[i].eq(0).imp(x[i].add(w[i]).le(width).and(y[i].add(l[i]).le(length))).post();
            o[i].eq(1).imp(x[i].add(l[i]).le(width).and(y[i].add(w[i]).le(length))).post();
        }
   
        for (int i =0; i < n-1 ; i++){
            for(int j=i+1; j<n ; j++){
                (o[i].eq(0).and(o[j].eq(0))).imp(x[i].add(w[i]).le(x[j]).or(x[j].add(w[j]).le(x[i])).or(y[i].add(l[i]).le(y[j]).or(y[j].add(l[j]).le(y[i])))).post();
                (o[i].eq(0).and(o[j].eq(1))).imp(x[i].add(w[i]).le(x[j]).or(x[j].add(l[j]).le(x[i])).or(y[i].add(l[i]).le(y[j]).or(y[j].add(w[j]).le(y[i])))).post();
                (o[i].eq(1).and(o[j].eq(0))).imp(x[i].add(l[i]).le(x[j]).or(x[j].add(w[j]).le(x[i])).or(y[i].add(w[i]).le(y[j]).or(y[j].add(l[j]).le(y[i])))).post();
                (o[i].eq(1).and(o[j].eq(1))).imp(x[i].add(l[i]).le(x[j]).or(x[j].add(l[j]).le(x[i])).or(y[i].add(w[i]).le(y[j]).or(y[j].add(w[j]).le(y[i])))).post();
            }
        }
        Solver solver = model.getSolver();
        Solution solution = solver.findSolution();
//        System.out.println(solution.toString());
        System.out.println("Solution");
        int [][] pack=new int[200][200];
        for (int i=0;i<n;i++) {
        	System.out.println("Box"+i+":"+x[i].getValue()+" "+y[i].getValue()+" "+o[i].getValue());
//        	for (int dx=x[i];dx<x[i]+w[i];dx++)
//        		for (int dy=y[i];dy<y[i]+l[])
        }

    }
}
