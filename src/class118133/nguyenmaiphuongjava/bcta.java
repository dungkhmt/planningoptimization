package class118133.nguyenmaiphuongjava;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.functions.max_min.Min;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.HillClimbing;
import sun.tools.tree.EqualExpression;

public class bcta {
        int numClass = 13, numTeacher = 3;
        int[] lesson = {3, 3, 4, 3, 4, 3, 3, 3, 4, 3, 3, 4, 4};
        int[][] canTeach = {
            {1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0},
            {1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1}
        };

        int[][] conflict = {
            {0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0}
        };

        LocalSearchManager mgr;
        ConstraintSystem constraintSystem;
        VarIntLS[] x;
        IFunction[] numberLessons;
        IFunction constraintNumberLessons;

    public void balancedClassTeacherAssignment()
    {
        //Khởi tạo bài toán
        mgr = new LocalSearchManager();
        x = new VarIntLS[numClass];
        for(int i = 0; i < numClass; ++i)
            x[i] = new VarIntLS(mgr, 0, numTeacher -1);

        //Thêm ràng buộc
        constraintSystem = new ConstraintSystem(mgr);
        for(int i = 0; i < numClass; i++)
            for(int j = 0; j < numClass; j++)
                if(conflict[i][j] == 1)
                {
                    IConstraint c = new NotEqual(x[i], x[j]);
                    constraintSystem.post(c);
                }
    
        numberLessons = new IFunction[numTeacher];
        for(int i = 0; i < numTeacher; i++)
            numberLessons[i] = new ConditionalSum(x, lesson, i);

        constraintNumberLessons = new Min(numberLessons);
        constraintSystem.post(new LessOrEqual(14, constraintNumberLessons));
        mgr.close();
    }

    public void search()
    {
        HillClimbing searcher = new HillClimbing();
        searcher.hillClimbing(constraintSystem, 10000);
    }

    public void printResult()
    {
        for(int i = 0; i < numTeacher; i++)
        {
            System.out.print("Teacher " + i + ": ");
            for(int j = 0; j< numClass; j++)
                if(x[j].getOldValue() == i)
                    System.out.print(j + " ");
            
            System.out.println();
        }
    }

    public static void main(String[] args) {
        bcta problem = new bcta();
        problem.balancedClassTeacherAssignment();
        problem.search();
        problem.printResult();
    }
}