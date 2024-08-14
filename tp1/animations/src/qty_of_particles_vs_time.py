import json

import matplotlib.pyplot as plt

from constants.FilePaths import FILE_TIMES, FILE_EXAMPLE


def read_times_file(file_path):
    N = []
    time = []

    with open(file_path, 'r') as file:
        for line in file:
            parts = line.split()
            N.append(float(parts[0]))
            time.append(float(parts[1]))

    return N, time


def plot_data(N, time, method):
    plt.figure(figsize=(10, 6))
    plt.plot(N, time, marker='o', linestyle='-', color='b')
    plt.title('N vs Tiempo (Método ' + method + ')')
    plt.xlabel('N')
    plt.ylabel('Tiempo (en ms)')
    plt.grid(True)
    plt.show()

N, time = read_times_file(FILE_TIMES)
with open(FILE_EXAMPLE, 'r') as f:
    config = json.load(f)

plot_data(N, time, config['method'])