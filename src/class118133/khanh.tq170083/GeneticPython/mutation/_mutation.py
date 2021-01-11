# author: Khanh.Quang 
# institute: Hanoi University of Science and Technology
# file name: _mutation.py
# project name: GeneticPython
# date: 13/11/2020

from solution import Solution

import random


def random_mutate(s: Solution):
    position = random.randint(0, s.get_size())
    mutated_chromosomes = random.random()
