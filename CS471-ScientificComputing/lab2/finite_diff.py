import time
from scipy import *
from scipy import linalg
from matplotlib import *
from matplotlib import pyplot

start = time.time()
errorp1 = zeros((1000-10,))
errorp2 = zeros((1000-10,))

for n in range(10, 1000):
    x = linspace(0,2*pi,n)
    h = 2*pi/(n-1)
    f = sin(x)
    fp1 = zeros( (x.shape[0]-2,) )
    fp2 = zeros( (x.shape[0]-4,) )
    for k in range(1,n-1):
        w = [1, -2, 1]
        fp1[k-1] = (1/h**2)*(w[0]*f[k-1] + w[1]*f[k] + w[2]*f[k+1])
        # insert second order accurate stencil
    for k in range(2,n-2):
        w = [-1/12, 4/3, -5/2, 4/3, -1/12]
        fp2[k-2] = (1/h**2)*(w[0]*f[k-2] + w[1]*f[k-1] + w[2]*f[k] + w[3]*f[k+1] + w[4]*f[k+2])
        # insert fourth order accurate stencil

    fpexact = -sin(x) # exact expression for the second derivative sin(x)'' = -sin(x)

    # Note that we did not compute the finite difference stencil at boundary points
    errorp1[n-10] = linalg.norm(fpexact[1:-1] - fp1)
    errorp2[n-10] = linalg.norm(fpexact[2:-2] - fp2)

print("elapsed: " + str(time.time() - start))

n = arange(10,1000)
pyplot.loglog(n, errorp1, n, errorp2, n, 1.0/n**2, n, 1.0/n**4)
pyplot.legend(['FD Plot 1', 'FD Plot 2', 'Quadratic Ref', 'Quartic Ref'])
pyplot.show()
