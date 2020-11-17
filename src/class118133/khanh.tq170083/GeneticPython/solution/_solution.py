# Author: Truong Quang Khanh
# Institute: Hanoi University of Science and Technology

from abc import abstractmethod, ABCMeta

from fitness import Fitness
from objective import Objective


class Solution(metaclass=ABCMeta):
    __chromosomes: list
    __fitness: Fitness

    @classmethod
    @abstractmethod
    def get_size(cls) -> int:
        pass

    @classmethod
    @abstractmethod
    def new_instance(cls):
        pass

    @abstractmethod
    def mutate(self):
        pass

    @abstractmethod
    def crossover(self, solution):
        pass

    @property
    def chromosomes(self):
        return self.__chromosomes

    @chromosomes.setter
    def chromosomes(self, chromosomes: list):
        chromes_size = self.get_size()
        if chromosomes.__len__() == chromes_size:
            self.__chromosomes = chromosomes
        else:
            print(chromes_size, chromosomes.__len__())
            raise Exception("Not appropriate size of chromosome!")

    @property
    def fitness(self) -> Fitness:
        return self.__fitness

    @fitness.setter
    def fitness(self, fitness: Fitness):
        self.__fitness = fitness

    def update(self, objective: Objective):
        self.fitness = objective.calculate_fitness(self.chromosomes)
