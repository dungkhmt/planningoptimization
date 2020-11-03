from __future__ import print_function
import string
import numbers
import time
from os import getenvb
from time import sleep


from ortools.linear_solver import pywraplp
from ortools.linear_solver.pywraplp import Objective, Solver

def create_data_model():
    try:
        f = open("./data/TSP/tsp-100.txt")

        data = {}
        a = f.readline()

        data['number_of_cities'] = int(a)
        data['distances'] = []
        for i in range(data['number_of_cities']):
            data['distances'].append([])
            
            s = f.readline()
            s = s.split(' ')

            for j in (s):
                if(j.isdigit()):
                    data['distances'][i].append(int(j))
    finally:
        f.close()

    return data

def main():
    data = create_data_model()

    solver = pywraplp.Solver.CreateSolver('TSP', 'CBC')
    
    #create variables
    x = []
    for i in range (data['number_of_cities']):
        x.append([])
        for j in range (data['number_of_cities']):
            if i == j:
                x[i].append(solver.IntVar(0, 0, 'x_%i_%i' % (i, j)))
            else:
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
            constraint.SetCoefficient(x[j][i], 1)

    '''#eliminate SEC
    generatorSEC = GeneratorSEC(data['number_of_cities'])
    
    while generatorSEC.size < data['number_of_cities']:
        generatorSEC.next()
        if (1 <= generatorSEC.size & generatorSEC.size < data['number_of_cities']):
            constraint = solver.Constraint(0, generatorSEC.size - 1)

            for i in range (data['number_of_cities']):
                if generatorSEC.SEC[i] != 0:
                    for j in range (data['number_of_cities']):
                        if generatorSEC.SEC[j] != 0:
                            constraint.SetCoefficient(x[i][j], 1)'''

    solution = []
    SEC = []
    count = 1
    cur = 0
    #Add SEC constraint
    while(1):
        status = solver.Solve()
        
        solution.clear()
        SEC.clear()
    
        #print('Objective value: ', solver.Objective().Value())
        #print('Solution: 0', end=' ')

        count = 1
        cur = 0
        solution.append(cur)
        while(count < data['number_of_cities']):
            for i in range (data['number_of_cities']):
                if(x[cur][i].solution_value() == 1):
                    #print(i, end=' ')
                    cur = i
                    solution.append(cur)
                    break
        
            count = count + 1

        #print() 

        #for i in range (data['number_of_cities']):
        #    for j in range (data['number_of_cities']):
        #        print('%i' %x[i][j].solution_value(), end = ' ')
        #    print()
        
        #print() 

        if(exist_SEC_constraint(solution, SEC) == False):
            break
        else:
            constraint = solver.Constraint(0, len(SEC) - 1)
            for p in SEC:
                for q in SEC:
                    constraint.SetCoefficient(x[p][q], 1)

    #status = solver.Solve()

    count = 1
    cur = 0
    solution.append(cur)
    print('Objective value: ', solver.Objective().Value())
    print('Solution: 0', end=' ')
    while(count < data['number_of_cities']):
        for i in range (data['number_of_cities']):
            if(x[cur][i].solution_value() == 1):
                print(i, end=' ')
                cur = i
                solution.append(cur)
                break
        
        count = count + 1

    print() 

    for i in range (data['number_of_cities']):
        for j in range (data['number_of_cities']):
            print('%i' %x[i][j].solution_value(), end = ' ')
        print()
        
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

def exist_SEC_constraint(solution, SEC):
    mark = [-1] * len(solution)
    for i in range (len(solution)):
        if(mark[solution[i]] != -1):
            j = i
            while(mark[solution[i]] != j):
                j -= 1
                SEC.append(solution[j])
            return True
        
        mark[solution[i]] = i

    return False

if __name__ == '__main__':
    start_time = time.time()
    main()
    print("--- %s seconds ---" % (time.time() - start_time))
