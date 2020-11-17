from __future__ import print_function
from ortools.sat.python import cp_model

model = cp_model.CpModel()

# N = 19
# W = 19
# H = 16 
W = 4
H = 6
N = 3
w = [3, 3, 1]
h = [2, 4, 6]
x = [0] * N 
y = [0] * N
o = [0] * N 
b = [0] * N
# w = [3 ,4 ,6 ,6 ,5 ,2 ,3 ,5 ,4 ,3 ,3 ,4 ,5 ,8 ,2 ,4 ,3 ,9 ,1]
# h = [5 ,4 ,2 ,3 ,4 ,6 ,5 ,5 ,3 ,3 ,6 ,2 ,5 ,4 ,6 ,4 ,2 ,2 ,6]
for i in range(N) : 
    x[i] = model.NewIntVar(0, W, 'x_%i' % i)
    y[i] = model.NewIntVar(0, H, 'y_%i' % i)
    o[i] = model.NewBoolVar('o_%i' % i)
 

L = 0 
def make_bool(cons) :
    global L 
    L += 1 
    bl = [model.NewBoolVar('b%i_%i' % (L, _) ) for _ in range(len(cons))] 
    for i in range(len(cons)) :
        model.Add(cons[i][0] ).OnlyEnforceIf(bl[i])
        model.Add(cons[i][1]).OnlyEnforceIf(bl[i].Not())
    return bl

for i in range(N) : 
    model.Add(x[i] + w[i] <= W).OnlyEnforceIf(o[i].Not())
    model.Add(y[i] + h[i] <= H).OnlyEnforceIf(o[i].Not())
    model.Add(x[i] + h[i] <= W).OnlyEnforceIf(o[i])
    model.Add(y[i] + w[i] <= H).OnlyEnforceIf(o[i])

for i in range(N-1) : 
    for j in range(i+1, N) :
        cons = [[x[i] + w[i] <= x[j], x[i] + w[i] > x[j]],
            [x[j] + w[j] <= x[i], x[j] + w[j] > x[i]],
            [y[i] + h[i] <= y[j], y[i] + h[i] > y[j]],
            [y[j] + h[j] <= y[i], y[j] + h[j] > y[i]]]
        model.Add(sum(make_bool(cons)) == 1).OnlyEnforceIf([o[i].Not(), o[j].Not()])

        cons = [[x[i] + w[i] <= x[j], x[i] + w[i] > x[j]],
            [x[j] + h[j] <= x[i], x[j] + h[j] > x[i]], 
            [y[i] + h[i] <= y[j], y[i] + h[i] > y[j]],
            [y[j] + w[j] <= y[i], y[j] + w[j] > y[i]]]
        model.Add(sum(make_bool(cons)) == 1).OnlyEnforceIf([o[i].Not(), o[j]])
                
        cons = [[x[i] + h[i] <= x[j], x[i] + h[i] > x[j]],
            [x[j] + w[j] <= x[i], x[j] + w[j] > x[i]],
            [y[i] + w[i] <= y[j], y[i] + w[i] > y[j]],
            [y[j] + h[j] <= y[i], y[j] + h[j] > y[i]]]
        model.Add(sum(make_bool(cons)) == 1).OnlyEnforceIf([o[i], o[j].Not()])

        cons = [[x[i] + h[i] <= x[j], x[i] + h[i] > x[j]], 
            [x[j] + h[j] <= x[i], x[j] + h[j] > x[i]],
            [y[i] + w[i] <= y[j], y[i] + w[i] > y[j]],
            [y[j] + w[j] <= y[i], y[j] + w[j] > y[i]]]
        model.Add(sum(make_bool(cons)) == 1).OnlyEnforceIf([o[i], o[j]])

solver = cp_model.CpSolver()
status = solver.Solve(model)
if status == cp_model.OPTIMAL : 
    for i in range(N) : 
        print(f"{i} : ({solver.Value(x[i])}, {solver.Value(y[i])})")








      