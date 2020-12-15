package Project;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class DataCreater {
    public ArrayList<Integer> GenerateUpN(int size){
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i< size; i++){
            list.add(i+1);
        }
        return list;
    }
    public void randData(int N, int M, int K) throws IOException {
        Random rand = new Random();
        File fi = new File("src/Project/data500.txt");
        OutputStream out = new FileOutputStream(fi);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write(N + " " + M + " "+K + "\n");
        int iter = N;
        for (int i = 1 ; i <= N; i++){
            int randL = rand.nextInt(M+1);
            randL = randL <M/2 ? M/2 : randL;
            ArrayList<Integer> L = GenerateUpN(M);
            while(randL >0){
                int index = rand.nextInt(L.size());
                int Li = L.get(index);
                L.remove(index);
                if (randL > 1){
                    writer.write(Li+ " ");
                }
                else writer.write(Li + "\n");
                randL--;
            }
            //writer.write( "\n");

        }
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        DataCreater creater = new DataCreater();
        creater.randData(500,100,50);


    }
}
