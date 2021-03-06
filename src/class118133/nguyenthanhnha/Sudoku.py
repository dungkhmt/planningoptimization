from ortools.sat.python import cp_model

def Solver():
    model = cp_model.CpModel()

    IntVar = [[model.NewIntVar(1,9,'x%s%s'%(i,j)) for j in range(9)] for i in range(9)]
    
    for i in range(9):
        for j1 in range(9):
            for j2 in range(j1+1,9):
                model.Add(IntVar[i][j1]!=IntVar[i][j2])
                model.Add(IntVar[j1][i]!=IntVar[j2][i])

    
    for i in range(3):
        for j in range(3):
            for i1 in range(3):
                for j1 in range(3):
                    for i2 in range(3):
                        for j2 in range(3):
                            if (i1<i2) or (i1==i2 and j1<j2):
                                  model.Add(IntVar[3*i + i1][3*j + j1] != IntVar[3*i + i2][3*j + j2])

    solver = cp_model.CpSolver()
    status = solver.Solve(model)


    if status == cp_model.OPTIMAL:
        for i in IntVar:
            print(' '.join([str(solver.Value(i[j])) for j in range(len(i))]))
    else:
        print('No solution!')
if __name__ == "__main__":
    sol = Solver()