"""
tsp by IP
"""

from ortools.sat.python import cp_model
from itertools import combinations
import numpy as np


class Printer(cp_model.CpSolverSolutionCallback):
  def __init__(self, variables):
    cp_model.CpSolverSolutionCallback.__init__(self)
    self.__variables = variables
    self.__solution_count = 0

  def OnSolutionCallback(self):
    self.__solution_count += 1
    n = len(self.__variables)
    for i in range(n):
        for j in range(n):

            v = int(self.Value(self.__variables[i][j]))
            
            print(v, end=" ")
        print()
    print("\n\n")

  def SolutionCount(self):
    return self.__solution_count

def read_data(path):
    pass




distance_matrix = [
    [0,1,3,6,7],
    [4,0,2,1,5],
    [6,6,0,3,2],
    [5,1,8,0,7],
    [4,2,2,1,0]
]
n = len(distance_matrix)

model = cp_model.CpModel()

solver = cp_model.CpSolver()

variables = [[0 for i in range(n)] for j in range(n)]
for i in range(n):
    for j in range(n):
        variables[i][j] = model.NewIntVar(0, 1, f"x{i}_{j}")
temp = []

printer = Printer(variables)
# sum x_*j = 1 balance condition

for j in range(n):
    sum_col_j = model.NewIntVar(0,n, f"col_{j}")
    model.Add(sum_col_j == cp_model.LinearExpr.Sum([
        variables[i][j] for i in range(n)]))
    model.Add(sum_col_j == 1)
    temp.append(sum_col_j)

        

# sum x_i* = 1 balance condition
for i in range(n):
    sum_row_i = model.NewIntVar(0,n, f"row_{i}")
    model.Add(sum_row_i == cp_model.LinearExpr.Sum([
        variables[i][j] for j in range(n)]))
    model.Add(sum_row_i == 1)
    temp.append(sum_row_i)

# no sub-cycle
for k in range(2,n):
    k_subsets = combinations(range(n), k)
    for l, subset in enumerate(k_subsets):
        sum_kl_set = model.NewIntVar(0,k-1, f"{k}-subset_{l}")
        subset_vars = [variables[i][j] for i,j in combinations(subset,2)]
        model.Add(sum_kl_set == cp_model.LinearExpr.Sum(subset_vars))

# objective
obj = model.NewIntVar(0,1000, f"obj")
obj_expression = cp_model.LinearExpr.ScalProd(
    [variables[i][j] for i in range(n) for j in range(n)],
    [distance_matrix[i][j] for i in range(n) for j in range(n)],
    )
model.Add(obj == obj_expression)
model.Minimize(obj) 
status = solver.Solve(model)
print(status == cp_model.OPTIMAL)
print(solver.ObjectiveValue())
