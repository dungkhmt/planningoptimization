import com.google.ortools.linearsolver.*;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

// week 5
public class TSP {
    static {
        System.loadLibrary("jniortools");
    }

//    int cost[][] = {{0,3,1,1},{4,0,2,1},{1,2,0,6},{7,1,1,0}};
    int cost[][];
    int n = 10;
    int b[] = new int[n];
    HashSet<Integer> hashSet;
    MPSolver solver = MPSolver.createSolver("TSP-Linear Programming ", String.valueOf(MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING));
    MPVariable[][] x = new MPVariable[n][n];
    Random random = new Random();



    public void solve() {
        System.out.println("Start solving ... ");
        // array integer 0,1
        genData();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                x[i][j] = solver.makeIntVar(0, 1, String.valueOf(i) + String.valueOf(j));
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

        // constraint SEC
        hashSet = new HashSet<>();
        TRY(0);
        MPObjective obj = solver.objective();
        for (int i = 0 ; i <n; i++){
            for (int j = 0 ; j < n; j++){
                obj.setCoefficient(x[i][j],cost[i][j]);
            }
        }

        MPSolver.ResultStatus stat = solver.solve();
        if(stat != MPSolver.ResultStatus.OPTIMAL){
            System.err.println("not exist optimal solution !");
            return;
        }
        for (int i = 0; i< n; i++){
            for (int j = 0; j< n;j++){
                System.out.println("X["+i+","+j+"] = "+x[i][j].solutionValue());
            }
        }
        System.out.println("Optimal Tour ");
        prinTour();

    }

    private void TRY(int k ){
        for ( int v = 0; v<=1; v++){
            b[k] = v;
            if(k == n-1){
                process();
            }
            else{
                TRY(k+1);
            }
        }
    }
    private void process(){
        hashSet.clear();
        for(int i = 0; i < n; i++){
            if (b[i] == 1 ) hashSet.add(i);
        }
        if(hashSet.size() > 1 && hashSet.size() < n){
            MPConstraint c = solver.makeConstraint(0,hashSet.size() - 1);
            for(int i : hashSet){
                for (int j : hashSet){
                    if(i != j){
                        c.setCoefficient(x[i][j],1);
                    }
                }
            }
        }

    }
    public void genData(){
        cost = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0 ; j < n; j++){
                cost[i][j] = random.nextInt(10) + 1;
            }
        }
    }

    // optimal tour

    public void prinTour(){
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



    public  static  void main(String args[]) {
        TSP tsp = new TSP();
        tsp.solve();





        // constraint khong ton tai chu trinh con

//        for ( int i = 0 ; i < Math.pow(2,n) ; i++){
//            int temp = i ;
//            String s = Integer.toBinaryString(i);
//            int add = n - s.length();
//            for (int addTop = 0; addTop < add; addTop ++){
//                s = "0" + s;
//            }
//            List<Integer> subGroup = new ArrayList<Integer>();
//            for(int j = 0; j < n; j++) {
//                if (s.charAt(j) == '1') {
//                    subGroup.add(j);
//                }
//            }
//            int len = subGroup.size();
//            //MPVariable tempp = new MPVariable();
//            int summ = 0;
//            `
//            for (int p = 0; p < len ; p++){
//                for (int q = p + 1; q < len; q++){
//                    summ += x[subGroup.get(p)][subGroup.get(q)];
//
//                }
//            }
//        }

        // SEC using backstracking
















    }

}

