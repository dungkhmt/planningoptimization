package class118133.tranhoangchuan;

import java.util.ArrayList;
import java.util.Random;

public class NQueen {
	public int N=5;
	public int[] ans;
	int tmpInd = -1;
	boolean inBlackList[];
	Random rd = new Random();
	public boolean checkThang(int x1, int y1, int x2, int y2) {
		return (x1!= x2) && (y1 != y2);
	}
	public boolean checkCheo(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2) != Math.abs(y1 - y2);
	}
	public ArrayList<Integer> getFailQueen(int[] arr){
		int[] count = new int[N];
		ArrayList<Integer> ke[] = new  ArrayList[N];
		for (int i = 0; i < N; ++i) {
			ke[i] = new ArrayList<Integer>();
		}
		for (int i = 0; i < N; ++i) {
			for (int j = i+1; j < N; ++j) {
				if (checkCheo(i, arr[i], j, arr[j])) {
					count[i]++;
					count[j]++;
					ke[i].add(j);
					ke[j].add(i);
				}
			}
		}
		int res = 0;
		this.tmpInd = -1;
		for (int i = 0; i < N; ++i) {
			if (count[i] > res) {
				res = count[i];
				this.tmpInd = i;
			}
		}
		if (res == 0) return null;
		this.inBlackList = new boolean[N];
		for (int i =0; i < ke[tmpInd].size(); ++i) inBlackList[i] = true;
		return ke[tmpInd];
	}
	public void solve() {
		// Init
		for (int i = 0; i < N; ++i) {
			ans[i] = i;
		}
		for (int step = 0; step < 100000; ++step) {
			int min = N;
			ArrayList<Integer> f = getFailQueen(ans);
			if (f!= null) {
				int i;
				do {
					i = rd.nextInt(N);
				} while (!inBlackList[i]);
				int tmp = ans[i];
				ans[i] = ans[tmpInd];
				ans[tmpInd] = tmp;
				System.out.print("Fit: " + f.size());
			} else {
				System.out.print("Step: " + step + "Done" );
			}
		}
	}
	public static void main(String[] args) {
//		NQueen a = new NQueen();
//		a.solve();
		System.out.println("fasfsafs");
	}
}
