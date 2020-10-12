from __future__ import print_function

import ortools
import numpy as np
import queue
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


def load_data(dir):
    with open(dir) as f:
        weighted_matrix = f.read().split('\n')[1:]
    for i in range(len(weighted_matrix)):
        weighted_matrix[i] = list(map(int, weighted_matrix[i].split()))
    return weighted_matrix


def get_sub_cycle(adj_matrix, start):
    check = [False for _ in range(len(adj_matrix))]
    cycle = [start]
    check[start] = True
    q = queue.Queue()
    while not q.empty():
        u = q.get()
        for i in adj_matrix[u]:
            if not check[i] and i == 1:
                q.put(i)
                check[i] = True
                cycle.append(i)

    return cycle


def get_tour(res, start=0):
    tour = [start]
    prev = start
    while len(tour) < len(res):
        cur = np.argmax(res[prev])
        tour.append(cur)
        prev = cur

    tour.append(start)
    tour = np.array(tour) + 1

    return tour.tolist()


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

    sol = [[0 for i in range(n)] for j in range(n)]
    for i in range(n):
        for j in range(n):
            if int(res[i][j].solution_value()) == 1:
                print(i, j, weighted_matrix[i][j])
                sol[i][j] = 1
    # print(get_tour(sol, 0))

    return sol

# weighted_matrix = gen_data(n=15).tolist()
main(weighted_matrix)
