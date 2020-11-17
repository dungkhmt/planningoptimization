from __future__ import print_function
import sys
from ortools.sat.python import cp_model

def main(board_size):
    model = cp_model.CpModel()

    queens = [model.NewIntVar(0, board_size - 1, 'x%i' % i) for i in range(board_size)]
    
    model.AddAllDifferent(queens)

    diag1 = []
    diag2 = []
    for j in range (board_size):
      q1 = model.NewIntVar(0, 2 * board_size, 'diag1%i' %j)
      diag1.append(q1)
      model.Add(q1 == queens[j] + j)

      q2 = model.NewIntVar(-board_size, board_size, 'diag2%i' %j)
      diag2.append(q2)
      model.Add(q2 == queens[j] - j)

    print(diag1)
    model.AddAllDifferent(diag1)
    model.AddAllDifferent(diag2)

    solver = cp_model.CpSolver()
    status = solver.Solve(model)

    print('  - status          : %s' % solver.StatusName(status))
    for i in range (board_size):
      print('x%i = %i' %(i, solver.Value(queens[i])))


if __name__ == '__main__':
  # By default, solve the 8x8 problem.
  board_size = 200
  main(board_size)
