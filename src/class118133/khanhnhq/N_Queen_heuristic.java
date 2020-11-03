package khanhnhq;

import java.util.ArrayList;
import java.util.Random;

public class N_Queen_heuristic {
	int n = 8;
	int[] x = new int[n+5];
	ArrayList<Integer> max_queen = new ArrayList<Integer>();
	ArrayList<Integer> position = new ArrayList<Integer>();
	public int find_queen(int pre) {
		max_queen.clear();
		int ans = -1;
		int tmp = 0;
		for(int i = 0; i < n; i++) {
			if(i != pre) {
				int cnt = 0;
				for(int j = 0; j < n; j++) {
					if(j != i) {
						if((x[i] == x[j]) || (x[i] - i == x[j] - j) || (x[i] + i == x[j] + j)) {
							cnt ++;
						}
					}
				}
				if(tmp == cnt && tmp > 0) {
					max_queen.add(i);
				}
				if(cnt > tmp) {
					tmp = cnt;
					max_queen.clear();
					max_queen.add(i);
				}
			}
		}
		if(max_queen.size() == 0) return -1;
//		System.out.println();
		Random random = new Random();
		ans = random.nextInt(max_queen.size());
		
		return max_queen.get(ans);
	}
	public int find_position(int t, int pre) {
		int ans = 0;
		int tmp = 10000;
		position.clear();
		for(int i = 0; i < n; i++) {
			if(i != pre) {
				int cnt = 0;
				x[t] = i;
				for(int j = 0; j < n; j++) {
					if(j != t) {
						if((x[t] == x[j]) || (x[t] - t == x[j] - j) || (x[t] + t == x[j] + j)) {
							cnt ++;
						}
					}
				}
				if(tmp == cnt) {
					position.add(i);
				}
				if(tmp > cnt) {
					tmp = cnt;
					position.clear();
					position.add(i);
				}
			}
			
		}
		Random random = new Random();
		ans = random.nextInt(position.size());
		return position.get(ans);
	}
	public void solve_heuristic() {
		
		for(int i = 0; i < n; i++) {
			x[i] = 0;
		}
//		System.out.println(find_queen(-1));
		int pre = -1;
		int c = 0;
		while(true) {
			c++;
			int q = find_queen(pre);
			pre = q;
//			System.out.println(q);
			if(q == -1) {
				break;
			}
//			System.out.print(x[q] + " ");
			int new_position = find_position(q, x[q]);
//			System.out.println(new_position);
			x[q] = new_position;
			if(c > 100000) break;
			System.out.println("Epoch:" + c);
		}
			
		for(int i = 0; i < n; i++) {
    		System.out.println("Q_" + i +  "=" + x[i]);
    	}
		
	}
    
    public static void main(String[] args) {
    	N_Queen_heuristic  N_queen = new N_Queen_heuristic();
//    	N_queen.n = 8;
        N_queen.solve_heuristic();
    }
}
