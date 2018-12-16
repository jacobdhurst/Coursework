import time
from scipy import *
from scipy import linalg
from scipy import sparse
from matplotlib import *
from matplotlib import pyplot

errorp1 = zeros((740,))
errorp2 = zeros((740,))
arraytimes1 = zeros((740,))
arraytimes2 = zeros((740,))
matrixtimes1 = zeros((740,))
matrixtimes2 = zeros((740,))

# comparing performance of array based finite differencing and sparse CSR matrices.
for n in range(10, 750):
    x = linspace(0,2*pi,n)
    h = 2*pi/(n-1)
    f = sin(x)
    fp1 = zeros( (x.shape[0]-2,) )
    fp2 = zeros( (x.shape[0]-4,) )
    
    # array based finite differencing
    
    arraytimes1[n-10] = 100
    for i in range(10):
        start = time.time()
        fp1 = (1/h**2)*(f[:-2] - 2*f[1:-1] + f[2:])
        runtime = (time.time()) - start
        if(runtime < arraytimes1[n-10]): arraytimes1[n-10] = runtime
            
        
    arraytimes2[n-10] = 100
    for i in range(10):
        start = time.time()
        fp2 = (1/h**2)*((-1/12)*f[:-4] + (4/3)*f[1:-3] + (-5/2)*f[2:-2] + (4/3)*f[3:-1] + (-1/12)*f[4:])
        runtime = (time.time()) - start
        if(runtime < arraytimes2[n-10]): arraytimes2[n-10] = runtime
        
    fpexact = -sin(x)

    errorp1[n-10] = linalg.norm(fpexact[1:-1] - fp1)
    errorp2[n-10] = linalg.norm(fpexact[2:-2] - fp2)

    # print("errorp1="+str(errorp1[n-10])+" found in "+str(arraytimes1[n-10])+"s")
    # print("errorp2="+str(errorp2[n-10])+" found in "+str(arraytimes2[n-10])+"s\n")

    # sparse CSR matrix method

    data = ones((3,n))
    data[1,:] = -2.0
    Acsr1 = (1/h**2)*sparse.spdiags(data, [-1,0,1], n, n, format='csr')

    data = ones((5,n))
    data[0,:] = -1/12.0
    data[1,:] = 4./3.
    data[2,:] = -5./2.
    data[3,:] = 4./3.
    data[4,:] = -1/12.0
    Acsr2 = (1/h**2)*sparse.spdiags(data, [-2, -1, 0, 1, 2], n, n, format='csr')

    matrixtimes1[n-10] = 100
    for t in range(10):
        start = time.time()
        y = Acsr1*x
        runtime = (time.time()) - start
        if(runtime < matrixtimes1[n-10]): matrixtimes1[n-10] = runtime

    for t in range(10):
        start = time.time()
        y = Acsr2*x
        runtime = (time.time()) - start
        if(runtime < matrixtimes2[n-10]): matrixtimes2[n-10] = runtime

n = linspace(0, 740, 740)
pyplot.semilogy(n, arraytimes1, n, arraytimes2, 
                n, matrixtimes1, n, matrixtimes2)
pyplot.legend(['array1', 'array2', 'matrix1', 'matrix2'])
pyplot.show()
