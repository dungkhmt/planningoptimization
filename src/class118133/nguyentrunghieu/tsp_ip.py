"""
tsp by IP
"""

from ortools.linear_solver import pywraplp
from itertools import combinations
import numpy as np

distance_matrix = [
    [0,3,1,1],
    [4,0,2,1],
    [1,2,0,6],
    [7,1,1,0],
]
n = len(distance_matrix)


solver = pywraplp.Solver.CreateSolver("simple_mip_program", "CBC")

variables = [
    solver.IntVar(0, 1, f"x{i}_{j}")
    for i in range(n)
    for j in range(n)
]

print("number of variables: ", solver.NumVariables())

# sum x_*j = 1 balance condition
for j in range(n):
    ct = solver.Constraint(1,1)
    print("\n\n")
    for k, var in enumerate(variables):
        coeff = int(k%n == j)
        print(coeff)
        ct.SetCoefficient(var, coeff)
        

# sum x_i* = 1 balance condition
for i in range(n):
    ct = solver.Constraint(1,1)
    print("\n\n")
    for k, var in enumerate(variables):
        coeff = int(k//n == i)
        print(coeff)
        ct.SetCoefficient(var, coeff)

# no sub-cycle
for k in range(2,n):
    k_subsets = combinations(range(n), k)
    for subset in k_subsets:
        ct = solver.Constraint(0,k-1)
        coeff = [0]*(n**2)
        for i,j in combinations(subset,2):
            coeff[i*n+j] = 1
            coeff[j*n+i] = 1
        print(subset)
        print(np.array(coeff).reshape(4,4), "\n")
        for _k, var in enumerate(variables):
            ct.SetCoefficient(var, coeff[_k])

print("number of constraints: ", solver.NumConstraints())

objective = solver.Objective()
max_cost = max([max(x) for x in distance_matrix])
for ind, var in enumerate(variables):
    i = ind//n
    j = ind%n
    coeff = distance_matrix[i][j]
    if i==j:
        coeff = max_cost+1
    print(coeff)
    objective.SetCoefficient(var, coeff)

objective.SetMinimization()

# solve the problem
solver.Solve()
print('Solution:')
print('Objective value =', objective.Value())
for var in variables:
    print(int(var.solution_value()))