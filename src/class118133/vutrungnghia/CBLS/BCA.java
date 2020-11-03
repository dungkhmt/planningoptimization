package class118133.vutrungnghia.CBLS;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

import java.io.File;
import java.util.*;

class Move{
    int i;
    int value;
    public Move(int i, int value) {
        this.i = i;
        this.value = value;
    }
}


public class BCA {
    int NClasses;
    int NConflictClasses;
    int NLecturers;
    int [] lessonsPerClass;
    ArrayList<Integer> [] conflicts;
    ArrayList<Integer> [] lecturersTeachingClasses;
    ArrayList<Integer> [] valuesSpace; // from lecturers teaching class

    LocalSearchManager mgr;
    VarIntLS[] assign;
    ConstraintSystem constraints;

    private void readInput(){
        try{
            Scanner in = new Scanner(new File("src/class118133/vutrungnghia/CBLS/BCA_input.txt"));
            NClasses = in.nextInt();
            NConflictClasses = in.nextInt();
            NLecturers = in.nextInt();
            lessonsPerClass = new int[NClasses];
            for(int i = 0; i < NClasses; ++i)
                lessonsPerClass[i] = in.nextInt();
            conflicts = new ArrayList[NConflictClasses];
            for(int i = 0; i < NConflictClasses; ++i){
                conflicts[i] = new ArrayList<>();
                conflicts[i].add(in.nextInt());
                conflicts[i].add(in.nextInt());
            }

            lecturersTeachingClasses = new ArrayList[NLecturers];
            for(int i = 0; i < NLecturers; ++i){
                lecturersTeachingClasses[i] = new ArrayList<>();
                int n = in.nextInt();
                for(int j = 0; j < n; ++j)
                    lecturersTeachingClasses[i].add(in.nextInt());
            }
            valuesSpace = new ArrayList[NClasses];
            for(int i = 0; i < NClasses; ++i)
                valuesSpace[i] = new ArrayList<>();
            for(int i = 0; i < NLecturers; ++i){
                for(int n: lecturersTeachingClasses[i])
                    valuesSpace[n].add(i);
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void printInput(){
        System.out.println("Number of lessons per class:");
        for(int i = 0; i < NClasses; ++i)
            System.out.println("Class " + i + ": " + lessonsPerClass[i]);
        System.out.println("Conflict classes:");
        for(int i = 0; i < NConflictClasses; ++i)
            System.out.println("pair: " + conflicts[i].get(0) + " - " + conflicts[i].get(1));
        System.out.println("Values space:");
        for(int i = 0; i < NClasses; ++i){
            System.out.print(i + ": ");
            for(int j: valuesSpace[i])
                System.out.print(j + " ");
            System.out.println();
        }
    }

    public void stateModel(){
        mgr = new LocalSearchManager();

        assign = new VarIntLS[NClasses];
        for(int i = 0; i < NClasses; ++i){
            Set<Integer> s = new HashSet<>();
            for(int v: valuesSpace[i])
                s.add(v);
            assign[i] = new VarIntLS(mgr, s);
        }

        // conflicted classed can not be assigned to the same lecturer
        constraints = new ConstraintSystem(mgr);
        for(int i = 0; i < NConflictClasses; ++i){
            int class1 = conflicts[i].get(0);
            int class2 = conflicts[i].get(1);
            constraints.post(new NotEqual(assign[class1], assign[class2]));
        }


        // the number of lessons assigned to a lecturer <= [int(total lessons / nLecturers) + 1]
        int totalLessons = 0;
        for(int i = 0; i < NClasses; ++i)
            totalLessons += lessonsPerClass[i];
        int supLessonsPerLecturer = Math.round(totalLessons * 1.0f / NLecturers) + 1;
        for(int lecturer = 0; lecturer < NLecturers; ++lecturer){
            IFunction lessonsPerLecturer = new ConditionalSum(assign, lessonsPerClass, lecturer);
            constraints.post(new LessOrEqual(lessonsPerLecturer, supLessonsPerLecturer));
        }

        mgr.close();
    }
    public void localSearch(){
        System.out.println("---------------------------------------------------------------");
        System.out.println("Init No.violations: " + constraints.violations());
        ArrayList<Move> candidates = new ArrayList<>();
        Random r = new Random();
        for(int it = 0; it < 1000; ++it){
            candidates.clear();
            int minViolations = 1000000;
            for(int i = 0; i < NClasses; ++i){
                for(int v: valuesSpace[i]){
                    int delta = constraints.getAssignDelta(assign[i], v);
                    if(delta < minViolations){
                        minViolations = delta;
                        candidates.clear();
                        candidates.add(new Move(i, v));
                    }
                    else if(delta == minViolations)
                        candidates.add(new Move(i, v));
                }
            }
            Move m = candidates.get(r.nextInt(candidates.size()));
            assign[m.i].setValuePropagate(m.value);
            System.out.println("Step " + it + ", violations = " + constraints.violations());
            if(constraints.violations() == 0){
                System.out.println("Found solution: ");
                break;
            }
        }

        System.out.println("Assigned results:");
        System.out.println(String.format("%-6s %-10s %-10s", "Class", "No.lessons", "Lecturer"));
        for(int i = 0; i < NClasses; ++i){
            System.out.println(String.format("%-6d %-10d %-10d", i, lessonsPerClass[i], assign[i].getValue()));
        }

        System.out.println("No.lessons per lecturer: ");
        for(int lecturer = 0; lecturer < NLecturers; ++lecturer){
            int s = 0;
            for(int i = 0; i < NClasses; ++i)
                if(assign[i].getValue() == lecturer)
                    s += lessonsPerClass[i];
            System.out.println("Lecturer: " + lecturer + " - " + s);
        }
    }

    public static void main(String[] args) {
        BCA bca = new BCA();
        bca.readInput();
        bca.printInput();
        bca.stateModel();
        bca.localSearch();
    }
}
