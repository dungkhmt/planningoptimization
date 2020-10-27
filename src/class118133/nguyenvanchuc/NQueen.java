package class118133.nguyenvanchuc;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class NQueen {
    public static void main(String[] args) {
        int n=8;
        Model model= new Model("Queen");
        IntVar[] x = new IntVar[n];
        IntVar[] d1= new IntVar[n];
        IntVar[] d2= new IntVar[n];

        for (int i=0; i<n; i++){
            x[i]= model.intVar("X"+ i,1,n);
            d1[i]= model.intOffsetView(x[i], i);
            d2[i]= model.intOffsetView(x[i], -i);
        }

        model.allDifferent(x).post();
        model.allDifferent(d1).post();
        model.allDifferent(d2).post();

        model.getSolver().solve();

        for (int i=0; i< n; i++){
            System.out.println("x["+ i+ "] = " + x[i].getValue());
        }
//        -------------------------------------------------------------------------------
//        Model model = new Model("Example");
//        IntVar[] X = new IntVar[5];
//        for(int i = 0; i < 5; i++)
//            X[i] = model.intVar("X[" + i + "]",1,5);
//
//        model.arithm(model.intOffsetView(X[2],3),"!=",X[1]).post();
//        model.arithm(X[3], "<=", X[4]).post();
//        model.arithm(model.intOffsetView(X[0], 1),"=", X[2],"+",X[3]).post();
//        model.arithm(X[4], "<=", 3).post();
//        model.arithm(X[1],"+",X[4],"=",7).post();
//        model.ifThen(model.arithm(X[2], "=", 1), model.arithm(X[4], "!=", 2));
//
//        model.getSolver().solve();
//        for(int i = 0; i < 5; i++)
//            System.out.println(X[i]);
//------------------------------SUDOKU----------------------------------------------------------------
//        Model model= new Model("Sudoku");
//        IntVar[][] x= new IntVar[9][9];
//        for (int i=0; i<9; i++){
//            for (int j=0; j<9; j++){
//                x[i][j]= model.intVar("x["+i +","+ j+"]",1,9);
//            }
//        }
//
//        for (int i=0; i<9;i++){
//            for (int j1=0; j1<9; j1++){
//                for (int j2=j1+1; j2<9;j2++){
//                    model.arithm(x[i][j1], "!=", x[i][j2]).post();
//                    model.arithm(x[j1][i], "!=", x[j2][i]).post();
//                }
//            }
//        }
//
//        for (int I = 0; I < 3; I++)
//            for (int J = 0; J < 3; J++)
//                for (int i1 = 0; i1 < 3; i1++)
//                    for (int j1 = 0; j1 < 3; j1++)
//                        for (int i2 = 0; i2 < 3; i2++)
//                            for (int j2 = 0; j2 < 3; j2++)
//                                if (i1 < i2 || i1 == i2 && j1 < j2)
//                                    model.arithm(x[3 * I + i1][3 * J + j1],
//                                            "!=", x[3 * I + i2][3 * J + j2]).post();
//        model.getSolver().solve();
//        for(int i = 0; i < 9; i++){
//            for(int j = 0; j < 9; j++)
//                System.out.print(x[i][j].getValue() + " ");
//            System.out.println();
//        }

    }
}
