import matplotlib.pyplot as plt

from src.constants import FilePaths

file_path = FilePaths.SIMULATIONS_DIR + "tries_vs_nj.txt"
nj_values = []
fraction_values = []

with open(file_path, 'r') as file:
    for line in file:
        nj, fraction = map(float, line.split())
        nj_values.append(nj)
        fraction_values.append(fraction)

plt.plot(nj_values, fraction_values, marker='o', linestyle='-', color='b')

plt.xlim(0, 101)
plt.xticks(nj_values, fontsize=12)
plt.yticks(fontsize=12)

plt.xlabel(r"$N_j$", fontsize=14)
plt.ylabel(r"$\varphi_t$", fontsize=16)

plt.subplots_adjust(bottom=0.15)

plt.grid()
plt.show()
