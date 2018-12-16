from scipy import *
from scipy import sparse
import time
from threading import Thread
import sys

def matvec(A,x,y,start,stop, myrank):
    '''
    Carry out a Matvec over this row-range
    '''
    # A is local, but x and y are global
    # But we are careful to avoid race conditions in y
    y[start:stop] = A*x
    
    # For debug printing inside of a threaded function, print to standard error
    sys.stderr.write("Thread " + str(myrank) + "\n")

# Construct Matrix -- Master thread portion
n = 24000000
data = ones((3,n))
data[1,:] = -2.0
A = sparse.spdiags(data, [-1,0,1], n, n, format='csr')
# Carry out threading 
min_time = 10000
nt = 1
x = zeros((A.shape[0],))
y = zeros((A.shape[0],))
for m in range(4):
    
    t_list = []
    for k in range(nt):
        start = int(k*(n/nt))
        stop = int((k+1)*(n/nt))
        t_list.append(Thread(target=matvec, args=(A[start:stop,:], x, y, start, stop, k))) 

    # Fork and Join part
    start = time.time()
    for t in t_list:
        t.start()   
    for t in t_list:
        t.join()   
    end = time.time()
    min_time = min(end-start, min_time)

# Output result
print("\nMin time take is: " + str(min_time) + "\n")

