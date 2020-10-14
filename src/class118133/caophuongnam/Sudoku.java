package class118133.caophuongnam;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Sudoku {
    public static void main(String[] args) {
        Model model = new Model("Sudoku");

        IntVar[][] table = new IntVar[9][9];
        for (int i=0; i<9; ++i) {
            for (int j=0; j<9; ++j) {
                table[i][j] = model.intVar("table["+i+"]["+j+"]", 1, 9);
            }
        }

        for (int i=0; i<9; ++i) {
            model.allDifferent(table[i]).post();
            for (int j1=0; j1<9; ++j1) {
                for (int j2=j1+1; j2<9; ++j2) {
                    model.arithm(table[j1][i], "!=", table[j2][i]).post();
                }
            }
        }

        for (int r=0; r<9; r+=3) {
            for (int c=0; c<9; c+=3) {
                for (int i1=r; i1<r+3; ++i1) {
                    for (int j1=c; j1<c+3; ++j1) {
                        for (int i2=r; i2<r+3; ++i2) {
                            for (int j2=c; j2<c+3; ++j2) if(i1!=i2 || j1!=j2) {
                                model.arithm(table[i1][j1], "!=", table[i2][j2]).post();
                            }
                        }
                    }
                }
            }
        }

        boolean hasResult = model.getSolver().solve();
        if (hasResult) {
            for (int i=0; i<9; ++i) {
                for (int j=0; j<9; ++j) {
                    System.out.print(table[i][j].getValue()+ " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("No Solution!");
        }
    }
}
