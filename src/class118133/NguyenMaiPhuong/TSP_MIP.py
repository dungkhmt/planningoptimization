from __future__ import print_function
import numbers
from os import getenvb
from time import sleep

from ortools.linear_solver import pywraplp
from ortools.linear_solver.pywraplp import Objective

def create_data_model():
    data = {}

    data['number_of_cities'] = 10
    data['distances'] = [
        [0, 6, 2, 8, 8, 7, 4, 1, 1, 5],
        [3, 0, 4, 10, 8, 10, 7, 6, 2, 7],
        [10, 7, 0, 10, 9, 4, 7, 2, 8, 6],
        [7, 4, 4, 0, 4, 3, 3, 7, 8, 5], 
        [8, 2, 10, 2, 0, 7, 10, 2, 2, 4], 
        [3, 10, 3, 2, 4, 0, 1, 2, 4, 7], 
        [6, 5, 9, 7, 6, 1, 0, 2, 4, 7], 
        [7, 8, 9, 2, 4, 3, 8, 0, 9, 1], 
        [3, 4, 6, 8, 7, 9, 4, 6, 0, 3], 
        [8, 8, 6, 4, 5, 3, 3, 5, 1, 0], 
    ]

    return data

def main():
    data = create_data_model()
    
    solver = pywraplp.Solver.CreateSolver('TSP', 'CBC')
    
    #create variables
    x = []
    for i in range (data['number_of_cities']):
        x.append([])
        for j in range (data['number_of_cities']):
            x[i].append(solver.IntVar(0, 1, 'x_%i_%i' % (i, j)))
    
    #set objective
    objective = solver.Objective()
    for i in range (data['number_of_cities']):
        for j in range (data['number_of_cities']):
            objective.SetCoefficient(x[i][j], data['distances'][i][j])
    objective.SetMinimization()

    #set constraints
    for i in range (data['number_of_cities']):
        constraint = solver.Constraint(1, 1)
        for j in range (data['number_of_cities']):
            constraint.SetCoefficient(x[j][i], 1)

    for j in range (data['number_of_cities']):
        constraint = solver.Constraint(1, 1)
        for i in range (data['number_of_cities']):
            constraint.SetCoefficient(x[i][j], 1)

    #eliminate SEC
    generatorSEC = GeneratorSEC(data['number_of_cities'])
    
    while generatorSEC.size < data['number_of_cities']:
        generatorSEC.next()
        if (1 <= generatorSEC.size & generatorSEC.size < data['number_of_cities']):
            constraint = solver.Constraint(0, generatorSEC.size - 1)

            for i in range (data['number_of_cities']):
                if generatorSEC.SEC[i] != 0:
                    for j in range (data['number_of_cities']):
                        if generatorSEC.SEC[j] != 0:
                            constraint.SetCoefficient(x[i][j], 1)

    status = solver.Solve()
    
    print('Objective value: ', solver.Objective().Value())
    print('Solution: 0', end=' ')

    count = 1
    cur = 0
    while(count < data['number_of_cities']):
        for i in range (data['number_of_cities']):
            if(x[cur][i].solution_value() == 1):
                print(i, end=' ')
                cur = i
                break
        
        count = count + 1

    print()

    for i in range (data['number_of_cities']):
        for j in range (data['number_of_cities']):
            print('%i' %x[i][j].solution_value(), end = ' ')
        print()

class GeneratorSEC():
    SEC = []
    number_of_cities = 0
    size = 0

    def __init__(self, number_of_cities):
        self.number_of_cities = number_of_cities
        for i in range (number_of_cities):
            self.SEC.append(0)
    
    def next(self):
        for i in range (self.number_of_cities-1, -1, -1):
            if (self.SEC[i] == 0):
                self.SEC[i] = 1
                for j in range (i+1, self.number_of_cities):
                    self.SEC[j] = 0
                break

        self.size = 0
        for i in range (self.number_of_cities):
            if self.SEC[i] == 1:
                self.size += 1
        
        return self.SEC

if __name__ == '__main__':
    main()