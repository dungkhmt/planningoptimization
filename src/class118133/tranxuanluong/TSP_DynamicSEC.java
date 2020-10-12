import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class TSP_DynamicSEC {
    static {
        System.loadLibrary("jniortools");
    }
    MPVariable[][] x; ;
    int cost[][];
    int n  = -1;
    List<HashSet> listSubtour = new ArrayList<>();
    MPSolver solver = MPSolver.createSolver("TSP-Linear Programming ", String.valueOf(MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING));
    int countSubtour = 2;
    MPObjective obj;
    public void solve() throws Exception{
        System.out.println("Start solving ... ");
        getDataFromFile();
        x = new MPVariable[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                x[i][j] = solver.makeIntVar(0, 1, String.valueOf(i*n+j));
            }
        }

        // constraint sigma(theo hang) = 1 ;
        for (int i = 0; i < n; i++) {
            MPConstraint c = solver.makeConstraint(1, 1, "row" + String.valueOf(i));
            for (int j = 0; j < n; j++) {
                c.setCoefficient(x[i][j], 1);
            }
        }

        // constraint sigma(theo cot) = 1
        for (int i = 0; i < n; i++) {
            MPConstraint c = solver.makeConstraint(1, 1, "col" + String.valueOf(i));
            for (int j = 0; j < n; j++) {
                c.setCoefficient(x[j][i], 1);
            }
        }
        obj = solver.objective();
        for (int i = 0 ; i <n; i++){
            for (int j = 0 ; j < n; j++){
                obj.setCoefficient(x[i][j],cost[i][j]);
            }
        }
        MPSolver.ResultStatus stat;
        System.out.println("Begin");

        while (countSubtour > 1){
            // giai bai toan ban dau khi chua co rang buoc SEC
            stat = solver.solve();
            System.out.println(x[0][1].solutionValue());
            if(stat != MPSolver.ResultStatus.OPTIMAL){
                System.err.println("not exist optimal solution !");
                return;
            }
            //prinTour(this.x);
            // detect subtour va cap nhat so subTour
            detectSubtour(this.x);
            // them constraint cho cac chu trinh con
            addSEC_Constraint();
            System.out.println("countSubtour " + this.countSubtour);
        }
        for (int i = 0; i< n; i++){
            for (int j = 0; j < n; j++){
                System.out.println("X["+i+","+j+"] = "+x[i][j].solutionValue());
            }
        }
        System.out.println("Optimal Tour " + solver.objective().value());
        prinTour(this.x);
    }
    private void process(HashSet<Integer> hashSet){
        if(hashSet.size() >= 1 && hashSet.size() < n){
            MPConstraint c = solver.makeConstraint(0,hashSet.size() - 1);
            for(int i : hashSet){
                for (int j : hashSet){
                    c.setCoefficient(x[i][j],1);
                }
            }
        }
    }
    private void detectSubtour(MPVariable[][] x){
        if(listSubtour.size() > 0) listSubtour.clear();
        System.out.println("detect subtour ");
        boolean mark[] = new boolean[n];
        for (int i = 0; i< n; i++){
            mark[i] = false;
        }
        int current ;
        for (int i = 0; i < n; i++){
            if(!mark[i]){
                mark[i] = true;// chu trinh tiep theo se bat dau tu i
                current = i;
                HashSet<Integer> s = new HashSet<Integer>(); // tao ra mot Set cac node cua chu trinh moi
                s.add(current);
                // tim chu trinh bat dau tu current
                int next = -1;// bien next canh cho den khi next = i
                while (i != next){
                    for(int j = 0 ; j < n; j++){// tim node ma current di den
                        if(x[current][j].solutionValue() == 1){
                            current = j;// cap nhat current
                            next = j;
                            mark[j] = true;
                            s.add(next);
                            break;
                        }
                    }
                }
                listSubtour.add(s);
            }
        }
        countSubtour = listSubtour.size();// cap nhat countSubtour
        System.out.println(countSubtour);
    }
    private void addSEC_Constraint(){
        for(int i = 0 ; i < listSubtour.size(); i++){
           process(listSubtour.get(i));
        }
    }
    public void prinTour(MPVariable[][] x){
        int current = 0;
        for (int i = 0; i< n; i++){
            System.out.print(current + "--->");
            for (int j = 0 ; j < n; j++){
                if(x[current][j].solutionValue() == 1){
                    current = j;
                    break;
                }
            }
        }
        System.out.println(0);
    }
    public void getDataFromFile(){
        try {
            File myObj = new File("/home/luong/planningoptimization/data/TSP/tsp-100.txt");
            Scanner myReader = new Scanner(myObj);
            this.n = myReader.nextInt();
            cost = new int[n][n];
            for(int i = 0; i < n;i++){
                System.out.println();
                for (int j = 0; j < n; j++){
                    cost[i][j] = myReader.nextInt();
                    System.out.print(cost[i][j] +" ");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        TSP_DynamicSEC tsp_dynamicSEC = new TSP_DynamicSEC();
        tsp_dynamicSEC.solve();
    }
}
