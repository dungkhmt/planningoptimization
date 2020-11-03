package class118133.nguyenvanchuc;

import java.util.ArrayList;
import java.util.Random;
//import java.util.function.IntFunction;
//
//import de.rototor.pdfbox.graphics2d.IPdfBoxGraphics2DFontTextDrawer;
//import localsearch.constraints.basic.Implicate;
//import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
//import localsearch.functions.basic.FuncPlus;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class BCA {
    LocalSearchManager mgr;
    VarIntLS[] X;
    ConstraintSystem S;
    int[][] Conflict = new int[13][13];
    int[] w = {3,3,4,3,4,3,3,3,4,3,3,4,4};
    int[][] day= new int[3][13];

    public void nhap(){
        for(int i=0; i<13; i++){
            for (int j=0; j<13;j++){
                Conflict[i][j]=0;
            }
        }
        Conflict[0][2]=1;
        Conflict[0][4]=1;
        Conflict[0][8]=1;
        Conflict[1][4]=1;
        Conflict[1][10]=1;
        Conflict[3][7]=1;
        Conflict[3][9]=1;
        Conflict[5][11]=1;
        Conflict[5][12]=1;
        Conflict[6][8]=1;
        Conflict[6][12]=1;

        //---------------------------------------------
//        for(int i=0; i<2; i++){
//            for (int j=0; j<13; j++){
//                day[i][j]=-1;
//            }
//        }
    }
    private void stateModel() {
        mgr = new LocalSearchManager();
        int N = 13;
        X = new VarIntLS[N];
        for (int i = 0; i < N; i++) {
            X[i] = new VarIntLS(mgr, 0, 2);

        }
        S = new ConstraintSystem(mgr);
        for (int i=0; i<N; ++i)
            for(int j=0; j<N; ++j){
                if(Conflict[i][j] == 1){
                    S.post(new NotEqual(X[i], X[j]));
                }
            }
        for (int x=0; x<3; x++) {
            IFunction f1 = new ConditionalSum(X, w, x);
            S.post(new LessOrEqual(f1,15));
            S.post(new LessOrEqual(14,f1));
        }


//		IFunction f1 = new FuncPlus(X[2], 3);
//		IConstraint c1 = new NotEqual(f1, X[1]);
//		S.post(c1);

//        S.post(new NotEqual(new FuncPlus(X[2], 3), X[1]));
//        S.post(new LessOrEqual(X[3], X[4]));
//        S.post(new IsEqual(new FuncPlus(X[2], X[3]), new FuncPlus(X[0], 1)));
//        S.post(new LessOrEqual(X[4], 3));
//        S.post(new IsEqual(new FuncPlus(X[1], X[4]), 7));
//        S.post(new Implicate(new IsEqual(X[2], 1), new NotEqual(X[4], 2)));
        mgr.close();
    }

    private void localSearch() {
		/*for (int i = 0; i < X.length; i++) {
			System.out.println("X[" + i + "] = " + X[i].getValue());
		}
		System.out.println("S.violations = " + S.violations());
		int delta = S.getAssignDelta(X[3], 4);
		System.out.println("delta = " + delta);

		X[3].setValuePropagate(4);
		System.out.println("new violations = " + S.violations());*/

        System.out.println("Init S.violations = " + S.violations());
        ArrayList<Move> cand = new ArrayList<Move>();
        Random r = new Random();
        for (int it = 1; it <= 1000; it++) {
            cand.clear();
            int min_delta = 10000000;
//			VarIntLS sel_x = null;
//			int sel_v = -1;

            // query neighborhood
            for (int i = 0; i < X.length; i++) {
                for (int v = X[i].getMinValue(); v <= X[i].getMaxValue(); v++) {
                    int delta = S.getAssignDelta(X[i], v);
                    if (delta < min_delta) {
                        min_delta = delta;
//						sel_x = X[i];
//						sel_v = v;
                        cand.clear();
                        cand.add(new Move(i, v));
                    }
                    else if (delta == min_delta) {
                        cand.add(new Move(i, v));
                    }
                }
            }

            // local move
//			sel_x.setValuePropagate(sel_v);
            Move m = cand.get(r.nextInt(cand.size()));
            X[m.i].setValuePropagate(m.value);
            System.out.println("Step " + it + ", S.violations = " + S.violations());
            if (S.violations()==0)
                break;
        }
    }

    class Move{
        int i;
        int value;
        public Move(int i, int value) {
            this.i = i;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        BCA app = new BCA();
        app.stateModel();
        app.localSearch();
    }
}

