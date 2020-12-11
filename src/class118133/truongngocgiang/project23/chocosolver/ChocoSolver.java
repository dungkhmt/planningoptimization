package chocosolver;

import java.util.ArrayList;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChocoSolver {
    private int half_day_period;
    private int n_day;
    private Model model;
    private Solver solver;
    private JSONObject data;



    public ChocoSolver() {
        this.half_day_period = 0;
        this.n_day = 0;
    }

    public ChocoSolver(int half_day_period, int n_day) {
        this.half_day_period = half_day_period;
        this.n_day = n_day;
    }

    public int solve(JSONObject data) {
        this.data = data;
        model = this.build_model(data);
        System.out.println("Build model done!");

        solver = model.getSolver();
        System.out.println("Get solver done!");

        if (solver.solve()) {
            print_solution();
            return 1;
        }
        System.out.println("The solver has proved the problem has no solution");
        return 0;
    }

    void print_solution() {
        Solution solution = new Solution(model);
        System.out.println("Find a solution");
        solution.record();
        String solution_str = solution.toString();
        solution_str = solution_str.substring(10);
        String[] abc = solution_str.split(",");

        int cnt = 0;
        int class_id = 0;
        JSONArray class_subject = (JSONArray)((JSONArray) data.get("CLASS_NEED")).get(class_id);
        System.out.println(String.format("Lớp %d:", class_id+1));
        int _N = (int) data.get("N_CLASS");
        for (String s : abc) 
        if (    s.contains("class_subject_to_teacher") 
                || s.contains("class_subject_start")
                || s.contains("class_subject_end")){
            int sub_id = cnt/3;
            ++cnt;
            if (cnt%3 == 1) {
                String[] tmp = s.split("=");
                int t = Integer.parseInt(tmp[1]);
                System.out.print(String.format("\tGiáo viên %d dạy môn %d ", t, (int)class_subject.get(sub_id)));
            }
            else if (cnt%3 == 2) {
                String[] tmp = s.split("=");
                int p = Integer.parseInt(tmp[1]);
                int day = p/(2 * half_day_period);
                int period = p - day * 2 * half_day_period;
                if (period == 0) {
                    --day;
                    period = 2 * half_day_period;
                }

                System.out.print(String.format("\tBắt đầu từ tiết %d thứ %d ", period, day+2));
            }
            else {
                String[] tmp = s.split("=");
                int p = Integer.parseInt(tmp[1]);
                int day = p/(2 * half_day_period) ;
                int period = p - day * 2 * half_day_period;
                if (period == 0) {
                    --day;
                    period = 2 * half_day_period;
                }

                System.out.println(String.format("\tKết thúc vào tiết %d thứ %d", period, day+2));
            }
            if (cnt/3 == class_subject.size()) {
                ++class_id;
                if (class_id < _N) {
                    class_subject = (JSONArray)((JSONArray) data.get("CLASS_NEED")).get(class_id);
                    cnt = 0;
                    System.out.println(String.format("Lớp %d:", class_id+1));
                }
            }

        }            

    }

    public Model build_model(JSONObject data) {
        int _T = (int) data.get("N_TEACHER");
        int _N = (int) data.get("N_CLASS");
        int _M = (int) data.get("N_CLASS_SUBJECT");

        JSONArray _teacher_can_teach = (JSONArray) data.get("TEACHER_CAN_TEACH");
        JSONArray _class_need = (JSONArray) data.get("CLASS_NEED");
        JSONArray _subject_period = (JSONArray) data.get("SUBJECT_PERIOD");

        Model model = new Model();

        ArrayList<ArrayList<Integer>> teacher_can_teach_subject = new ArrayList<ArrayList<Integer>>();
        for (int it = 0; it < _M; ++it) teacher_can_teach_subject.add(new ArrayList<Integer>());
        for (int i1 = 0; i1 < _teacher_can_teach.size(); ++i1) {
            JSONArray sub_arr = (JSONArray) _teacher_can_teach.get(i1);
            for (int i2 = 0; i2 < sub_arr.size(); ++i2) {
                int sub = (int) sub_arr.get(i2);
                teacher_can_teach_subject.get(sub-1).add(i1+1);
            }
        }

        ArrayList<ArrayList<IntVar>> class_subject_to_teacher = new ArrayList<ArrayList<IntVar>>();
        ArrayList<ArrayList<IntVar>> class_subject_start = new ArrayList<ArrayList<IntVar>>();
        ArrayList<ArrayList<IntVar>> class_subject_end = new ArrayList<ArrayList<IntVar>>();
        for (int i = 0; i < _N; ++i) {
            class_subject_to_teacher.add(new ArrayList<IntVar>());
            class_subject_start.add(new ArrayList<IntVar>());
            class_subject_end.add(new ArrayList<IntVar>());
            JSONArray class_need_i = (JSONArray) _class_need.get(i);

            for (int j = 0; j < class_need_i.size(); ++j) {
                int subject = (int) class_need_i.get(j);
                int period = (int) _subject_period.get(subject-1);

                IntVar teacher = model.intVar(String.format("class_subject_to_teacher[%d][%d]", i, j), 1, _T);
                IntVar subject_start = model.intVar(String.format("class_subject_start[%d][%d]", i, j), 1, 2 * this.half_day_period * this.n_day);
                IntVar subject_end = model.intVar(String.format("class_subject_end[%d][%d]", i, j), 1, 2 * this.half_day_period * this.n_day);

                class_subject_to_teacher.get(i).add(teacher);
                class_subject_start.get(i).add(subject_start);
                class_subject_end.get(i).add(subject_end);

                // Thời điểm bắt đầu + số tiết học = Thời điểm kết thúc
                subject_start.add(period-1).eq(subject_end).decompose().post();

                // Giáo viên phải dạy đúng môn học
                ArrayList<Integer> t_sub = teacher_can_teach_subject.get(subject-1);
                BoolVar correct_teacher = model.boolVar(String.format("Correct_teacher[%d][%d]", i, j), false);
                for (int k = 0; k < t_sub.size(); ++k) { 
                    int k_teacher = (int) t_sub.get(k);
                    correct_teacher = correct_teacher.or(teacher.eq(k_teacher)).boolVar();
                }
                correct_teacher.extension().post();

                // Môn học phải kết thúc trước khi hết buổi
                BoolVar subject_end_in_correct_half_day = model.boolVar(String.format("subject_end_in_correct_half_day[%d][%d]", i, j), false);
                for (int k = 0; k < 2*this.n_day; ++k) {
                    int start_half_day = k * this.half_day_period;
                    int end_half_day = (k+1) * this.half_day_period;

                    ReExpression bool_and = subject_start.gt(start_half_day);
                    bool_and = bool_and.and(subject_start.le(end_half_day));
                    bool_and = bool_and.and(subject_end.le(end_half_day));

                    subject_end_in_correct_half_day = subject_end_in_correct_half_day.or(bool_and).boolVar();
                }
                subject_end_in_correct_half_day.extension().post();
            }
        }

        for (int i1 = 0; i1 < _N; ++i1) {
            JSONArray class_need_i1 = (JSONArray) _class_need.get(i1);

            for (int j1 = 0; j1 < class_need_i1.size(); ++j1) {
                IntVar t1 = class_subject_to_teacher.get(i1).get(j1);
                IntVar st1 = class_subject_start.get(i1).get(j1);
                IntVar en1 = class_subject_end.get(i1).get(j1);

                // Các môn học cùng lớp không được trùng lịch nhau
                for (int j2 = j1+1; j2 < class_need_i1.size(); ++j2) {
                    IntVar st2 = class_subject_start.get(i1).get(j2);
                    IntVar en2 = class_subject_end.get(i1).get(j2);

                    en1.lt(st2).or(en2.lt(st1)).decompose().post();
                }       
                
                // Các môn cùng giáo viên không được trùng lịch nhau
                for (int i2 = i1+1; i2 < _N; ++i2) {
                    JSONArray class_need_i2 = (JSONArray) _class_need.get(i2);

                    for (int j2 = 0; j2 < class_need_i2.size(); ++j2) {
                        IntVar t2 = class_subject_to_teacher.get(i2).get(j2);
                        IntVar st2 = class_subject_start.get(i2).get(j2);
                        IntVar en2 = class_subject_end.get(i2).get(j2);

                        t1.eq(t2).imp(en1.lt(st2).or(en2.lt(st1))).decompose().post();
                    }
                }
            }

        }
        return model;
    }

    



}