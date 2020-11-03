package class118133.pqd;

import java.util.ArrayList;
import java.util.Random;

public class QueenLS {
	int n = 10;
	int[] x;
	Random R;
	ArrayList<Integer> cand;
	private void generateInitSolution(){
		// generate initial solution randomly
		for(int i = 0; i < n; i++)
			x[i] = R.nextInt(n);
	}
	private int violations(){
		int v = 0;
		for(int i = 0; i < n-1; i++)
			for(int j = i+1; j < n; j++){
				if(x[i] == x[j])v++;
				if(x[i] + i == x[j] + j) v++;
				if(x[i] - i == x[j] - j) v++;
			}
		return v;
	}
	private int violations(int i){
		int v = 0;
		for(int j = 0; j < n; j++) if(j != i){
			if(x[i] == x[j])v++;
			if(x[i] + i == x[j] + j)v++;
			if(x[i] -i == x[j] - j) v++;
		}
		return v;
	}
	private int selectMostViolatingQueen(){
		int q = -1;
		int maxV = 0;
		cand.clear(); 
		for(int i = 0; i < n; i++){
			int vi = violations(i);
			if(maxV < vi){
				maxV = vi;
				cand.clear();
				cand.add(i);
			}else if(maxV == vi){
				cand.add(i);
			}
		}
		q = cand.get(R.nextInt(cand.size()));
		return q; 
	}
	private int selectMostPromissingRow(int i){
		int sel_r = -1;
		int minV = Integer.MAX_VALUE;
		cand.clear();
		for(int r = 0; r < n; r++)
		//if(x[i] != r)
		{
			// query violations if i is moved to row r
			int oldR = x[i];
			x[i] = r;
			int vi = violations();
			x[i] = oldR;
			if(minV > vi){
				minV = vi;
				cand.clear();
				cand.add(r);
			}else if(minV == vi){
				cand.add(r);
			}
		}
		sel_r = cand.get(R.nextInt(cand.size()));
		return sel_r; 
	}
	public void solve(int n){
		this.n = n;
		x = new int[n];
		R = new Random();
		cand = new ArrayList<Integer>();
		generateInitSolution();
		for(int it = 1; it <= 1000000; it++){
			int i = selectMostViolatingQueen();
			int r = selectMostPromissingRow(i);
			x[i] = r;
			
			System.out.println("Step " + it + ", violations = " + violations());
			if(violations() == 0) break;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueenLS app = new QueenLS();
		app.solve(1000);
	}

}
