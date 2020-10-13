"""
tsp by IP
"""

from ortools.linear_solver import pywraplp
from itertools import combinations
import numpy as np

class TSPSolver:
    def __init__(self, distance=None):
        self.solver = pywraplp.Solver.CreateSolver(
            "simple_mip_program", "CBC")
        self.addtional_constraints = []
        self.distance = distance
        
    def __len__(self):
        return len(self.distance)
    
    def reset(self):
        # self.solver.Clear()
        self.set_variables()
        self.set_base_constraints()
        # print("number of variables: ", self.solver.NumVariables())
        # print("number of constraints: ", self.solver.NumConstraints())
        self.set_objective()
        
    def solve(self):
        self.reset()

        n = len(self)
        # for k in range(2,n):
        #     k_subsets = combinations(range(n), k)
        #     self.set_additional_constraints(k_subsets)
        # self.set_objective()
        while True:
            self.solver.Solve()
            # print("number of variables: ", self.solver.NumVariables())
            print("number of constraints: ", self.solver.NumConstraints())
            self.display()
            
            path = self.get_cycle()
            print("a cycle length:", len(path))
            if len(path) < n:
                self.set_single_constraint(path)
            else:
                break
        
    def display_constraints(self):
        
        n = len(self)
        constraints = self.solver.constraints()
        # infos = []
        for constraint in constraints:
            info = {
                "Ub": constraint.Ub(),
                "Lb": constraint.Lb(),
            }
            for i in range(n):
                for j in range(n):
                    if i != j:
                        var = self.variables[i][j]
                        info[var.name()] = constraint.GetCoefficient(var)
            print(info)
    
    def display(self, obj_only=True):
        n = len(self)
        print('Objective value =', self.objective.Value())
        if not obj_only:
            for i in range(n):
                for j in range(n):
                    if i != j:
                        print(f"{self.variables[i][j].name()}: "
                            f"{self.variables[i][j].solution_value()}", end=" ")
                print()
                    
    def set_variables(self):
        n = len(self)
        variables = [[0 for i in range(n)] for j in range(n)]

        self.variables = [
            [self.solver.IntVar(0, 1, f"x{i}_{j}") if i != j else 0
            for j in range(n)]
            for i in range(n)
        ]
        
    def display_variables(self):
        names = []
        n = len(self)
        for i in range(n):
            for j in range(n):
                if i != j:
                    names.append(self.variables[i][j].name())
        print(names)
        
    def set_base_constraints(self):
        solver = self.solver
        variables = self.variables
        n = len(self)
        
        # sum x_*j = 1
        for j in range(n):
            ct = solver.Constraint(1,1)
            for i in range(n):
                if i != j:
                    ct.SetCoefficient(variables[i][j], 1)
                    
        # sum x_i* = 1
        for i in range(n):
            ct = solver.Constraint(1,1)
            for j in range(n):
                if i != j:
                    ct.SetCoefficient(variables[i][j], 1)

    def set_additional_constraints(self, constraints=None):
        
        if constraints is None:
            constraints = self.addtional_constraints
            
        if len(constraints) == 0:
            return

        for constraint in constraints:
            self.set_single_constraint(constraint)
                
    def set_single_constraint(self, constraint):
        
        assert len(constraint) >= 2
        solver = self.solver
        
        ct = solver.Constraint(0,len(constraint)-1)
        for i,j in combinations(constraint,2):
            ct.SetCoefficient(self.variables[i][j], 1)
            ct.SetCoefficient(self.variables[j][i], 1)
            
    def set_objective(self):
        obj = self.solver.Objective()
        n = len(self)
        
        for i in range(n):
            for j in range(n):
                if i != j:
                    obj.SetCoefficient(
                        self.variables[i][j], self.distance[i][j])
        
        obj.SetMinimization()
        self.objective = obj
    
    def get_cycle(self):
        """
        find cycle from solution
        """
        n = len(self)
        solution = np.array([
            [
                self.variables[i][j].solution_value() if i != j else 0
                for j in range(n)
            ] for i in range(n)
        ]).astype(np.int32)

        path = [0]
        current = 0
        while True:
            next = int(np.where(solution[current] == 1)[0])
            if next != path[0]:
                path.append(next)
                current = next
            else: 
                break
            # break
            # print(path)
        return path

    def load(self, path):
        with open(path, "r") as f:
            data = f.readlines()
        data = [[int(x) for x in line.split()] for line in data[1:]]
        self.distance = data
                    
    

if __name__ == "__main__":
    folder = "/mnt/DATA/learning_stuffs/uni/20201/scheduling_optimization/planningoptimization"
    solver = TSPSolver()
    solver.load(folder+"/data/TSP/tsp-100.txt")
    solver.solve()
    # solver.display()
    # print(solver.get_cycle())