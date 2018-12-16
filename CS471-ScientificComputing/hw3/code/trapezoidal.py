from scipy import *
from numpy import *
from scipy import integrate


def trapezoidal(f, lower, upper, n):
    h = (upper - lower) / n
    x = linspace(lower, upper, n + 1)
    feval = zeros(n+1)

    for i in range(0, n+1):
        feval[i] = f(x[i])
    
    weights = ones(n+1)
    for i in range(1, n):
        weights[i] += 1
    
    integral = (h / 2) * sum( multiply(weights, feval) )

    return integral
