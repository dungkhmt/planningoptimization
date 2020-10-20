package class118133.danglamsan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeuristicQueen {
    int[] x;
    int n;
//    Random rd =
    public HeuristicQueen(int n) {
        this.n = n;
        this.x = new int[n];
    }

    public void initVar() {
        Random rd = new Random();
        for (int i = 0; i < this.n; i++) {
            this.x[i] = rd.nextInt(this.n);
        }
    }

    public int getWorstQueen() {
        int[] check_queen = new int[this.n];
        List<Integer> i_max = new ArrayList<>();

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (i != j && this.x[i] == this.x[j]) {
                    check_queen[i] += 1;
                }
            }
            for (int j = 0; j < this.n; j++) {
                if (i != j && this.x[i] - i == this.x[j] - j) {
                    check_queen[i] += 1;
                }
                if (i != j && this.x[i] + i == this.x[j] + j)
                    check_queen[i] += 1;
            }
        }
        int sum = 0, max = -1;
        for (int i = 0; i < this.n; i++) {
            sum += check_queen[i];
            if (max <= check_queen[i]) {
//                i_max = i;
                max = check_queen[i];
            }
        }
        Random rd = new Random();
        for (int i = 0; i < this.n; i++) {
            if (check_queen[i] == max) {
                i_max.add(i);
            }
        }
        return i_max.get(rd.nextInt(i_max.size()));
    }

    public int calFitness() {
        /*
            Calculate the number illegal constraint
         */
        int res = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = i + 1; j < this.n; j++) {
                if (i != j && this.x[i] == this.x[j]) {
                    res += 1;
                }
            }
            for (int j = i + 1; j < this.n; j++) {
                if (i != j && this.x[i] - i == this.x[j] - j) {
                    res += 1;
                }
                if (i != j && this.x[i] + i == this.x[j] + j)
                    res += 1;
            }

        }
        return res;

    }
    public void improve(int loop) {
//        int loop = 100;
        for (int cnt = 0; cnt < loop; cnt++) {
            Random rd = new Random();
            int i_max = getWorstQueen();
            int sum = calFitness();
            System.out.println("Number illegal constraint:= " + sum);

            int current_pos = this.x[i_max], illegal_constraint = sum;
            int[] illegal = new int[this.n];
            for (int i = 0; i < this.n; i++) {
                    this.x[i_max] = i;
                    int tmp_fit = calFitness();
                    if (tmp_fit <= illegal_constraint) {
                        illegal_constraint = tmp_fit;
                        illegal[i] = tmp_fit;
                    }
            }
            if (illegal_constraint == 0) {
                System.out.println("Problem is solved with optimal solution");
                System.exit(0);
            }
            List<Integer> chosen_row = new ArrayList<>();
            for (int i = 0; i < this.n; i++) {
                if (illegal[i] == illegal_constraint)
                    chosen_row.add(i);
            }
            this.x[i_max] = chosen_row.get(rd.nextInt(chosen_row.size()));
        }

    }
    public static void main(String[] args) {
        HeuristicQueen heuristicQueen = new HeuristicQueen(500);
        heuristicQueen.initVar();
        heuristicQueen.improve(300);
    }
}

