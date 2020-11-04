package class118133.vuducnhi.heuristic;

import java.util.ArrayList;
import java.util.Random;

public class Example {
    static int findBetterPosition(int[] state, int worstIndex) {
        int bestCovered = calculateCellWillBeCoverBy(state, worstIndex);
        ArrayList<Integer> candidate = new ArrayList<Integer>();
        for (int i = 0; i < state.length; ++i) {
            if (i == worstIndex) continue;
            int temp = state[worstIndex];
            state[worstIndex] = i;
            int currentCovered = calculateCellWillBeCoverBy(state, worstIndex);
            if (currentCovered < bestCovered) {
                bestCovered = currentCovered;
                candidate.clear();
            }
            if (currentCovered == bestCovered) {
                candidate.add(i);
            }
            state[worstIndex] = temp;
        }
        Random random = new Random();
        return candidate.get(random.nextInt(candidate.size()));
    }

    static int calculateCellWillBeCoverBy(int[] state, int currentIndex) {
        int result = 0;
        for (int i = 0; i < state.length; ++i) {
            if (i == currentIndex) continue;
            result += (state[i] == state[currentIndex]) ? 1 : 0;
            result += (state[i] - i == state[currentIndex] - currentIndex) ? 1 : 0;
            result += (state[i] + i == state[currentIndex] + currentIndex) ? 1 : 0;
        }
        return result;
    }

    static void printState(String message, int[] state) {
        for (int i = 0; i < state.length; ++i) {
            System.out.print(state[i] + " ");
        }
        System.out.println();
    }

    static int improveSolution(int[] state, int lastImprovedIndex) {
        int worstValue = 0;
        int worstIndex = -1;
        ArrayList<Integer> candidate = new ArrayList<Integer>();
        for (int i = 0; i < state.length; ++i) {
            if (i == lastImprovedIndex) continue;
            int countInvalidCells = calculateCellWillBeCoverBy(state, i);
            if (countInvalidCells > worstValue) {
                worstValue = countInvalidCells;
                candidate.clear();
            }
            if (countInvalidCells == worstValue && i != lastImprovedIndex) {
                candidate.add(i);
            }
        }
        Random random = new Random();
        worstIndex = candidate.get(random.nextInt(candidate.size()));
        int newPosition = findBetterPosition(state, worstIndex);
        state[worstIndex] = newPosition;

        return worstIndex;
    }

    static boolean verifyConstraint(int[] state) {
        for (int i = 0; i < state.length; ++i) {
            for (int j = 0; j < state.length; ++j) {
                if (i == j) continue;
                if (state[i] == state[j]) return false;
                if (state[i] + i == state[j] + j) return false;
                if (state[i] - i == state[j] - j) return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int N = 100;
        int[] currentState = new int[N];
        for (int i = 0; i < N; ++i) {
            currentState[i] = 0;
        }
        int lastImprovedIndex = -1;
        for (int loop = 0; loop < 1000000; ++loop) {
            int improvedIndex = improveSolution(currentState, lastImprovedIndex);
            lastImprovedIndex = improvedIndex;
        }
        System.out.println("Result:");
        for (int i = 0; i < N; ++i) {
            System.out.println("Queen " + i + " at row " + currentState[i]);
        }
        System.out.println("Solution is good or not: " + verifyConstraint(currentState));
    }
}
