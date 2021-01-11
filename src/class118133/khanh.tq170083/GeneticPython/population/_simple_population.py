# Author: Truong Quang Khanh
# Institute: Hanoi University of Science and Technology

import random
from overrides import overrides

from objective import Objective
from solution import Solution
from population._population import Population


class SimplePopulation(Population):
    __OBJECTIVE: Objective
    __SOLUTION_TYPE: type(Solution)
    __config: dict

    def __init__(self, pop_size: int,
                 iteration: int,
                 mutation_rate: float,
                 crossover_rate: float,
                 objective: Objective,
                 solution_type):
        super().__init__(pop_size,
                         iteration,
                         mutation_rate,
                         crossover_rate)
        self.__OBJECTIVE = objective
        self.__SOLUTION_TYPE = solution_type
        self.__config = super().get_config()
        self.__config.update({'OBJECTIVE': self.__OBJECTIVE})

    @overrides
    def get_config(self) -> dict:
        return self.__config

    def _initialize(self):
        config = self.get_config()
        pop_size = config["POP_SIZE"]
        for i in range(pop_size):
            new_solution = self.__SOLUTION_TYPE.new_instance()
            new_solution.update(self.__OBJECTIVE)
            self.population.append(new_solution)

    def _breed(self):
        # crossover
        new_population = list()
        pop_size = self.get_config()["POP_SIZE"]
        for i in range(pop_size):
            ind1 = random.randint(0, pop_size-1)
            ind2 = ind1
            while ind2 == ind1:
                ind2 = random.randint(0, pop_size-1)
            (offspring1, offspring2) = \
                self.population[ind1].crossover(self.population[ind2])
            offspring1.update(self.__OBJECTIVE)
            offspring2.update(self.__OBJECTIVE)
            new_population.extend([offspring1, offspring2])
        self.population.extend(new_population)

        # mutate
        for solution in self.population:
            if random.random() < self.get_config()["MUTATION_RATE"]:
                solution.mutate()

    def _select_best_solutions(self):
        def fitness(solution: Solution):
            return solution.fitness.value
        self.population = sorted(self.population, key=fitness)
        self.population = self.population[:self.get_config()["POP_SIZE"]]
