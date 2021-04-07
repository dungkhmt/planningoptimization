import pycbls
N = 10
model = CBLSModel()
D = [i for i in range(N)] # or set

X = [VarIntLS(D) for i in range(N)]

c1 = model.AllDifferent(X)
c2 = model.LessOrEqual(X[1],X[5])
c3 = model.Equal(model.Sum(X),2*N)



searcher = HillClimbing(model)
searcher.search(10)
