package class118133.tranthiuyen;
import java.util.ArrayList;
import java.util.Random;
import localsearch.model.ConstraintSystem;
import localsearch.model.VarIntLS;

public class HillClimbing {

    class AssignMove{
        int i,j1, j2;
        public AssignMove( int i, int j1, int j2){
            this.i = i;
            this.j1 = j1;
            this.j2 = j2;
        }
    }
    public void hillClimbing(ConstraintSystem c, VarIntLS[][] X, int maxIter) {
        ArrayList<AssignMove> cand = new ArrayList<AssignMove>();
        Random R = new Random();
        int it = 0;
        int N = X.length;
        while(it < maxIter && c.violations() > 0) {
            cand.clear();
            int minDelta = Integer.MAX_VALUE;
            for(int i = 0; i < N; i++) {
                for (int j1 = 0; j1 < N-1; j1++) {
                    for (int j2 = j1 + 1; j2 < N; j2++) {
                        int a = X[i][j1].getValue();
                        int b = X[i][j2].getValue();
                        int v = c.violations();
                        X[i][j1].setValuePropagate(b);
                        X[i][j2].setValuePropagate(a);
                        int d = c.violations() - v;
//                        System.out.format("%d %d\n", v, c.violations());
                        if (d < minDelta) {
                            cand.clear();
                            cand.add(new AssignMove(i, j1, j2));
                            minDelta = d;
                        } else if (d == minDelta) {
                            cand.add(new AssignMove(i, j1, j2));
                        }
                        X[i][j1].setValuePropagate(a);
                        X[i][j2].setValuePropagate(b);
                    }
                }
            }
            if (minDelta>0){
                System.out.println("Reach local minimum");
                return;
            }
            int idx = R.nextInt(cand.size());
            AssignMove m = cand.get(idx);
            int i = m.i;
            int j1 = m.j1;
            int j2 = m.j2;
            int a = X[i][j1].getValue();
            int b = X[i][j2].getValue();
            X[i][j1].setValuePropagate(b);
            X[i][j2].setValuePropagate(a);
            System.out.println("Step " + it + ", violations = " + c.violations());
            it++;
        }
        System.out.println("End");
    }
}
