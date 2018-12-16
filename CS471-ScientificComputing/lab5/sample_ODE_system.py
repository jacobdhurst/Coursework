from scipy import *
from matplotlib import pyplot 
from poisson import poisson

# SPLU is a sparse matrix LU factorization
from scipy.sparse.linalg import splu
# speye generates a sparse identity matrix
from scipy.sparse import eye as speye


def euler_backward(A, y, ht):
    '''
    Carry out forward Euler's method for one time step
    '''
    Ainv = splu( speye(A.shape[0], format='csc') - ht*A)
    return Ainv.solve(y)


def euler_forward(A, y, ht):
    '''
    Carry out forward Euler's method for one time step
    '''
    return y + 0.0 # Fill in with correct values! 


def RK2(A, y, ht):
    '''
    Carry out RK2 for one time step 
    '''

    k1 = 0.0 # Fill in with correct values!
    k2 = 0.0 # Fill in with correct values! 
    return y + 0.0 # Fill in with correct values! 

##
# Problem Definition
#
# u_t = u_xx + u_yy,  on the unit box  
#
# u(t,x,y) = 0,       for (x,y) on the boundary
#
# u(t=0,x,y) = sin(pi x) * sin(pi y)
#
##



# Declare time domain
t0 = 0.0
T = 1.0
nt = 101
ht = (T - t0)/float(nt-1)

# Declare spatial domain
n = 16
pts = linspace(0,1,n)
X,Y = meshgrid(pts, pts)
X = X.reshape(-1,)
Y = Y.reshape(-1,)
h = 1.0 / (n-1.0)

# Declare spatial discretization matrix
A = poisson((n,n), format='csc')
A = (1.0/h**2)*A

# Declare initial condition
#   This initial condition obeys an all 0 boundary condition, meaning the
#   solution is clamped to 0 at the boundary.
y0 = sin(pi*X)*sin(pi*Y)

# Declare storage
y = zeros((nt,n*n))
y[0,:] = y0

# Tasks:
# - Read through the code.  Make sure you understand it.  Then run it,
#   and make sure you understand the output.  Why is the solution 
#   simple descreasing in magnitude over time?
# - Implement forward Euler and RK2 above
# - Experiment with different ht values, observing when you violate the 
#   stability limit of forward Euler and RK2
#   --> You'll have to comment in the correct time stepping scheme down below
# - If I told you that the  max eigenvalue of A was roughly 4.0/h^2, 
#   Could you pre-compute the largest allowed time-step size? 


# Run time-stepping
for i in range(1,nt):
    # Option 1: Backward Euler
    y[i,:] = euler_backward(A, y[i-1,:], ht)
    
    # Option 2: Forward Euler
    #y[i,:] = euler_forward(A, y[i-1,:], ht)
    
    # Option 3: RK2
    #y[i,:] = RK2(A, y[i-1,:], ht)


pyplot.figure(1)
pyplot.imshow(y[0,:].reshape(n,n), origin='lower', extent=(0, 1, 0, 1))
pyplot.colorbar()
pyplot.xlabel('X')
pyplot.ylabel('Y')
pyplot.title("Initial Condition")

pyplot.figure(2)
pyplot.imshow(y[int(nt/2),:].reshape(n,n))
pyplot.colorbar()
pyplot.xlabel('X')
pyplot.ylabel('Y')
pyplot.title("Solution at time T/2")

pyplot.figure(3)
pyplot.imshow(y[-1,:].reshape(n,n))
pyplot.colorbar()
pyplot.xlabel('X')
pyplot.ylabel('Y')
pyplot.title("Solution at final time")

pyplot.show()

