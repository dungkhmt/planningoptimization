package dinhvanhieu;

import java.util.ArrayList;
import java.util.Random;

import localsearch.model.IConstraint;
import localsearch.model.VarIntLS;

public class GenericHillClimbingSearch {
	class Move{
		int i;// index of variable
		int val;
		public Move(int i, int val){
			this.i = i; this.val = val;
		}		
	}
	
	ArrayList<Move> cand = new ArrayList<Move>();
	IConstraint c;
	VarIntLS[] X;
	Random R = new Random();
	private void exploreNeighborhood(){
		cand.clear();
		int minDelta = Integer.MAX_VALUE;
		for(int i = 0; i < X.length; i++){
			for(int v = X[i].getMinValue(); v <= X[i].getMaxValue(); v++){
				if(v != X[i].getValue()){
					int delta = c.getAssignDelta(X[i], v);// query quality of a neighbor
					if(delta > 0) continue;
					if(delta < minDelta){
						cand.clear();
						cand.add(new Move(i,v));
						minDelta = delta;
					}else if(delta == minDelta){
						cand.add(new Move(i,v));
					}
				}
			}
		}
	}
	public void search(IConstraint c, int maxIters, int maxTime){
		this.c = c;
		this.X = c.getVariables();
		cand.clear();
		double t0 = System.currentTimeMillis();
		for(int it = 1; it <= maxIters; it++){
			if(System.currentTimeMillis() - t0 > maxTime) break;
			
			exploreNeighborhood();
			if(cand.size() <= 0){
				System.out.println("Reach local optimum"); break;
			}
			Move m = cand.get(R.nextInt(cand.size()));
			X[m.i].setValuePropagate(m.val);// local move for generating new solution
			System.out.println("Step " + it + " Move X[" + m.i + "] = " + m.val + ", violations = " + c.violations());
		}
	}
}
