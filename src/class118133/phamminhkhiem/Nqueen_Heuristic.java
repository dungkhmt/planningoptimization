package class118133.phamminhkhiem;

import java.util.Random;

public class Nqueen_Heuristic {
    public static int N = 10 ;
    public static int[] cot = new int[N];
    public static Random random = new Random();
    public static int[] columm_count = new int[N];
    public static int[] cross_1_count = new int[3*N];
    public static int[] cross_2_count = new int[3*N];
    public static void main(String[] args) {
        for (int i = 0 ; i < N ; i++)
            cot[i] = random.nextInt(N);
        int MAX_INTER = 100000;
        while (true){
            MAX_INTER -- ;
            if (MAX_INTER == 0 ) {
                System.out.println("Solution not found !!!");
                break;
            }
            for (int i = 0 ; i < N ; i++){
                columm_count[i] = 0 ;
                for (int j =  0 ; j < N ; j++) {
                    cross_1_count[i+ j] = 0;
                    cross_2_count[i- j +N] = 0;
                }
            }
            for (int i = 0 ; i < N ; i++){
                columm_count[cot[i]] ++;
                cross_1_count[cot[i] + i] ++ ;
                cross_2_count[cot[i] - i+N] ++ ;
            }
            int max = -10000 ;
            int max_pos = -1 ;
            int [] pos = new int[N];
            int num = 0 ;
            for (int i = 0 ; i < N ; i++){
                int count = columm_count[cot[i]] + cross_1_count[cot[i] + i] + cross_2_count[cot[i] - i + N];
                if (count > max ) {
                    max = count ;
                //    max_pos = i ;
                    num = 0;
                    pos[num] = i ;
                } else if (count == max ){
                    num ++;
                    pos[num] = i ;
                }
            }
            max_pos = pos[random.nextInt(num+1)] ;
            if (max <=3 ){
                System.out.println("Solution Found !! ");
                for (int i = 0 ; i <N ; i++)
                System.out.print(cot[i] + "_");
                break;
            }



             int min = Integer.MAX_VALUE  ;
            int min_pos = -1 ;
            for (int i = 0 ; i < N ; i++) {
                // try to put (max_pos) into i
                int count = columm_count[i] + cross_1_count[i + max_pos] + cross_2_count[i - max_pos+ N] + 3;
                if (count < min && i != cot[max_pos]){
                    min = count ;
                    min_pos = i ;
                    num = 0;
                    pos[num] = i ;
                }  else if (count == max ){
                    num ++;
                    pos[num] = i ;
                }
            }
            min_pos = pos[random.nextInt(num+1)] ;
            System.out.println("Move [" + max_pos +"," + cot[max_pos] + " ] to [" + max_pos +" " + min_pos + "]");
            cot[max_pos] = min_pos ;
        }
    }
}
