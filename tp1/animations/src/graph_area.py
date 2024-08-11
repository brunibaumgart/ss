import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Define parameters
M = 10  # Size of the grid cells
L = 7  # Side length of the area (adjust as needed)

# Load data into a DataFrame
data = {
    'id': range(30),
    'x': [1.76, 2.23, 5.83, 3.86, 1.00, 1.67, 6.92, 6.11, 6.81, 0.50,
          6.20, 2.04, 6.04, 0.07, 0.79, 4.58, 2.40, 5.49, 3.60, 0.98,
          2.95, 6.71, 4.64, 0.01, 2.47, 5.39, 5.47, 6.31, 3.09, 3.25],
    'y': [4.21, 6.25, 4.87, 6.24, 2.48, 0.54, 4.88, 6.08, 5.86, 6.03,
          2.16, 0.97, 3.03, 0.86, 1.35, 6.52, 5.20, 2.32, 4.51, 3.99,
          3.74, 3.81, 6.98, 5.09, 4.86, 3.30, 5.77, 2.37, 1.27, 3.60],
    'neighbours': [[], [], [], [], [], [], [23], [], [], [],
                   [27], [], [12], [], [22], [24], [], [], [], [29],
                   [], [15], [6], [16], [], [], [], [10], [], [20]]
}
df = pd.DataFrame(data)

# Plot the points
plt.figure(figsize=(10, 8))
plt.scatter(df['x'], df['y'], c='blue', label='Particles')

# Plot the connections based on neighbors
for index, row in df.iterrows():
    x1, y1 = row['x'], row['y']
    for neighbor_id in row['neighbours']:
        neighbor = df[df['id'] == neighbor_id].iloc[0]
        x2, y2 = neighbor['x'], neighbor['y']
        plt.plot([x1, x2], [y1, y2], 'k--', alpha=0.6)  # dashed line

# Overlay the grid
size_M = 4
for i in range(0, size_M, M):
    plt.axvline(i, color='gray', linestyle='--', linewidth=0.5)
    plt.axhline(i, color='gray', linestyle='--', linewidth=0.5)

# Set labels and title
plt.xlabel('X Coordinate')
plt.ylabel('Y Coordinate')
plt.title(f'Particle Plot with Neighbors and {M}x{M} Grid')
plt.legend()
plt.grid(True)

# Set axis limits to fit the grid nicely
plt.xlim(0, L)
plt.ylim(0, L)

# Display the plot
plt.show()
