import ortools
from ortools.sat.python import cp_model


def main():
    model = cp_model.CpModel()
    x = [[model.NewIntVar(1, 9, 'x_' + str(i) + '_' + str(j)) for j in range(9)] for i in range(9)]

    for i in range(9):
        for j in range(9):
            for j1 in range(j+1, 9):
                model.Add(x[i][j1] != x[i][j])
                model.Add(x[j1][i] != x[j][i])

    for i in range(3):
        for j in range(3):
            for i1 in range(3):
                for j1 in range(3):
                    for i2 in range(3):
                        for j2 in range(3):
                            if i1 < i2 or (i1 == i2 and j1 < j2):
                                model.Add(x[3*i + i1][3*j + j1] != x[3*i + i2][3*j + j2])

    solver = cp_model.CpSolver()
    status = solver.Solve(model)
    # print(solver.StatusName(status))

    if status == cp_model.OPTIMAL:
        for i in x:
            print(' '.join([str(solver.Value(i[j])) for j in range(len(i))]))
    else:
        print('No solution!')


main()
