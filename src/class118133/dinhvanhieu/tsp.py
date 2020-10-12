import ortools
from ortools.linear_solver import pywraplp
import argparse
from itertools import chain, combinations


def get_weight():
    """
    Get path file data from args and load
    """
    # Get path file data
    parser = argparse.ArgumentParser()
    parser.add_argument("--data_path", help="Path to file data.txt")
    args = parser.parse_args()

    # Load data
    try:
        with open(args.data_path, "r") as f:
            data = f.read().split("\n")
            n = int(data[0])
            weight_matrix = data[1:]
        weight_matrix = [
            list(map(int, weight_matrix[i].split())) for i in range(n)
        ]
    except Exception as e:
        print(e)
    return weight_matrix


def main(weight_matrix):
    """
    #####################################
    """
    n = len(weight_matrix)

    # Init solver
    solver = pywraplp.Solver.CreateSolver("integer_programming_example", "CBC")

    # Set variabels
    # x[i][j] = 1 if from i go to j else 0
    # Each x[i][j] is 0 or 1
    x = [[None for i in range(n)] for j in range(n)]
    for i in range(n):
        for j in range(n):
            if i != j:
                x[i][j] = solver.IntVar(0, 1, f"x_{i}_{j}")
            else:
                x[i][j] = solver.IntVar(0, 0, f"x_{i}_{j}")

    # Constraint sum of each row and columns is 1
    for i in range(n):
        rowx = solver.Constraint(1, 1)
        rowy = solver.Constraint(1, 1)
        for j in range(n):
            rowx.SetCoefficient(x[i][j], 1)
            rowy.SetCoefficient(x[j][i], 1)

    # Constraint not SEC
    all_subsets = chain.from_iterable(
        combinations(range(n), r) for r in range(2, n - 1)
    )
    for subset in all_subsets:
        sum = solver.Constraint(0, len(subset) - 1)
        for i in subset:
            for j in subset:
                sum.SetCoefficient(x[i][j], 1)

    # Create objective
    objective = solver.Objective()
    for i in range(n):
        for j in range(n):
            objective.SetCoefficient(x[i][j], weight_matrix[i][j])

    objective.SetMinimization()

    # Call solve function
    solver.Solve()

    # Print result
    print("Objective value =", objective.Value())

    for i in range(n):
        for j in range(n):
            if int(x[i][j].solution_value()) == 1:
                print(i, j, weight_matrix[i][j])


if __name__ == "__main__":
    main(get_weight())
