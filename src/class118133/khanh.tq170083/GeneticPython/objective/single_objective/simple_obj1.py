# author: Khanh.Quang 
# institute: Hanoi University of Science and Technology
# file name: simple_obj1.py
# project name: GeneticPython
# date: 13/11/2020
from fitness import SingleFitness, Fitness
from objective.objective import Objective


class SimpleObj1(Objective):
    @staticmethod
    def calculate_fitness(variables: list) -> Fitness:
        """
        implements function x^2 + y^2
        :param self:
        :param variables:
        :return:
        """

        x = variables[0]
        y = variables[1]
        return SingleFitness(x ** 2 + y ** 2)
