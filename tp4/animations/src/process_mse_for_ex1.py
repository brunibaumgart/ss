import os
from decimal import Decimal, getcontext

import numpy as np
import pandas as pd

from src.constants import FilePaths

# Set the precision for the decimal module
getcontext().prec = 50  # Set this to the desired precision

# Function to extract algorithm and delta T from filename
def extract_algorithm_and_delta_t(file_path):
    file_name = os.path.basename(file_path)  # Extract only the file name from the path
    algorithm = file_name.split('_')[0]
    delta_t = Decimal(file_name.split('_')[1].replace('.txt', ''))
    return algorithm, delta_t

# Function to read data from file using pandas
def read_data(file_name):
    data = pd.read_csv(file_name, sep='\s+', header=None, names=['time', 'position'], dtype=np.float64)
    return data['time'].values, data['position'].values  # Ensure high precision

# Function for the analytical solution
def analytical_solution(t, A, gamma, m, k):
    # Convert Decimal parameters to float for NumPy operations
    A_float = float(A)
    gamma_float = float(gamma)
    m_float = float(m)
    k_float = float(k)

    term1 = A_float * np.exp(-gamma_float / (2 * m_float) * t)
    term2 = np.cos(np.sqrt(k_float / m_float - (gamma_float ** 2 / (4 * m_float ** 2))) * t)
    return term1 * term2

# Function to calculate mean squared error (MSE)
def calculate_mse(positions_calculated, positions_analytical):
    return np.mean((positions_calculated - positions_analytical) ** 2)

# Placeholder values for the analytical solution
A = Decimal(1.0)  # Use Decimal for maximum precision
gamma = Decimal(100.0)
m = Decimal(70.0)
k = Decimal(10000.0)

# List of file paths
files = [
    FilePaths.SIMULATIONS_DIR + '/ej1/beeman_0.000001.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/beeman_0.000010.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/beeman_0.000100.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/beeman_0.001000.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/beeman_0.010000.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/verlet_0.000001.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/verlet_0.000010.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/verlet_0.000100.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/verlet_0.001000.txt',
    FilePaths.SIMULATIONS_DIR + '/ej1/verlet_0.010000.txt',
    ]

# Create an empty DataFrame to store results
results_df = pd.DataFrame(columns=['Algorithm', 'Delta T', 'MSE'])

# Loop through each file
for file_path in files:
    # Extract the algorithm and delta T from the file name
    algorithm, delta_t = extract_algorithm_and_delta_t(file_path)

    # Read the time and positions from the file
    times_calculated, positions_calculated = read_data(file_path)

    # Calculate the analytical solution for the same times
    positions_analytical = analytical_solution(times_calculated, A, gamma, m, k)

    # Calculate the Mean Squared Error (MSE)
    mse = calculate_mse(positions_calculated, positions_analytical)

    # Append the results to the DataFrame with high precision
    results_df = results_df._append({
        'Algorithm': algorithm,
        'Delta T': delta_t,
        'MSE': Decimal(mse)  # Store MSE as Decimal for maximum precision
    }, ignore_index=True)

# Save results to CSV with high precision
results_df.to_csv('mse_results.csv', float_format='%.40f', index=False)
