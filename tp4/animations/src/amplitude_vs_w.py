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

# Increase font size globally
plt.rcParams.update({'font.size': 14})  # Adjust 14 to your preferred font size

# Plot with points for each value
plt.plot(ws, amplitudes, '-o')  # '-o' adds points and connects them with a line
plt.xlabel(r"$\omega$ (rad/s)", fontsize=16)  # Adjust fontsize if needed
plt.ylabel('Amplitud (m)', fontsize=16)   # Adjust fontsize if needed
plt.grid(True)
plt.tight_layout()
plt.show()