# author: Khanh.Quang 
# institute: Hanoi University of Science and Technology
# file name: _single_fitness.py
# project name: GeneticPython
# date: 13/11/2020

from fitness._fitness import Fitness


class SingleFitness(Fitness):
    value: float

    def __init__(self, value):
        self.value = value

    @property
    def value(self):
        return self.__value
    
    @value.setter
    def value(self, value: float):
        self.__value = value

    def to_string(self):
        return str(self.value)
