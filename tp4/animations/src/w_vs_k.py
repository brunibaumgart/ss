import matplotlib.pyplot as plt
import os

from src.constants import FilePaths

k_values = [100, 1000, 2500, 5000, 10000]
w_max_values = []

base_filename = FilePaths.SIMULATIONS_DIR + 'amplitude_vs_w_k_'

for k in k_values:
    filename = f"{base_filename}{k}.txt"
    ws = []

    with open(filename, 'r') as file:
        next(file)
        for line in file:
            _, w = map(float, line.split())
            ws.append(w)

    w_max_values.append(max(ws))

plt.scatter(k_values, w_max_values, marker='o')

plt.xlabel(r'$k$ ($\mathrm{kg/s^2}$)', fontsize=16)
plt.ylabel(r'$\omega_0$ (rad/s)', fontsize=16)

plt.grid(True)
plt.tight_layout()
plt.show()
