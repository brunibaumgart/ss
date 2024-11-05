import matplotlib.pyplot as plt
import numpy as np
from src.constants import FilePaths

file_path = FilePaths.SIMULATIONS_DIR + 'max_distance_vs_nj.txt'
nj_values = []
mean_distance_values = []
std_distance_values = []

with open(file_path, 'r') as file:
    for line in file:
        nj, mean_distance, std_distance = map(float, line.split())
        nj_values.append(nj)
        mean_distance_values.append(mean_distance)
        std_distance_values.append(std_distance)

mean_distance_values = np.array(mean_distance_values)
std_distance_values = np.array(std_distance_values)

lower_errors = std_distance_values
upper_errors = std_distance_values.copy()

max_value = 100
for i in range(len(upper_errors)):
    if mean_distance_values[i] + upper_errors[i] > max_value:
        upper_errors[i] = max_value - mean_distance_values[i]
        if upper_errors[i] < 0:
            upper_errors[i] = 0

margin = 0.05
y_min = 0 - (max_value * margin)
y_max = max_value + (max_value * margin)

plt.errorbar(nj_values, mean_distance_values,
             yerr=[lower_errors, upper_errors],
             fmt='o-', color='b',
             capsize=5)

plt.xlabel(r"$N_j$", fontsize=16)
plt.ylabel(r"$x_M$", fontsize=16)
plt.xlim(0, max(nj_values) + 1)
plt.ylim(y_min, y_max)
plt.xticks(nj_values, fontsize=12)
plt.yticks(fontsize=12)
plt.tick_params(axis='both', which='major', width=1.5, length=7)
plt.subplots_adjust(bottom=0.2)
plt.grid()
plt.show()