package class118133.danglamsan;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataModelTSP {
    public int[][] distanceMatrix;
    int len;
    public DataModelTSP(String file_name) {
        File file = new File(file_name);
        try {
            Scanner sc = new Scanner(file);
            this.len = sc.nextInt();
            this.distanceMatrix = new int[len][len];
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    this.distanceMatrix[i][j] = sc.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataModelTSP dataModelTSP = new DataModelTSP("./Data/tsp-50.txt");
        int len = dataModelTSP.len;
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                System.out.print(dataModelTSP.distanceMatrix[i][j] +" ");
            }
            System.out.println();
        }
//        int[] visited = new int[10];
//        for (int i = 0; i < 10; i++) {
//            System.out.println(visited[i] + " ");
//        }
    }

}
