import json
import numpy as np
import matplotlib.pyplot as plt
from constants.FilePaths import FILE_EXAMPLE, FOLDER_CIM_TIMES_VS_M, FOLDER_BFM_TIMES_VS_M


def read_times_files(folder_path, iterations):
    data = {}

    for i in range(0, iterations):
        file_path = folder_path + "output_" + str(i) + ".txt"
        with open(file_path, 'r') as file:
            next(file)
            for line in file:
                parts = line.split()
                M_value = float(parts[0])
                time_value = float(parts[1])

                if M_value not in data:
                    data[M_value] = []

                data[M_value].append(time_value)

    return data


def compute_mean_and_error(data):
    M = sorted(data.keys())
    mean_times = []
    error_bars = []

    for m in M:
        mean_times.append(np.mean(data[m]))
        error_bars.append(np.std(data[m]))

    return M, mean_times, error_bars


def plot_data_with_error_bars(M_cim, time_cim, error_cim, M_bfm, time_bfm, error_bfm, n, l, rc):
    plt.figure(figsize=(10, 6))
    plt.errorbar(M_cim, time_cim, yerr=error_cim, marker='o', linestyle='-', color='b', label='cim', capsize=5)
    #plt.errorbar(M_bfm, time_bfm, yerr=error_bfm, marker='o', linestyle='-', color='r', label='bfm', capsize=5)
    plt.title(f'M vs Tiempo (N/L*L = {n / (l * l):.2f}, RC = {rc:.2f})')
    plt.xlabel('M')
    plt.ylabel('Tiempo (en ms)')
    plt.legend(loc="upper right")
    plt.grid(True)
    plt.show()


with open(FILE_EXAMPLE, 'r') as f:
    config = json.load(f)

data_cim = read_times_files(FOLDER_CIM_TIMES_VS_M, config['plotTimeVsMIterations'])
#data_bfm = read_times_files(FOLDER_BFM_TIMES_VS_M, config['plotTimeVsMIterations'])

M_cim, mean_time_cim, error_cim = compute_mean_and_error(data_cim)
#M_bfm, mean_time_bfm, error_bfm = compute_mean_and_error(data_bfm)

plot_data_with_error_bars(M_cim, mean_time_cim, error_cim, 0, 0, 0,
                          config['n'], config['l'], config['rc'])
