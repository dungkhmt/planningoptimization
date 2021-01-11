from ortools.sat.python import cp_model
import numpy as np


class DiagramPrinter(cp_model.CpSolverSolutionCallback):
    def __init__(self, xs,ys,zs, temp, temp2=None):
        cp_model.CpSolverSolutionCallback.__init__(self)
        self.__xs, self.__ys, self.__zs = xs,ys,zs
        self.__temp = temp
        # self.__temp2 = temp2
        self.__solution_count = 0

    def OnSolutionCallback(self):
        n = len(self.__xs)
        self.__solution_count += 1

        for x,y,z in zip(self.__xs, self.__ys, self.__zs, ):
            print(self.Value(x),self.Value(y), self.Value(z))
        
        # for i in range(n-1):
        #     for j in range(i+1, n):
        #         val = [self.Value(x) for x in self.__temp[i][j]]
        #         print(f"{i}, {j}: ", val)
        # print()
        # for i in range(n):
        #     val = [self.Value(x) for x in self.__temp2[i]]
        #     print(f"{i}: ", val)
        print()
        
    def SolutionCount(self):
        return self.__solution_count
    
def decode(line):
    return [int(x) for x in line.split(" ")]

def read_input(path):
    boxes = []
    with open(path, "r") as f:
        container = decode(f.readline())
        while True:
            x = decode(f.readline())
            if len(x) > 1:
                boxes.append(x)
            else:
                break
    return container, boxes

def solve(contrainer, boxes):
    """
    scale the boxes by 2
    x,y center of the box
    z=1: orignal position
    z=0: rotate 90
    """
    model = cp_model.CpModel()
    solver = cp_model.CpSolver()
    x_s, y_s, z_s = [], [], []
    w_s = []
    h_s = []
    
    W, H = container[0], container[1]
    w_max = max([x[0] for x in boxes])
    h_max = max([x[1] for x in boxes])
    size_max = max(H,W)
    # print(W,H)
    n = len(boxes)
    # temp = [[[] for i in range(n)] for j in range(n)]
    # temp2 = [[] for i in range(n)]
    # define variable
    for i, (w, h) in enumerate(boxes):
        x_s.append(model.NewIntVar(0,W-w, f"x{i}"))
        # x_div = model.NewIntVar(-1,1,f"x_div{i}") 
        # model.AddModuloEquality(x_div, x_s[-1], 2)
        # model.Add(x_div == w%2)
        
        y_s.append(model.NewIntVar(0,H-h, f"y{i}"))
        # y_div = model.NewIntVar(-1,1,f"y_div{i}") 
        # model.AddModuloEquality(y_div, y_s[-1], 2)
        # model.Add(y_div == h%2)
        
        zi = model.NewIntVar(0,1, f"z{i}")
        z_s.append(zi)
        
        # define w
        w_var = model.NewIntVar(min(w,h), max(w,h), f"w{i}")
        model.Add(w_var == (w-h)*zi + h)
        w_s.append(w_var)
        
        # define h
        h_var = model.NewIntVar(min(w,h), max(w,h), f"h{i}")
        model.Add(h_var == (h-w)*zi + w)
        h_s.append(h_var)
        # temp2[i].extend([x_div, y_div, w_var, h_var])
    # define constraint
    for i in range(n-1): 
        x1, y1, w1, h1 = x_s[i], y_s[i], w_s[i], h_s[i]
        for j in range(i+1, n):
            x2, y2, w2, h2 = x_s[j], y_s[j], w_s[j], h_s[j]
            
            # w contraint
            w_dis_abs = model.NewIntVar(0,2*size_max, f"w_dis_abs{i}{j}")
            w_dis = model.NewIntVar(-2*size_max,2*size_max, f"w_dis{i}{j}")
            model.Add(w_dis == 2*x2+w2-2*x1-w1)
            model.AddAbsEquality(w_dis_abs, w_dis)
            
            h_dis_abs = model.NewIntVar(0,2*size_max, f"h_dis_abs{i}{j}")
            h_dis = model.NewIntVar(-2*size_max,2*size_max, f"h_dis{i}{j}")
            model.Add(h_dis == 2*y2+h2-2*y1-h1)
            model.AddAbsEquality(h_dis_abs, h_dis)
            
            w_bool = model.NewBoolVar(f"wbool{i}{j}")
            h_bool = model.NewBoolVar(f"hbool{i}{j}")
            
            # temp[i][j].extend([w_dis, w_dis_abs, w_bool, h_dis, h_dis_abs, h_bool])
            model.Add(w_dis_abs >= w1+w2).OnlyEnforceIf(w_bool)
            model.Add(h_dis_abs >= h1+h2).OnlyEnforceIf(h_bool)
            model.AddBoolOr([
                w_bool, h_bool
            ])

    printer = DiagramPrinter(x_s, y_s, z_s)
    status = solver.SearchForAllSolutions(model, printer)
    print('Solutions found: ', printer.SolutionCount())

if __name__ == "__main__":
    container, boxes = read_input("/mnt/DATA/learning_stuffs/uni/20201/scheduling_optimization/planningoptimization/data/BinPacking2D/bin-packing-2D-W19-H24-I21.txt")
    print(container, boxes)
    solve(container, boxes)
    