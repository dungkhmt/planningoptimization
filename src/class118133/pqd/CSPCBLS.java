package class118133.pqd;



import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class CSPCBLS {

	LocalSearchManager mgr;
	VarIntLS[] X;
	ConstraintSystem S;
	
	public void stateModel(){
		mgr = new LocalSearchManager();
		X = new VarIntLS[5];
		for(int i = 0; i < 5; i++){
			X[i] = new VarIntLS(mgr,1,5);			
		}
		
		S = new ConstraintSystem(mgr);
		
		//IFunction f1 = new FuncPlus(X[2],3);
		//IConstraint c1 = new NotEqual(f1, X[1]);
		//S.post(c1);
		S.post(new NotEqual(new FuncPlus(X[2],3), X[1]));
		S.post(new LessOrEqual(X[3], X[4]));
		S.post(new IsEqual(new FuncPlus(X[2],X[3]), new FuncPlus(X[0],1)));
		S.post(new LessOrEqual(X[4], 3));
		S.post(new IsEqual(new FuncPlus(X[1],X[4]), 7));
		S.post(new Implicate(new IsEqual(X[2], 1),new NotEqual(X[4], 2)));
		
		mgr.close();
		
	}
	class Move{
		int i;
		int value;
		public Move(int i, int value){
			this.i = i; this.value = value;
		}
	}
	public void hillClimbing(){
		GenericHillClimbingSearch searcher = new GenericHillClimbingSearch();
		searcher.search(S, 100000, 10000);
	}
	
	public void localSearch(){
		System.out.println("Init S.violations = " + S.violations());
		Random R = new Random();
		ArrayList<Move> cand = new ArrayList<Move>();
		for(int it = 1; it <= 100; it++){
			cand.clear();
			int min_delta = 10000000;
			VarIntLS sel_x = null;
			int sel_v = -1;
			
			// query neighborhood
			for(int i = 0; i < X.length; i++){
				for(int v = X[i].getMinValue(); v <= X[i].getMaxValue(); v++){
					int delta = S.getAssignDelta(X[i], v);
					if(delta < min_delta){
						min_delta = delta;
						cand.clear();
						cand.add(new Move(i,v));
					}else if(delta == min_delta){
						cand.add(new Move(i,v));
					}
				}
			}
			
			// local move
			Move m = cand.get(R.nextInt(cand.size()));
			
			X[m.i].setValuePropagate(m.value);
			
			System.out.println("Step " + it + ", S.violations = " + S.violations());
			if(S.violations() == 0) break;
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CSPCBLS app =new CSPCBLS();
		app.stateModel();
		//app.localSearch();
		app.hillClimbing();
	}

}
