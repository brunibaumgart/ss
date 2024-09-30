import matplotlib.pyplot as plt

from src.constants import FilePaths

# Replace 'data.txt' with your file name
filename = FilePaths.SIMULATIONS_DIR + 'amplitude_vs_time.txt'

# Initialize lists to store time and y_position values
time = []
y_position = []

# Read the file
with open(filename, 'r') as file:
    for line in file:
        t, y = map(float, line.split())  # Split the line and convert to float
        time.append(t)
        y_position.append(y)

# Create the plot
plt.plot(time, y_position)  # You can customize the marker and line style
plt.xlabel('Tiempo (s)')
plt.ylabel('Amplitud (m)')
plt.grid(True)
plt.show()
