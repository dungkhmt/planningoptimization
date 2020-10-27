import sys
from ortools.sat.python import cp_model

def main(board_size):
    model = cp_model.CpModel()

    # Variables x[i] = j mean queen in row i, column j
    x = [
        model.NewIntVar(0, board_size - 1, f"x{i}")
        for i in range(board_size)
    ]

    # Add constraint all queens are in different rows
    model.AddAllDifferent(x)

    # Add constraint no two queens on the same diagonal
    diag1 = []
    diag2 = []
    for i in range(board_size):
        d1 = model.NewIntVar(0, 2 * board_size, f"diag1_{i}")
        diag1.append(d1)
        model.Add(d1 == x[i] + i)

        d2 = model.NewIntVar(-board_size, board_size, f"diag2_{i}")
        diag2.append(d2)
        model.Add(d2 == x[i] - i)

    model.AddAllDifferent(diag1)
    model.AddAllDifferent(diag2)

    # Solve model
    solver = cp_model.CpSolver()
    solution_printer = SolutionPrinter(x)
    solver.SearchForAllSolutions(model, solution_printer)
    print()
    print("Num of solutions: ", solution_printer.SolutionCount())

class SolutionPrinter(cp_model.CpSolverSolutionCallback):
    def __init__(self, variables):
        cp_model.CpSolverSolutionCallback.__init__(self)
        self.__variables = variables
        self.__solution_count = 0
        self.__board_size = len(variables)

    def OnSolutionCallback(self):
        self.__solution_count += 1
        
        for v in self.__variables:
            queen_col = int(self.Value(v))
            for j in range(self.__board_size):
                if j == queen_col:
                    print("Q", end=" ")
                else:
                    print("_", end=" ")
            print()
        print()

    def SolutionCount(self):
        return self.__solution_count
    
if __name__ == "__main__":
    board_size = 8
    if len(sys.argv) > 1:
        board_size = int(sys.argv[1])
    main(board_size)


