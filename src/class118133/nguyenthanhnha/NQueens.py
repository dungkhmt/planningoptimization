from ortools.sat.python import cp_model

def main(board_size):
    model = cp_model.CpModel()
    queens = [model.NewIntVar(0,board_size-1,'queen_'+str(i)) for i in range(board_size)]
    model.AddAllDifferent(queens)

    diag1 = [model.NewIntVar(0,2*board_size,'diag1_'+str(i)) for i in range(board_size)]
    diag2 = [model.NewIntVar(-board_size,board_size,'diag2_'+str(i)) for i in range(board_size)]

    for i in range(board_size):
        model.Add(diag1[i] == queens[i] + i)
        model.Add(diag2[i] == queens[i] - i)
        
    model.AddAllDifferent(diag1)
    model.AddAllDifferent(diag2)

    solver = cp_model.CpSolver()
    status = solver.Solve(model)
    if status == cp_model.OPTIMAL:
        print('Solution for board size  = {}:'.format(board_size))
        for i in range(board_size):
            print("queens[{}] = {}".format(i, solver.Value(queens[i])))
    else:
            print('No solution!')

main(8)

    

