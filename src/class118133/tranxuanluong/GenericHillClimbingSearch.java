import localsearch.model.IConstraint;
import localsearch.model.VarIntLS;

import java.util.ArrayList;
import java.util.Random;

public class GenericHillClimbingSearch {
    private static class Move{
        int i;
        int val;
        public Move(int i, int val){
            this.i = i;
            this.val = val;
        }
    }

    ArrayList<Move> candidates = new ArrayList<>();
    IConstraint constraint;
    VarIntLS[] X;
    Random random = new Random();

    public GenericHillClimbingSearch(IConstraint constraint){
        this.constraint = constraint;
        this.X = constraint.getVariables();
    }

    private void exploreNeighborhood(){
        candidates.clear();
        int minDelta = Integer.MAX_VALUE;
        for(int i = 0; i < X.length; ++i){
            for(int v = X[i].getMinValue(); v <= X[i].getMaxValue(); ++v){
                if(v != X[i].getValue()){
                    int delta = constraint.getAssignDelta(X[i], v);
                    if(delta > 0)
                        continue;
                    if(delta < minDelta){
                        candidates.clear();
                        candidates.add(new Move(i, v));
                        minDelta = delta;
                    }
                    else if(delta == minDelta)
                        candidates.add(new Move(i, v));
                }
            }
        }
    }

    public void search(int maxIterations){
        candidates.clear();
        double t0 = System.currentTimeMillis();
        for(int it = 0; it <= maxIterations; ++it){
//            if(System.currentTimeMillis() - t0 > maxTime)
//                break;

            exploreNeighborhood();

            if(constraint.violations() == 0){
                System.out.println("Reach global solution");
                break;
            }
            else if (candidates.size()==0){
                System.out.println("Reach local solution");
                break;
            }

            Move mv = candidates.get(random.nextInt(candidates.size()));
            X[mv.i].setValuePropagate(mv.val);
            System.out.println("Step: " + it + " Move: " + mv.i + " " + mv.val + " No.violations: " + constraint.violations());
        }
    }
}
