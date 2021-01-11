# author: Khanh.Quang 
# institute: Hanoi University of Science and Technology
# file name: exam.py
# project name: GeneticPython
# date: 17/11/2020

def read_data(file_path):
    data = []
    with open(file_path) as file:
        text = file.read()
        text_list = list(map(lambda x: x.split(' '), text.split('\n')))
        for t in text_list:
            data.append(list(map(lambda x: int(x), t)))
    return data


file_path = 'data/data1.txt'
data = read_data(file_path)
