"""
tsp by IP
"""

from ortools.sat.python import cp_model
import numpy as np

class DiagramPrinter(cp_model.CpSolverSolutionCallback):
  def __init__(self, variables):
    cp_model.CpSolverSolutionCallback.__init__(self)
    self.__variables = variables
    self.__solution_count = 0

  def OnSolutionCallback(self):
    self.__solution_count += 1

    for v in self.__variables:
      queen_col = int(self.Value(v))
      board_size = len(self.__variables)
      # Print row with queen.
      for j in range(board_size):
        if j == queen_col:
          # There is a queen in column j, row i.
          print("Q", end=" ")
        else:
          print("_", end=" ")
      print()
    print()

  def SolutionCount(self):
    return self.__solution_count



class NQueenSolver:
    def __init__(self, num_queens=8):
        self.model = cp_model.CpModel()
        self.num_queens = num_queens
        self.solver = cp_model.CpSolver()
        self.printer = None
    def __len__(self):
        return self.num_queens
    
        
    def solve(self):
        self.set_variables()
        self.set_constraints()
        
        status = self.solver.SearchForAllSolutions(self.model, self.printer)
        
        print()
        print('Solutions found: ', self.printer.SolutionCount())
         
    def set_variables(self):
        """
        x_i=j means the i-th queen is in row i column j
        """
        n = len(self)

        self.variables = [self.model.NewIntVar(0, n-1, f"x{i}") for i in range(n)]
        self.printer = DiagramPrinter(self.variables)
        
    def set_constraints(self):
        model = self.model
        variables = self.variables
        n = len(self)
        
        # not in the same column
        model.AddAllDifferent(variables)
        
        # not in the same diagonal

        diag1 = []
        diag2 = []
        for j in range(n):
            d1 = model.NewIntVar(0,2*n, f"diag1_{j}")
            model.Add(d1 == variables[j] + j)
            diag1.append(d1)
            
            d2 = model.NewIntVar(-n, n, f"diag2_{j}")
            model.Add(d2 == variables[j] - j)
            diag2.append(d2)
        model.AddAllDifferent(diag1)
        model.AddAllDifferent(diag2)
        


if __name__ == "__main__":
    num_queens = 6
    solver = NQueenSolver(num_queens)
    solver.solve()


