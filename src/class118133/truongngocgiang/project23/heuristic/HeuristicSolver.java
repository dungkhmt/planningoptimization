package heuristic;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class HeuristicSolver {
    private int _half_day_period;
    private int _n_day;
    private int _T;
    private int _N;
    private int _M;
    private ArrayList<JSONArray> _class_need;
    private int[][] _teacher_can_teach;
    private int[] _subject_period;
    private Teacher[] _teachers;

    public HeuristicSolver(int half_day_period, int n_day) {
        this._half_day_period = half_day_period;
        this._n_day = n_day;
    }


    public void solve(JSONObject data) {
        parse(data);
        System.out.println("Parse data done");
        solve_problem();
    }

    private int solve_problem() {
        System.out.println("Finding Solution...");
        _teachers = new Teacher[_T];
        for (int i = 0; i < _T; ++i) {
            _teachers[i] = new Teacher(_half_day_period, _n_day);
            System.out.println("Create teacher " + i);
        }

        for (int c = 0; c < _N; ++c) {
            JSONArray cn = _class_need.get(c);
            for (int it = 0; it < cn.size(); ++it) {
                int sub = (int) cn.get(it)-1;
                
                Boolean notFoundSolution = true;
                for (int t = 0; t < _T; ++t) 
                if (can_teach(t, sub)){
                    int st = _teachers[t].add(c, sub, _subject_period[sub]);

                    if (st != 0) {
                        System.out.println(String.format("Lớp %d môn thứ %d do giáo viên %d bắt đầu lúc %d - kết thúc lúc %d", c, sub, t, st, st+_subject_period[sub]-1));
                        notFoundSolution = false;
                        break;
                    }
                }
                if (notFoundSolution) {
                    System.out.println("Cannot find solution!");
                    return 0;
                }
            }
        }

        return 1;
    }

    private Boolean can_teach(int t, int sub) {
        if (_teacher_can_teach[t][sub] == 1) return true;
        return false;
    }

    private void parse(JSONObject data) {
        this._T = (int) data.get("N_TEACHER");
        this._N = (int) data.get("N_CLASS");
        this._M = (int) data.get("N_CLASS_SUBJECT");

        this._class_need = new ArrayList<JSONArray>();
        JSONArray cn = (JSONArray) data.get("CLASS_NEED");
        for (int it = 0; it < _N; ++it) {
            this._class_need.add((JSONArray) cn.get(it));
        }

        _teacher_can_teach = new int[_M][_M];
        JSONArray tct = (JSONArray) data.get("TEACHER_CAN_TEACH");
        for (int i = 0; i < _T; ++i) {
            JSONArray tmp = (JSONArray) tct.get(i);
            for (int j = 0; j < tmp.size(); ++j) {
                int id = (int) tmp.get(j) - 1;
                _teacher_can_teach[i][id] = 1;               
            }
        }

        JSONArray sp = (JSONArray) data.get("SUBJECT_PERIOD");
        _subject_period = new int[_M];
        for (int i = 0; i < _M; ++i) {
            _subject_period[i] = (int) sp.get(i);
        }
    }
}
