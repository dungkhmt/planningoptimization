import ortools
from ortools.sat.python import cp_model


def main(n):
    model = cp_model.CpModel()
    x = [model.NewIntVar(1, n, 'x_' + str(i)) for i in range(1, n+1)]
    d1 = [model.NewIntVar(cp_model.INT32_MIN, cp_model.INT32_MAX, 'd1_' + str(i)) for i in range(1, n+1)]
    d2 = [model.NewIntVar(cp_model.INT32_MIN, cp_model.INT32_MAX, 'd2_' + str(i)) for i in range(1, n+1)]

    for i in range(n):
        model.Add(d1[i] == x[i] + i)
        model.Add(d2[i] == x[i] - i)

    model.AddAllDifferent(x)
    model.AddAllDifferent(d1)
    model.AddAllDifferent(d2)

    solver = cp_model.CpSolver()
    status = solver.Solve(model)
    # print(solver.StatusName(status))

    if status == cp_model.OPTIMAL:
        print('Solution for n = {}:'.format(n))
        for i in range(n):
            print("x[{}] = {}".format(i, solver.Value(x[i])))
    else:
        print('No solution!')


main(20)