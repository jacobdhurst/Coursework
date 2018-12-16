from numpy import *
from scipy import *
from matplotlib import pyplot
import math

n = linspace(0, 150, 150)
f = 32*n*log2(n)
g = 2*n**2

pyplot.figure(1)
pyplot.plot(n, f, n, g)
pyplot.plot(108, 23357, 'ro')
pyplot.legend(['Algorithm A', 'Algorithm B', 'x = 108'])
pyplot.xlabel('N')
pyplot.ylabel('Clock Cycles')
pyplot.savefig('q1.png', dpi=500)
