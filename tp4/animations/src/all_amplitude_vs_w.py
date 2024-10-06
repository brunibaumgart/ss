import matplotlib.pyplot as plt

from src.constants import FilePaths

ks = [100, 1000, 2500, 5000, 10000]

amplitudes_dict = {}
ws_dict = {}

for k in ks:
    filename = FilePaths.SIMULATIONS_DIR + f'amplitude_vs_w_k_{k}.txt'

    amplitudes = []
    ws = []

    with open(filename, 'r') as file:
        next(file)
        for line in file:
            a, w = map(float, line.split())
            amplitudes.append(a)
            ws.append(w)

    amplitudes_dict[k] = amplitudes
    ws_dict[k] = ws

plt.rcParams.update({'font.size': 14})

for k in ks:
    plt.plot(ws_dict[k], amplitudes_dict[k], '-o', label=f'k = {k}', markersize=3)  # Ajusta el tamaño de los puntos con markersize

plt.xlabel(r"$\omega$ (rad/s)", fontsize=16)
plt.ylabel('Amplitud (m)', fontsize=16)
plt.grid(True)

plt.legend(fontsize=12)

plt.tight_layout()
plt.show()