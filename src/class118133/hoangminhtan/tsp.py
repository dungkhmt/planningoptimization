from __future__ import print_function

import ortools
import numpy as np
from itertools import combinations, permutations
from ortools.linear_solver import pywraplp

weighted_matrix = [[0, 6, 1, 1],
                       [6, 0, 1, 1],
                       [1, 1, 0, 5],
                       [1, 1, 5, 0]]

def gen_data(n):
    res = np.random.randint(1, 100, (n, n), dtype=np.int32)
    for i in range(n):
        for j in range(n):
            if i == j:
                res[i][j] = 0
    return res

def main(weighted_matrix):
    n = len(weighted_matrix)
    solver = pywraplp.Solver.CreateSolver('integer_programming_example', 'CBC')
    res = [[None for i in range(n)] for j in range(n)]

    for i in range(n):
        for j in range(n):
            if i == j:
                res[i][j] = solver.IntVar(0, 0, 'x_' + str(i) + '_' + str(j))
            else:
                res[i][j] = solver.IntVar(0, 1, 'x_' + str(i) + '_' + str(j))

    # ct1
    ctx = [solver.Constraint(1, 1, 'ctx' + str(i)) for i in range(n)]
    cty = [solver.Constraint(1, 1, 'cty' + str(i)) for i in range(n)]
    for i in range(n):
        for j in range(n):
            ctx[i].SetCoefficient(res[i][j], 1)
            cty[j].SetCoefficient(res[i][j], 1)

    for m in range(1, n):
        for combi in combinations(range(n), m+1):
            if len(combi) < n:
                ct = solver.Constraint(0, m)
                # print(combi, '\n')
                for i, j in permutations(combi, 2):
                     ct.SetCoefficient(res[i][j], 1)

    objective = solver.Objective()
    for i in range(n):
        for j in range(n):
            objective.SetCoefficient(res[i][j], weighted_matrix[i][j])

    objective.SetMinimization()
    solver.Solve()
    print('Objective value =', objective.Value())
    prev = [-1 for i in range(n)]
    for i in range(n):
        for j in range(n):
            if int(res[i][j].solution_value()) == 1:
                print(i, j, weighted_matrix[i][j])


# weighted_matrix = gen_data(n=15).tolist()
main(weighted_matrix)
