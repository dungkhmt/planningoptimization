# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.

from solution import SimpleSolution
from population import SimplePopulation
from objective.single_objective.simple_obj1 import SimpleObj1


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    population = SimplePopulation(pop_size=100,
                                  iteration=1000,
                                  mutation_rate=0.1,
                                  crossover_rate=0.1,
                                  objective=SimpleObj1,
                                  solution_type=SimpleSolution)

    population.solve()
    print(population.population[0].fitness.to_string())
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
