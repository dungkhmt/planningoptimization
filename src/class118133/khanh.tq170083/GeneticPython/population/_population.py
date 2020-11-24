# Author: Truong Quang Khanh
# Institute: Hanoi University of Science and Technology

from abc import ABCMeta, abstractmethod
from typing import List

from solution import Solution


class Population(metaclass=ABCMeta):
    __POP_SIZE: int
    __ITERATION: int
    __MUTATION_RATE: float
    __CROSSOVER_RATE: float

    __population: List[Solution]

    def __init__(self, pop_size: int,
                 iteration: int,
                 mutation_rate: float,
                 crossover_rate: float):
        self.__POP_SIZE = pop_size
        self.__ITERATION = iteration
        self.__MUTATION_RATE = mutation_rate
        self.__CROSSOVER_RATE = crossover_rate
        self.population = list()

    def get_config(self) -> dict:
        return {
            'POP_SIZE': self.__POP_SIZE,
            'ITERATION': self.__ITERATION,
            'MUTATION_RATE': self.__MUTATION_RATE,
            'CROSSOVER_RATE': self.__CROSSOVER_RATE
        }

    @abstractmethod
    def _initialize(self):
        pass

    @abstractmethod
    def _breed(self):
        pass

    @abstractmethod
    def _select_best_solutions(self):
        pass

    def solve(self):
        config = self.get_config()
        self._initialize()
        for _ in range(config["ITERATION"]):
            self._breed()
            self._select_best_solutions()
            # print(self.population[0].fitness.to_string())

    @property
    def population(self):
        return self.__population

    @population.setter
    def population(self, population: List[Solution]):
        self.__population = population
