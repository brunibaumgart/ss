import matplotlib.pyplot as plt

from src.constants import FilePaths

filename = FilePaths.SIMULATIONS_DIR + 'amplitude_vs_w.txt'
optimum_w = 49.67294 # define optimum omega for the vertical line

amplitudes = []
ws = []

with open(filename, 'r') as file:
    next(file)
    for line in file:
        a, w = map(float, line.split())
        amplitudes.append(a)
        ws.append(w)

plt.rcParams.update({'font.size': 14})

plt.plot(ws, amplitudes, 'o', markersize=3)

plt.axvline(x=optimum_w, color='red', linestyle='--')

plt.xlabel(r"$\omega$ (rad/s)", fontsize=16)
plt.ylabel('Amplitud (m)', fontsize=16)
plt.grid(True)
plt.tight_layout()
plt.show()