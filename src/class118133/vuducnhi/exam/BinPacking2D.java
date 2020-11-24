package class118133.vuducnhi.exam;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BinPacking2D {
    int containerWidth;
    int containerLength;
    int numBin;
    ArrayList<Integer> binsWidth;
    ArrayList<Integer> binsLength;

    void inputData() {
        try {
            File file = new File("data/BinPacking2D/bin-packing-2D-W10-H7-I6.txt");
            Scanner reader = new Scanner(file);
            containerWidth = reader.nextInt();
            containerLength = reader.nextInt();
            binsWidth = new ArrayList<Integer>();
            binsLength = new ArrayList<Integer>();
            while (reader.hasNextInt()) {
                int w = reader.nextInt();
                if (w == -1) break;
                int l = reader.nextInt();
                binsWidth.add(w);
                binsLength.add(l);
            }
            numBin = binsWidth.size();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    void solve() {
        Model model = new Model("BinPacking2D");
        IntVar[] X = model.intVarArray("X_", numBin, 0, containerWidth - 1);
        IntVar[] Y = model.intVarArray("Y_", numBin, 0, containerLength - 1);
        BoolVar[] O = model.boolVarArray("O_", numBin);

        for (int i = 0; i < numBin; ++i) {
            O[i].eq(0).imp(X[i].add(binsWidth.get(i)).le(containerWidth).and(Y[i].add(binsLength.get(i)).le(containerLength))).post();
            O[i].eq(1).imp(X[i].add(binsWidth.get(i)).le(containerWidth).and(Y[i].add(binsLength.get(i)).le(containerLength))).post();
        }

        for (int i = 0; i < numBin - 1; ++i) {
            for (int j = i + 1; j < numBin; ++j) {
                (O[i].eq(0).and(O[j].eq(0))).imp(X[i].add(binsWidth.get(i)).le(X[j]).or(X[j].add(binsWidth.get(j)).le(X[i])).or(Y[i].add(binsLength.get(i)).le(Y[j]).or(Y[j].add(binsLength.get(j)).le(Y[i])))).post();
                (O[i].eq(0).and(O[j].eq(1))).imp(X[i].add(binsWidth.get(i)).le(X[j]).or(X[j].add(binsLength.get(j)).le(X[i])).or(Y[i].add(binsLength.get(i)).le(Y[j]).or(Y[j].add(binsWidth.get(j)).le(Y[i])))).post();
                (O[i].eq(1).and(O[j].eq(0))).imp(X[i].add(binsLength.get(i)).le(X[j]).or(X[j].add(binsWidth.get(j)).le(X[i])).or(Y[i].add(binsWidth.get(i)).le(Y[j]).or(Y[j].add(binsLength.get(j)).le(Y[i])))).post();
                (O[i].eq(1).and(O[j].eq(1))).imp(X[i].add(binsLength.get(i)).le(X[j]).or(X[j].add(binsLength.get(j)).le(X[i])).or(Y[i].add(binsWidth.get(i)).le(Y[j]).or(Y[j].add(binsWidth.get(j)).le(Y[i])))).post();
            }
        }

        Solver solver = model.getSolver();
        Solution solution = solver.findSolution();

        if (solution.exists()) {
            for (int i = 0; i < numBin; ++i) {
                String rotatedOrNot = (O[i].getValue() == 0 ? "'s rotated" : " doesn't rotate");
                System.out.println("Bin " + i + " at position (" + X[i].getValue() + ", " + Y[i].getValue() + ") and it" + rotatedOrNot);
            }
        } else {
            System.out.println("No solution existed");
        }
    }

//    void otherArithModel() {
//        Model model = new Model("BinPacking2D");
//        IntVar[] X = model.intVarArray("X_", numBin, 0, containerWidth - 1);
//        IntVar[] Y = model.intVarArray("Y_", numBin, 0, containerLength - 1);
//        IntVar[][] C = model.intVarMatrix("C_", numBin, 2, 0, 1);
//        for (int i = 0; i < numBin; ++i) {
//
//        }
//    }

    public static void main(String[] args) {
        BinPacking2D solver = new BinPacking2D();
        solver.inputData();
        solver.solve();
    }
}
