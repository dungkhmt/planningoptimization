package class118133.levuloi;

import java.util.ArrayList;
import java.util.Random;

public class NQueenHeuristic {
	private int N;
	private int[] errorSignal;
	private int[] pos;
	private Random rd = new Random();
	
	public void init() {
		N = 300;
		pos = new int[N];
		errorSignal = new int[N];
		for (int i = 0; i < N; i++) {
			pos[i] = rd.nextInt(N);
		}
	}
	
	public void clearErrorSignal() {
		for (int i = 0; i < N; i++) {
			errorSignal[i] = 0;
		}
	}
	
	public int computeViolentAndAccumError() {
		int res = 0;
		clearErrorSignal();
		for (int i = 0; i < N - 1; i++) {
			for (int j = i + 1; j < N; j++) {
				if (pos[i] == pos[j] || pos[i] + i == pos[j] + j || pos[i] - i == pos[j] - j) {
					res++;
					errorSignal[i]++;
					errorSignal[j]++;
				}
			}
		}
		return res;
	}
	
	public int computeViolent() {
		int res = 0;
		for (int i = 0; i < N - 1; i++) {
			for (int j = i + 1; j < N; j++) {
				if (pos[i] == pos[j] || pos[i] + i == pos[j] + j || pos[i] - i == pos[j] - j) {
					res++;
				}
			}
		}
		return res;
	}
	
	public void printErrorSignal() {
		for (int i = 0; i < N; i++) {
			System.out.printf("Error[%d] = %d\n", i, errorSignal[i]);
		}
	}
	
	public int getWorstQueen() {
		int max = -1;
		for (int i = 0; i < N; i++) {
			if (max < errorSignal[i]) {
				max = errorSignal[i];
			}
		}
		
		ArrayList<Integer> L = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			if (errorSignal[i] == max) L.add(i);
		}
		
		int idx = rd.nextInt(L.size());
		return L.get(idx);
	}
	
	public void changePos(int w) {
		int best = computeViolent();
		int[] cache = new int[N];
		int oldPos = pos[w];
		cache[oldPos] = best;
		for (int i = 0; i < N; i++) {
			pos[w] = i;
			cache[i] = computeViolent();
			if (best > cache[i]) {
				best = cache[i];
			}
		}
		
		ArrayList<Integer> L = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			if (cache[i] == best) {
				L.add(i);
			}
		}
		int idx = rd.nextInt(L.size());
		pos[w] = L.get(idx);
	}
	
	public void iterative() {
		int constraintViolent = computeViolentAndAccumError();
		int w;
		int step = 0;
		while (true) {
			step++;
			System.out.println("Step " + step + ", violent = " + constraintViolent);
			if (constraintViolent == 0) {
				break;
			}
			w = getWorstQueen();
			changePos(w);
			constraintViolent = computeViolentAndAccumError();
		}
	}
	
	public void printPos() {
		for (int i = 0; i < N; i++) {
			System.out.printf("Pos[%d] = %d\n", i, pos[i]);
		}
	}
	
	public static void main(String[] args) {
		NQueenHeuristic ins = new NQueenHeuristic();
		ins.init();
		ins.iterative();
	}
}
