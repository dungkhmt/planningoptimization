from ortools.sat.python import cp_model
from sys import argv


def read_input_file(filepath):
    f = open(filepath)
    data = {}
    line = f.readline()
    parts = line.split(' ')
    data['w'] = int(parts[0])
    data['l'] = int(parts[1])
    item = []
    while True:
        line = f.readline()
        parts = line.split(' ')
        if (int(parts[0]) == -1):
            break
        item.append({'w': int(parts[0]), 'l': int(parts[1])})
    data['bins'] = item
    data['bin_cnt'] = len(item)
    return data


def generate_model(data):
    w = data['w']
    l = data['l']
    bin_cnt = data['bin_cnt']
    model = cp_model.CpModel()
    o = []
    x = []
    y = []
    w_i = []
    l_i = []
    interval_x = []
    interval_y = []
    points = []
    for i in range(bin_cnt):
        w_i.append(data['bins'][i]['w'])
        l_i.append(data['bins'][i]['l'])
        o.append(model.NewIntVar(0, 1, 'o[%i]' % i))
        x.append(model.NewIntVar(0, w - 1, 'x[%i]' % i))
        y.append(model.NewIntVar(0, l - 1, 'y[%i]' % i))
        o_w_i = model.NewIntVar(1, w, 'o_w[%i]' % i)
        o_l_i = model.NewIntVar(1, l, 'o_l[%i]' % i)
        corner_x_i = model.NewIntVar(1, w, 'corner_x[%i]' % i)
        corner_y_i = model.NewIntVar(1, l, 'corner_y[%i]' % i)
        model.Add((o[i] == 0 and o_w_i == w_i[i])
                  or (o[i] == 1 and o_w_i == l_i[i]))
        model.Add((o[i] == 0 and o_l_i == l_i[i])
                  or (o[i] == 1 and o_l_i == w_i[i]))
        interval_x.append(model.NewIntervalVar(
            x[i], o_w_i, corner_x_i, 'interval_x[%i]' % i))
        interval_y.append(model.NewIntervalVar(
            y[i], o_l_i, corner_y_i, 'interval_y[%i]' % i))
        points.append([o[i], x[i], y[i]])
    model.AddNoOverlap2D(interval_x, interval_y)
    return model, points


class VarArraySolutionPrinter(cp_model.CpSolverSolutionCallback):
    def __init__(self, points):
        cp_model.CpSolverSolutionCallback.__init__(self)
        self.points = points
        self.__solution_count = 0

    def on_solution_callback(self):
        self.__solution_count += 1
        for p in self.points:
            for v in p:
                print('%s=%s' % (v, self.Value(v)), end=' ')
            print()
        self.StopSearch()

    def solution_count(self):
        return self.__solution_count


def solve(model, variables):
    solver = cp_model.CpSolver()
    solution_printer = VarArraySolutionPrinter(variables)
    status = solver.SearchForAllSolutions(model, solution_printer)
    print('Num solution:', solution_printer.solution_count())


data = read_input_file(argv[1])
model, variables = generate_model(data)
solve(model, variables)