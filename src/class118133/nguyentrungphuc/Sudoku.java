import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Sudoku {
    public static void main(String[] args) {
        Model model = new Model("Sudoku");
        int N = 9;
        IntVar[][] X = new IntVar[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                X[i][j] = model.intVar("X[" + i + "]", 1, N);
            }
        }

        int grid = N / 3;
        IntVar[] row = new IntVar[N];
        IntVar[] col = new IntVar[N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                row[j]= X[i][j];
                col[j]=X[j][i];
            }
            model.allDifferent(row).post();
            model.allDifferent(col).post();
        }
        IntVar[] tab=new IntVar[N];
        for(int i=0;i<grid;i++){
            for(int j=0;j<grid;j++){
                int count =0;
                for(int k=0;k<3;k++){
                    for(int l=0;l<3;l++){
                        tab[count]=X[i*3+k][j*3+l];
                        count++;
                    }
                }
                model.allDifferent(tab).post();
            }
        }

        model.getSolver().solve();
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                System.out.print(X[i][j].getValue()+" ");
            }
            System.out.println();
        }
    }
}
