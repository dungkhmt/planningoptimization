//package class118133.phamminhkhiem;
//
//import org.chocosolver.solver.Model;
//import org.chocosolver.solver.variables.IntVar;
//
//public class SUDOKU {
//    public static void main(String[] args) {
//        Model model = new Model("SODUKU_SOLVER");
//        int n =  9 ;
//        IntVar[] x = new IntVar[n] ;
//        IntVar[] x1 = new IntVar[n];
////        IntVar[] x2 = new IntVar[n] ;
//        for (int i = 0 ; i < n ; i++)
//            x[i] = model.intVar("x[" + i + "]", 1, n);
//        for (int i = 0 ; i < n ; i++) {
//            x1[i] = model.intOffsetView(x)
////            x1[i] = model.intOffsetView(x[i], i);
////            x2[i] = model.intOffsetView(x[i], -i);
//        }
//        model.allDifferent(x).post();
////        model.allDifferent(x1).post();
////        model.allDifferent(x2).post();
//        int count = 0 ;
//        while(model.getSolver().solve()) {
//            count ++ ;
//            System.out.println("Solution " +count +" : ");
//            for (int i = 0; i < n; i++) {
//                for (int j = 1; j <= n; j++) {
//                    if (x[i].getValue() == j)
//                        System.out.print(1 + "\t");
//                    else System.out.print(0 + "\t");
//                }
//                System.out.println("\n");
//            }
//        }
//    }
//}
