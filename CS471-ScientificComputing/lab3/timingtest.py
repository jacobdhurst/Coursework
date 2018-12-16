from scipy import *
from matplotlib import pyplot
import time

#def fprime(f, fp, h):
    # Return an approximation to the first derivative
    #fp[1:] = (f[1:] - f[:-1])/h
    #fp[0] = fp[1]i
    #return fp

x = linspace(0,2*pi,100001)
h = 2*pi/30
f = sin(x)

min = 1000
for i in range(1, 1000):
    fp = zeros(x.shape)
    start = time.time()
    #fp = fprime(f,fp,h)
    fp[1:] = (f[1:] - f[:-1])/h
    fp[0] = fp[1]
    end = time.time()
    runtime = end - start
    if(runtime < min): min = runtime

print("\nminimum time to run fprime: " + str(min) + "\n")
