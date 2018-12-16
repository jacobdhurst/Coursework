from scipy import *
from matplotlib import pyplot
from pdb import set_trace

def fprime(f, h):
    # Return an approximation to the first derivative
    # set_trace()
    fp = zeros(x.shape)
    fp[1:] = (f[1:] - f[:-1])/h
    fp[0] = fp[1]
    return fp

# set_trace()
n = 100
x = linspace(0,2*pi,n+1)
h = 2*pi/n
f = sin(x)
fp = fprime(f,h)

pyplot.plot(x,cos(x))
pyplot.plot(x,fp)
pyplot.legend(['f-prime', 'f-prime approx.'])
pyplot.show()
