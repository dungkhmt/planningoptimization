package class118133.vutrungnghia.csp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class NQueenHeuristic {
    int X[];
    int N;
    private int countViolations(){
        int n = 0;
        for(int i = 0; i < N; ++i){
            for(int j = i + 1; j < N; ++j){
                if(X[i] == X[j])
                    n++;
                if(X[i] + i == X[j] + j)
                    n++;
                if(X[i] - i == X[j] - j)
                    n++;
            }
        }
        return n;
    }

    private int selectRandomSuspect(){
        int[] arr = new int[N];
        for(int i = 0; i < N; ++i)
            arr[i] = 0;
        for(int i = 0; i < N; ++i){
            for(int j = i + 1; j < N; ++j){
                if(X[i] == X[j]){
                    arr[i]++;
                    arr[j]++;
                }
                if(X[i] + i == X[j] + j){
                    arr[i]++;
                    arr[j]++;
                }
                if(X[i] - i == X[j] - j){
                    arr[i]++;
                    arr[j]++;
                }
            }
        }

        int maxViolations = -1000;
        for(int i = 0; i < N; ++i){
            if(arr[i] > maxViolations)
                maxViolations = arr[i];
        }
        ArrayList<Integer> suspects = new ArrayList<>();
        for(int i = 0; i < N; ++i)
            if(arr[i] == maxViolations)
                suspects.add(i);
        Random random = new Random();
        return suspects.get(random.nextInt(suspects.size()));
    }

    private void solve(){
        System.out.println("N QUEEN");
        N = 200;
        X = new int[N];
        System.out.println("BOARD");
        for(int i = 0; i < N; ++i){
            X[i] = 0;
            System.out.print(X[i] + " ");
        }
        System.out.println();
        int globalMaxViolations = 1000000000;
//        for(int step = 0; step < 100; ++step)
        int iter = 0;
        while(globalMaxViolations != 0){
            System.out.println("ITER: "+ iter);
            iter++;
            int suspect = selectRandomSuspect();
            System.out.println("suspect: " + suspect);
            int violations = countViolations();
            int newRow = -1;
            int oldRow = X[suspect];
            System.out.println("old row: " + oldRow);
            System.out.println("illegal constraints: " + violations);
            int[] candidates = new int[N];
            ArrayList<Integer> arr = new ArrayList<>();
            for(int i = 0; i < N; ++i)
                candidates[i] = 1000000000;
            for(int i = 0; i < N; ++i){
                if(i == oldRow)
                    continue;
                X[suspect] = i;
                int newViolations = countViolations();
                candidates[i] = newViolations;
                if(newViolations < violations) {
                    violations = newViolations;
                    newRow = i;
                }

                for(int j = 0; j < N; ++j){
                    if(candidates[i] == violations)
                        arr.add(i);
                }

                if(arr.size() > 0){
                    Random random = new Random();
                    int index = random.nextInt(arr.size());
                    newRow = arr.get(index);
                }

                if(violations < globalMaxViolations)
                    globalMaxViolations = violations;
            }
            if (arr.size() == 0){
                System.out.println("Could not find better solution");
                X[suspect] = oldRow;
                print();
                System.exit(0);
            }
            X[suspect] = newRow;
            System.out.println("global illegal constraints: " + globalMaxViolations);
            System.out.println("new row: " + newRow);
            System.out.println("-------------------------------------");
        }
        print();
    }

    private void print(){
        for(int i = 0; i < N; ++i){
            System.out.print(X[i] + " ");
        }
    }
    public static void main(String[] args) {
        NQueenHeuristic nQueenHeuristic = new NQueenHeuristic();
        nQueenHeuristic.solve();
    }
}
