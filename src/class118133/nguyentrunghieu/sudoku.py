from ortools.sat.python import cp_model
import numpy as np

class DiagramPrinter(cp_model.CpSolverSolutionCallback):
    def __init__(self, variables):
        cp_model.CpSolverSolutionCallback.__init__(self)
        self.__variables = variables
        # print(variables)
        self.__solution_count = 0

    def OnSolutionCallback(self):
        self.__solution_count += 1
        variables = []
        for i in range(9):
            variables.append([int(self.Value(x)) for x in self.__variables[9*i:9*i+9]])
            
        for r_id, row in enumerate(variables):
            if r_id % 3 == 0:
                print("|-------"*3 + "|")
                
            for c_id, var in enumerate(row):
                value = var
                if c_id % 3 == 0:
                    print("|", end=" ")

                if value:
                    print(f"{value}", end=" ")
                else:
                    print("_", end=" ")
                
            print("|")
        print("|-------"*3 + "|")
        print()

    def SolutionCount(self):
        return self.__solution_count



class Sudoku:
    def __init__(self, inputs=None):
        self.model = cp_model.CpModel()
        self.solver = cp_model.CpSolver()
        self.set_variables(inputs)
        self.set_constraints()
    
    def solve(self):        
        status = self.solver.SearchForAllSolutions(self.model, self.printer)
        
        print()
        print('Solutions found: ', self.printer.SolutionCount())
         
    def set_variables(self, inputs=None):
        """
        """
        if inputs is not None:
            variables = [[0 for i in range(9)] for j in range(9)]
            for i in range(9):
                for j in range(9):
                    x = int(inputs[i,j])
                    if x != 0:
                        variables[i][j] = self.model.NewIntVar(x, x, f"x{i}_{j}")
                    else:
                        variables[i][j] = self.model.NewIntVar(1, 9, f"x{i}_{j}")
        else:
            variables = [
                [self.model.NewIntVar(1, 9, f"x{i}_{j}") for j in range(9)]
                for i in range(9)
            ]
        self.variables = variables
        self.printer = DiagramPrinter([variables[i][j] for i in range(9) for j in range(9)])
        
    def set_constraints(self):
        model = self.model
        variables = self.variables
        
        # column, row different
        for i in range(9):
            model.AddAllDifferent([variables[i][j] for j in range(9)])
            model.AddAllDifferent([variables[j][i] for j in range(9)])
        
        for i in range(3):
            for j in range(3):
                model.AddAllDifferent([
                    variables[i*3+k][j*3+l] for k in range(3) for l in range(3)])


        


if __name__ == "__main__":
    # _inputs = np.zeros((9,9), dtype=np.int32)
    # for i in range(9):
    #     _inputs[i] = (np.arange(9)+i) % 9 + 1
    # inputs = np.zeros((9,9), dtype=np.int32)
    # for j in range(3): 
    #     for i in range(3):
    #         inputs[3*i+j] = _inputs[3*j+i]
    # inputs[0,0] = 0
    # inputs[8,8] = 0
    
    inputs = np.array([
        [8,0,0,0,0,0,0,0,0],
        [0,0,3,6,0,0,0,0,0],
        [0,7,0,0,9,0,2,0,0],
        [0,5,0,0,0,7,0,0,0],
        [0,0,0,0,4,5,7,0,0],
        [0,0,0,1,0,0,0,3,0],
        [0,0,1,0,0,0,0,6,8],
        [0,0,8,5,0,0,0,1,0],
        [0,9,0,0,0,0,4,0,0]
    ]).astype(np.int32)
    print("inputs")
    print(inputs)
    print("\nsolution: ")
    solver = Sudoku(inputs)
    solver.solve()

