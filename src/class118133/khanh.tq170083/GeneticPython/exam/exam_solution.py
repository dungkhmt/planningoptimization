# author: Khanh.Quang 
# institute: Hanoi University of Science and Technology
# file name: exam_solution.py
# project name: GeneticPython
# date: 17/11/2020

from solution import Solution

import random


class ExamSolution(Solution):
    __size: int
    __upper_bound: float
    __lower_bound: float

    def __init__(self, chromosomes=None):
        if chromosomes is None:
            chromosomes = list()
            for _ in range(ExamSolution.get_size()):
                chromosomes.append([ExamSolution._random_value(),
                                    ExamSolution._random_value(),
                                    random.randint(0, 1)])
        self.chromosomes = chromosomes

    @classmethod
    def get_size(cls) -> int:
        return cls.__size

    @classmethod
    def new_instance(cls):
        return ExamSolution()

    @classmethod
    def setup(cls, size, lower_bound, upper_bound):
        cls.__size = size
        cls.__lower_bound = lower_bound
        cls.__upper_bound = upper_bound

    def mutate(self):
        mutated_index = self.__get_random_index()
        self.chromosomes[mutated_index] = [ExamSolution._random_value(),
                                           ExamSolution._random_value(),
                                           random.randint(0, 1)]

    def crossover(self, solution):
        crossover_index = self.__get_random_index()
        chromosome1 = self.chromosomes[:crossover_index] \
            .extend(solution.chromosomes[crossover_index:])
        chromosome2 = solution.chromosomes[:crossover_index] \
            .extend(self.chromosomes[crossover_index:])
        return ExamSolution(chromosome1), ExamSolution(chromosome2)

    def __get_random_index(self):
        return random.randint(0, self.get_size() - 1)

    @classmethod
    def _random_value(cls):
        return random.random() * (cls.__upper_bound - cls.__lower_bound) \
               + cls.__lower_bound
