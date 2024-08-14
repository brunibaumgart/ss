import json

import matplotlib.pyplot as plt

from constants.FilePaths import FILE_EXAMPLE, FILE_CIM_TIMES_VS_M, FILE_BFM_TIMES_VS_M


def read_times_file(file_path):
    M = []
    time = []

    with open(file_path, 'r') as file:
        next(file)
        for line in file:
            parts = line.split()
            M.append(float(parts[0]))
            time.append(float(parts[1]))

    return M, time


def plot_data(M, time_cim, time_bfm, n, l):
    plt.figure(figsize=(10, 6))
    plt.plot(M, time_cim, marker='o', linestyle='-', color='b', label='cim')
    plt.plot(M, time_bfm, marker='o', linestyle='-', color='r', label='bfm')
    plt.title('M vs Tiempo (N/L*L) = ' + str(n/(l*l)) + ')')
    plt.xlabel('M')
    plt.ylabel('Tiempo (en ms)')
    plt.legend(loc="upper right")
    plt.grid(True)
    plt.show()

M, time_cim = read_times_file(FILE_CIM_TIMES_VS_M)
M, time_bfm = read_times_file(FILE_BFM_TIMES_VS_M)

with open(FILE_EXAMPLE, 'r') as f:
    config = json.load(f)

plot_data(M, time_cim, time_bfm, config['n'], config['l'])