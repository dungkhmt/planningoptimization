import numpy as np
import random


def generate_solution(n=10):
    sol = np.arange(n)
    np.random.shuffle(sol)
    return sol


def count_errors(solution):
    counter = 0
    for i in range(len(solution)):
        for j in range(i+1, len(solution)):
            if solution[i] == solution[j]:
                counter += 1
            elif solution[i] + i == solution[j] + j:
                counter += 1
            elif solution[i] - i == solution[j] - j:
                counter += 1
    return counter


def improve(solution):
    n = len(solution)
    min_counter = count_errors(solution)
    mark = np.array([[min_counter] * n] * n)
    res = []
    for i in range(n):
        pos = solution[i]
        for j in range(n):
            solution[i] = j
            violation = count_errors(solution)
            mark[i, j] = violation
            min_counter = min(violation, min_counter)
        solution[i] = pos
    for i in range(n):
        for j in range(n):
            if mark[i][j] == min_counter:
                res.append([i, j])

    return random.choice(res), min_counter


def main(n=100):
    print('n=', n)
    solution = generate_solution(n)
    num_iter = 100
    min_counter = n**2
    is_optimized = False
    for i in range(num_iter):
        change, violation = improve(solution)
        solution[change[0]] = change[1]
        print('Number of iterations: {}, number of violations: {}'.format(i + 1, violation))
        min_counter = min(min_counter, violation)
        if min_counter == 0:
            is_optimized = True
            break
    if is_optimized:
        print('Solution: ',solution + 1)
    else:
        print('Can not find solution in {} iterations.'.format(num_iter))


main(n=40)
