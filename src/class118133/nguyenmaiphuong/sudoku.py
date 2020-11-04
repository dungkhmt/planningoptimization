from __future__ import print_function
from ortools.sat.python import cp_model
from ortools.sat.python.cp_model import INFEASIBLE, OPTIMAL

def create_data_model():
    data = {}
    data['board_size'] = 9
    data['numbers'] = [
        [5, 3, None, None, 7, None, None, None, None],
        [6, None, None, 1, 9, 5, None, None, None],
        [None, 9, 8, None, None, None, None, 6, None],
        [8, None, None, None, 6, None, None, None, 3],
        [4, None, None, 8, None, 3, None, None, 1],
        [7, None, None, None, 2, None, None, None, 6],
        [None, 6, None, None, None, None, 2, 8, None],
        [None, None, None, 4, 1, 9, None, None, 5],
        [None, None, None, None, 8, None, None, 7, 9]
    ]
    data['block'] = [
        [0, 0, 0, 1, 1, 1, 2, 2, 2],
        [0, 0, 0, 1, 1, 1, 2, 2, 2],
        [0, 0, 0, 1, 1, 1, 2, 2, 2],
        [3, 3, 3, 4, 4, 4, 5, 5, 5],
        [3, 3, 3, 4, 4, 4, 5, 5, 5],
        [3, 3, 3, 4, 4, 4, 5, 5, 5],
        [6, 6, 6, 7, 7, 7, 8, 8, 8],
        [6, 6, 6, 7, 7, 7, 8, 8, 8],
        [6, 6, 6, 7, 7, 7, 8, 8, 8],
    ]
    return data

def main():
    """
    main function
    """
    data = create_data_model()

    model = cp_model.CpModel()

    x = []
    row = []
    column = []
    block = []

    for i in range(data['board_size']):
        column.append([])
        block.append([])

    for i in range(data['board_size']):
        x.append([])
        row.append([])
        for j in range(data['board_size']):
            if (data['numbers'][i][j] == None):
                x[i].append(model.NewIntVar(1, data['board_size'], 'x%i' %i + '%i' %j))
                row[i].append(x[i][j]) 
                column[j].append(x[i][j])
                block[data['block'][i][j]].append(x[i][j]) 
            else:
                x[i].append(model.NewIntVar(data['numbers'][i][j], data['numbers'][i][j], 'x%i' %i + '%i' %j))
                row[i].append(x[i][j]) 
                column[j].append(x[i][j])
                block[data['block'][i][j]].append(x[i][j]) 

    for i in range(data['board_size']):
        model.AddAllDifferent(row[i])
        model.AddAllDifferent(column[i])
        model.AddAllDifferent(block[i])

    solver = cp_model.CpSolver()
    status = solver.Solve(model)

    if status != INFEASIBLE:
        print('  - status          : %s' % solver.StatusName(status))
        for i in range(data['board_size']):
            for j in range(data['board_size']):
                print('| %i |' %(solver.Value(x[i][j])), end = '')
            
            print()

if __name__ == "__main__":
    main()

    