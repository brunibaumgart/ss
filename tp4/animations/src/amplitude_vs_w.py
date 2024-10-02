import matplotlib.pyplot as plt

from src.constants import FilePaths

filename = FilePaths.SIMULATIONS_DIR + 'amplitude_vs_w.txt'

amplitudes = []
ws = []

with open(filename, 'r') as file:
    next(file)
    for line in file:
        a, w = map(float, line.split())
        amplitudes.append(a)
        ws.append(w)

plt.plot(ws, amplitudes)
plt.xlabel('W (s)')
plt.ylabel('Amplitud (m)')
plt.grid(True)
plt.show()