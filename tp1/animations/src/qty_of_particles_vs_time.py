import json

import matplotlib.pyplot as plt

from constants.FilePaths import FILE_EXAMPLE, FILE_CIM_TIMES_VS_N, FILE_BFM_TIMES_VS_N


def read_times_file(file_path):
    N = []
    time = []

    with open(file_path, 'r') as file:
        next(file)
        for line in file:
            parts = line.split()
            N.append(float(parts[0]))
            time.append(float(parts[1]))

    return N, time


def plot_data(N, time_cim, time_bfm, l):
    plt.figure(figsize=(10, 6))
    plt.plot(N, time_cim, marker='o', linestyle='-', color='b', label='cim')
    plt.plot(N, time_bfm, marker='o', linestyle='-', color='r', label='bfm')
    plt.title('N vs Tiempo (L = '+ str(l) + ')')
    plt.xlabel('N')
    plt.ylabel('Tiempo (en ms)')
    plt.legend(loc="upper right")
    plt.grid(True)
    plt.show()

N, time_cim = read_times_file(FILE_CIM_TIMES_VS_N)
N, time_bfm = read_times_file(FILE_BFM_TIMES_VS_N)

with open(FILE_EXAMPLE, 'r') as f:
    config = json.load(f)

plot_data(N, time_cim, time_bfm, config['l'])