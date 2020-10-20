package class118133.caophuongnam;

import java.util.Random;
import java.util.Scanner;

public class NQueenHeuristic {

    private static int n;
    private static int[] heu;
    private int timeLoops = 500;

    int calcWrongValue(int ind, int[] x) {
        int d1 = x[ind] - (ind+1);
        int d2 = x[ind] + (ind+1);
        int res = 0;
        for (int i=0; i<n; ++i) if (i!=ind) {
            if (x[i] - (i+1) == d1) res++;
            if (x[i] + (i+1) == d2) res++;
            if (x[i] == x[ind]) res++;
        }
        return res;
    }

    PairCustom findWrongNodeWithValue() {
        int ind = 0, bestValue = -1, temp;
        for (int i=0; i<n; ++i) {
            temp = calcWrongValue(i, heu);
            if (temp > bestValue) {
                bestValue = temp;
                ind = i;
            }
        }
        PairCustom ans = new PairCustom();
        ans.setFirst(ind);
        ans.setSecond(bestValue);
        return ans;
    }

    int choosePositionToMove(int row, int bestValue) {
        int res = heu[row];
        int temp;
        for (int i=0; i<n; ++i) {
            int[] x = heu;
            x[row] = i+1;
            temp = calcWrongValue(i, x);
            if (temp < bestValue) {
                bestValue = temp;
                res = i+1;
            }
        }
        return res;
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        Random rd = new Random();
        System.out.println("Nhap vao so n:");
        n = scanner.nextInt();
        heu = new int[n];

        for (int i=0; i<n; ++i) {
            heu[i] = rd.nextInt(n) + 1;
        }

        PairCustom ans;
        int ind, nextPos;

        do {
            for (int i=0; i<n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (heu[i] == j + 1) {
                        System.out.print("* ");
                    } else System.out.print("o ");
                }
                System.out.println();
            }
            ans = findWrongNodeWithValue();
            ind = ans.first;
            nextPos = choosePositionToMove(ind, ans.second);
            heu[ind] = nextPos;
            timeLoops--;
            System.out.println(ind + ", " + ans.second);
        } while (ans.second > 0 && timeLoops>0);

        for (int i=0; i<n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (heu[i] == j + 1) {
                    System.out.print("* ");
                } else System.out.print("o ");
            }
            System.out.println();
        }

        System.out.println("After "+(500-timeLoops)+" loops!");
    }

    public static void main(String[] args) {
        NQueenHeuristic app = new NQueenHeuristic();
        app.run();
    }

    class PairCustom {
        int first, second;

        public void setFirst(int first) {
            this.first = first;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }
}
