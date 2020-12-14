# Author: Truong Quang Khanh
# Institute: Hanoi University of Science and Technology

from interface import Interface

from fitness import Fitness


class Objective(Interface):
    @staticmethod
    def calculate_fitness(variables: list) -> Fitness:
        """

        :rtype: object
        """
        pass
