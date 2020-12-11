package datagenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class DataGenerator {
    private String _dir;

    public DataGenerator(String dir) {
        this._dir = dir;
        File theDir = new File(dir);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    public void generate(Condition cond, int nTest) {
        Random rand = new Random();

        for (int it = 0; it < nTest; ++it) {

            int T = rand.nextInt(cond._n_teacher) + 1;
            int N = rand.nextInt(cond._n_class) + 1;
            int M = rand.nextInt(cond._n_subject) + 1;
            int max_period = rand.nextInt(cond._max_period_of_class - 14) + 14;
            int max_subject = rand.nextInt(cond._max_subject_of_teacher) + 1;
            while (T * max_subject < M) {
                T = rand.nextInt(cond._n_teacher) + 1;
                M = rand.nextInt(cond._n_subject) + 1;
                max_subject = rand.nextInt(cond._max_subject_of_teacher) + 1;
            }

            String fn = _dir + "/" + String.format("%d_%d_%d_%d_%d.txt", T, N, M, max_period, max_subject);
            File file = new File(fn);
            try {
                if (file.createNewFile()) {
                    System.out.println("Create file " + fn);
                } else {
                    System.out.println("File alreade exists");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                FileWriter writter = new FileWriter(file);
                writter.write(String.format("%d %d %d\n", T, N, M));
                int[] subject_period = new int[M];
                for (int i = 0; i < M; ++i)
                    subject_period[i] = rand.nextInt(5) + 1;
                Set<Integer> s_sub = getNewSet(M);
                for (int i = 0; i < N; ++i) {
                    int n_subject = rand.nextInt(max_period)+1;
                    int tmp = 0;
                    
                    s_sub = getNewSet(M);
                    for (int j = 0; j < n_subject; ++j) {
                        int sub = rand.nextInt(M);
                        if (s_sub.isEmpty()) break;
                        while (!s_sub.contains(sub)) sub = rand.nextInt(M);
                        
                        int period = subject_period[sub];
                        if (tmp + period > max_period) break;

                        tmp += period;
                        s_sub.remove(sub);
                        writter.write(String.format("%d ", sub+1));
                    }
                    writter.write(String.format("%d\n", 0));
                    System.out.println("subject " + i);
                }

                // int[] cnt = new int[M];
                s_sub = getNewSet(M);
                int tmp = M;
                for (int i = 0; i < T; ++i) {
                    int mx;
                    if (tmp > 0) mx = Math.min(tmp, max_subject);
                    else mx = rand.nextInt(max_subject) + 1;
                    tmp -= mx;

                    for (int j = 0; j < mx; ++j) {
                        int sub = rand.nextInt(M);
                        while (!s_sub.contains(sub)) sub = rand.nextInt(M);

                        s_sub.remove(sub);
                        if (s_sub.isEmpty()) s_sub = getNewSet(M);
                        writter.write(String.format("%d ", sub+1));
                    }

                    System.out.println("teacher " + i);
                    writter.write(String.format("%d\n", 0));
                }
                
                for (int i = 0; i < M; ++i)
                    writter.write(String.format("%d ", subject_period[i]));

                writter.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    private HashSet<Integer> getNewSet(int lenght) {
        HashSet<Integer> res = new HashSet<Integer>();
        for (int i = 0; i < lenght; i++) {
            res.add(i);
        }
        return res;
    }
}
