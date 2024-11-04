import matplotlib.pyplot as plt

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

plt.errorbar(nj_values, mean_distance_values, yerr=std_distance_values, fmt='o-', color='b')

plt.xlabel(r"$N_j$", fontsize=16)
plt.ylabel("Distancia máxima recorrida (m)", fontsize=16)

plt.xlim(10, max(nj_values) + 1)
plt.xticks(nj_values, fontsize=12)
plt.yticks(fontsize=12)

plt.tick_params(axis='both', which='major', width=1.5, length=7)

plt.subplots_adjust(bottom=0.2)

plt.grid()
plt.show()
