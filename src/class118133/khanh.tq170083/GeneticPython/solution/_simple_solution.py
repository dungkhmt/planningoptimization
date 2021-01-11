# author: Khanh.Quang 
# institute: Hanoi University of Science and Technology
# file name: _simple_solution.py
# project name: GeneticPython
# date: 13/11/2020

from solution._solution import Solution

import random


class SimpleSolution(Solution):
    __size = 2
    __upper_bound = 3
    __lower_bound = -3

    def __init__(self, variables: list):
        self.chromosomes = variables

    @classmethod
    def get_size(cls) -> int:
        return cls.__size

    @classmethod
    def new_instance(cls):
        random_var = list()
        for _ in range(SimpleSolution.__size):
            random_var.append(cls.__random_value())
        return cls(random_var)

    @staticmethod
    def __random_value():
        random_value = random.random() * \
                       (SimpleSolution.__upper_bound -
                        SimpleSolution.__lower_bound) + \
                       SimpleSolution.__lower_bound
        return random_value

    def mutate(self):
        for i in range(SimpleSolution.__size):
            random_value = SimpleSolution.__random_value()
            self.chromosomes[i] = random_value

    def crossover(self, solution):
        offspring1 = SimpleSolution([self.chromosomes[0], solution.chromosomes[1]])
        offspring2 = SimpleSolution([self.chromosomes[1], solution.chromosomes[0]])
        return offspring1, offspring2
