import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import chocosolver.ChocoSolver;
import datagenerator.Condition;
import datagenerator.DataGenerator;
import heuristic.HeuristicSolver;


public class Main {
    public static void main(String[] args) {
        String fn = "data.txt";
        int half_day_period = 6;
        int n_day = 6;

        ChocoSolver app = new ChocgoSolver(half_day_period, n_day);
        JSONObject data = read_data(fn);
        // System.out.println(data.toJSONString());
        app.solve(data);


        Condition cond = new Condition();
        cond._n_teacher = 5;
        cond._n_class = 5;
        cond._n_subject = 10;
        cond._max_period_of_class = 24;
        cond._max_subject_of_teacher = 3;

        String data_dir = "test/";
        DataGenerator gen = new DataGenerator(data_dir);
        gen.generate(cond, 5);

    }

    public static JSONObject read_data(String fn) {
        File file = new File(fn);

        JSONObject data = new JSONObject();

        try {
            Scanner scanner = new Scanner(file);
            int T = scanner.nextInt();
            data.put("N_TEACHER", T);
            int N = scanner.nextInt();
            data.put("N_CLASS", N);
            int M = scanner.nextInt();
            data.put("N_CLASS_SUBJECT", M);

            JSONArray cn = new JSONArray();
            for (int it = 0; it < N; ++it) {
                JSONArray arr = new JSONArray();

                while (scanner.hasNextInt()) {
                    int val = scanner.nextInt();
                    if (val == 0) {
                        break;
                    }
                    arr.add(val);
                }
                cn.add(arr);
            }
            data.put("CLASS_NEED", cn);

            JSONArray tct = new JSONArray();
            for (int it = 0; it < T; ++it) {
                JSONArray arr = new JSONArray();

                while (scanner.hasNextInt()) {
                    int val = scanner.nextInt();
                    if (val == 0) break;
                    arr.add(val);
                }
                tct.add(arr);
            }
            data.put("TEACHER_CAN_TEACH", tct);

            JSONArray sp = new JSONArray();
            for (int it = 0; it < M; ++it) {
                int val = scanner.nextInt();
                sp.add(val);
            }
            data.put("SUBJECT_PERIOD", sp);
            scanner.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return data;
    } 
}
