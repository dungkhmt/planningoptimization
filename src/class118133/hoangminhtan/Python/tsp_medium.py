from __future__ import print_function

import ortools
import numpy as np
import os
import queue
from itertools import combinations, permutations
from ortools.linear_solver import pywraplp
import time

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
        weighted_matrix = f.read().split('\n')[1:-1]
        # print(weighted_matrix)
    for i in range(len(weighted_matrix)):
        weighted_matrix[i] = list(map(int, weighted_matrix[i].split()))
    return weighted_matrix


def get_sub_cycle(adj_matrix, start):
    check = [False for _ in range(len(adj_matrix))]
    cycle = [start]
    check[start] = True
    q = queue.Queue()
    q.put(start)
    while not q.empty():
        u = q.get()
        # print(u)
        for i in range(len(adj_matrix[u])):
            if not check[i] and adj_matrix[u][i] == 1:
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


def create_solver(weighted_matrix):
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

    objective = solver.Objective()
    # print(n)
    for i in range(n):
        for j in range(n):
            # print(i, j)
            objective.SetCoefficient(res[i][j], weighted_matrix[i][j])

    objective.SetMinimization()
    return solver, objective, res


def add_subcycle_constraints(solver, res, constraints):
    # n = len(res)
    for subcycle in constraints:
        ct = solver.Constraint(0, len(subcycle)-1)
        for i, j in permutations(subcycle, 2):
            ct.SetCoefficient(res[i][j], 1)


def convert(res):
    n = len(res)
    sol = [[0 for i in range(n)] for j in range(n)]
    for i in range(n):
        for j in range(n):
            if int(res[i][j].solution_value()) == 1:
                # print(i, j, weighted_matrix[i][j])
                sol[i][j] = 1
    return sol


def main(weighted_matrix):
    n = len(weighted_matrix)
    constraints = []
    subcycle = []
    solver, objective, res = create_solver(weighted_matrix)
    while True:
        # solver, objective, res = create_solver(weighted_matrix)
        # add_subcycle_constraints(solver, res, constraints)
        add_subcycle_constraints(solver, res, subcycle)
        solver.Solve()
        sol = convert(res)
        subcycle = [-1]
        for i in range(n):
            subcycle = get_sub_cycle(sol, i)
            if len(subcycle) > 1:
                break

        # print(len(subcycle), subcycle)
        # constraints.append(subcycle)

        if len(subcycle) == n:
            return objective.Value(), sol
        subcycle = [subcycle]


# weighted_matrix = gen_data(n=15).tolist()'
data_dir = '../../.././data/TSP'
# data_dir = 'TSP'
for i in os.listdir(data_dir):
    weighted_matrix = load_data(os.path.join(data_dir, i))
    start = time.time()
    objective, sol = main(weighted_matrix)
    print('File : {},\t\tObjective Value: {} \nTime: {} \nSolution: {}\n'.format(i, objective, time.time()-start, get_tour(sol, 0)))
