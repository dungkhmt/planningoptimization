from ortools.sat.python import cp_model

def read_data(file_path):
    data = {}
    with open(file_path, "r") as r:
        
        cnt = 0
        wl = []
        for line in r:
            line = line.split(" ")
            line = [int(x) for x in line]
            if (line[0] == -1): break
            if cnt == 0:
                data['W'] = line[0]
                data['L'] = line[1]
            else:
                wl.append((line[0], line[1]))
            cnt += 1
    data['wl'] = wl
    return data

def build_model(data):
    W = data['W']
    L = data['L']
    wl = data['wl']

    N = len(wl)

    model = cp_model.CpModel()
    
    x = []
    y = []
    o = []
    wi = []
    li = []
    variables = []
    x_intervals = []
    y_intervals = []

    for i in range(N):
        x.append(model.NewIntVar(0, W-1, 'x[%i]' % i))
        y.append(model.NewIntVar(0, L-1, 'y[%i]' % i))
        o.append(model.NewIntVar(0, 1, 'o[%i]' % i))
        o_w_i = model.NewIntVar(1, W, 'o_w_[%i]' % i)
        o_l_i = model.NewIntVar(1, L, 'o_l_[%i]' % i)
        corner_x_i = model.NewIntVar(1, W, 'corner_x[%i]' % i)
        corner_y_i = model.NewIntVar(1, L, 'corner_y[%i]' % i)

        wi.append(wl[i][0])
        li.append(wl[i][1])

        model.Add(  (o[i] == 0 and o_w_i == wi[i])
                    or (o[i] == 1 and o_w_i == li[i]))
        model.Add(  (o[i] == 0 and o_l_i == li[i])
                    or (o[i] == 1 and o_l_i == wi[i]))
                    

        variables.append(x[i])
        variables.append(y[i])
        variables.append(o[i])

        x_intervals.append(model.NewIntervalVar(x[i], o_w_i, corner_x_i, 'x_interval[%i]' % i))
        y_intervals.append(model.NewIntervalVar(y[i], o_l_i, corner_y_i, 'x_interval[%i]' % i))
    
    # for i in range(N):
    #     for j in range(i+1, N):
    #         x_cond = (o[i] == 0 and o[j] == 0 and (x[i] + wi[i] <= x[j] or x[j] + wi[j] <= x[i])) \
    #             or (o[i] == 1 and o[j] == 0 and (x[i] + li[i] <= x[j] or x[j] + wi[j] <= x[i])) \
    #             or (o[i] == 0 and o[j] == 1 and (x[i] + wi[i] <= x[j] or x[j] + li[j] <= x[i])) \
    #             or (o[i] == 1 and o[j] == 1 and (x[i] + li[i] <= x[j] or x[j] + li[j] <= x[i]))
    #         y_cond = (o[i] == 0 and o[j] == 0 and (y[i] + li[i] <= y[j] or y[j] + li[j] <= y[i])) \
    #             or (o[i] == 1 and o[j] == 0 and (y[i] + wi[i] <= y[j] or y[j] + li[j] <= y[i])) \
    #             or (o[i] == 0 and o[j] == 1 and (y[i] + li[i] <= y[j] or y[j] + wi[j] <= y[i])) \
    #             or (o[i] == 1 and o[j] == 1 and (y[i] + wi[i] <= y[j] or y[j] + wi[j] <= y[i]))
            
    #         model.Add((x_cond or y_cond))
    model.AddNoOverlap2D(x_intervals, y_intervals)

    return model, variables

class VarArraySolutionPrinter(cp_model.CpSolverSolutionCallback):

    def __init__(self, variables):
        cp_model.CpSolverSolutionCallback.__init__(self)
        self.__variables = variables
        self.__solution_count = 0

    def on_solution_callback(self):
        self.__solution_count += 1
        for v in self.__variables:
            print('%s=%i' % (v, self.Value(v)), end=' ')
        print()
        self.StopSearch()

    def solution_count(self):
        return self.__solution_count
        
        
def solve(data):
    model, variables = build_model(data)
    solver = cp_model.CpSolver()
    solution_printer = VarArraySolutionPrinter(variables)
    status = solver.SearchForAllSolutions(model, solution_printer)
    if solution_printer.solution_count() == 0:
        print("Imposible")



if __name__ == '__main__':
    fn = 'D:\\Dev\\University\\20201\\Optimize and Plan\\planningoptimization\\data\\BinPacking2D\\bin-packing-2D-W19-H24-I21.txt'
    # fn = 'test.txt'
    
    data = read_data(fn)
    print(data)
    solve(data)