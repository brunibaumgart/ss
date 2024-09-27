import pandas as pd
import matplotlib.pyplot as plt
from decimal import Decimal

from pandas.util import capitalize_first_letter

# Read the CSV file into a DataFrame
results_df = pd.read_csv('../output/mse_results.csv')

# Convert MSE values back to Decimal for precision if necessary
results_df['MSE'] = results_df['MSE'].apply(Decimal)

# Create a figure for plotting
plt.figure(figsize=(10, 6))

# Plotting each algorithm with a different color
for algorithm in results_df['Algorithm'].unique():
    subset = results_df[results_df['Algorithm'] == algorithm]
    plt.scatter(subset['Delta T'], subset['MSE'], label=capitalize_first_letter(algorithm), marker='o')

# Set x-axis and y-axis to logarithmic scale
plt.xscale('log')
plt.yscale('log')

# Increase font sizes
plt.xlabel(r"$dt$ (s)", fontsize=14)  # Increased font size for the x-label
plt.ylabel('Error Cuadrático Medio', fontsize=14)  # Increased font size for the y-label
plt.xticks(fontsize=12)  # Increased font size for x-axis ticks
plt.yticks(fontsize=12)  # Increased font size for y-axis ticks
plt.grid(True)

# Customize legend and title with larger fonts
plt.legend(title='Algoritmo', fontsize=12, title_fontsize=14)

# Display the plot with a tighter layout
plt.tight_layout()
plt.show()
