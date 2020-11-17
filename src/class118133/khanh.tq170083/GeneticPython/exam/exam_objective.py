# author: Khanh.Quang 
# institute: Hanoi University of Science and Technology
# file name: exam_objective.py
# project name: GeneticPython
# date: 17/11/2020
from fitness import Fitness, SingleFitness
from objective import Objective
from exam.exam import data

def _get_overlap(s1, s2, position1, position2):
    x1 = s1[0]
    y1 = s1[1]
    r1 = s1[2]
    x2 = s2[0]
    y2 = s2[1]
    r2 = s2[2]
    return _check_overlap(x1, y1, r1, data[position1][0], data[position1][1],
                          x2, y2, r2, data[position2][0], data[position2][1])

def _check_overlap(x1, y1, r1, l1, w1, x2, y2, r2, l2, w2):
    if r1 == 0 and r2 == 0:
        fine = (x1 + w1 <= x2 or x2 + w2 <= x1 \
                 or y1 + l1 <= y2 or y2 + l2 <= y1)
        if not fine:
            width = min(x1 + w1, x2 + w2) - max(x1, x2)
            height = min(y1 + l1, y2 + l2) - max(y1, y2)
            return width * height

    if r1 == 0 and r2 == 1:
        fine = (x1 + w1 <= x2 or x2 + l2 <= x1 \
                 or y1 + l1 <= y2 or y2 + w2 <= y1)
        if not fine:
            width = min(x1 + w1, x2 + l2) - max(x1, x2)
            height = min(y1 + l1, y2 + w2) - max(y1, y2)
            return width * height

    if r1 == 1 and r2 == 0:
        fine = (x1 + l1 <= x2 or x2 + w2 <= x1 \
                or y1 + w1 <= y2 or y2 + l2 <= y1)
        if not fine:
            width = min(x1 + l1, x2 + w2) - max(x1, x2)
            height = min(y1 + w1, y2 + l2) - max(y1, y2)
            return width * height

    if r1 == 1 and r2 == 1:
        fine = (x1 + l1 <= x2 or x2 + l2 <= x1 \
                or y1 + w1 <= y2 or y2 + w2 <= y1)
        if not fine:
            width = min(x1 + l1, x2 + l2) - max(x1, x2)
            height = min(y1 + w1, y2 + w2) - max(y1, y2)
            return width * height

    return 0


class ExamObjective(Objective):
    @staticmethod
    def calculate_fitness(variables: list) -> Fitness:
        size = variables.__len__()
        overlap_square = 0
        for i in range(size-1):
            for j in range(i, size):
                overlap_square += _get_overlap(variables[i], variables[j], i, j)

        return SingleFitness(overlap_square)


