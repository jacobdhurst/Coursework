from scipy import *
from matplotlib import pyplot 
from poisson import poisson

# SPLU is a sparse matrix LU factorization
from scipy.sparse.linalg import splu
# speye generates a sparse identity matrix
from scipy.sparse import eye as speye

# Tasks:
# - Read through the code.  Make sure you understand it.  Then run it,
#   and make sure you understand the output.  
#
# - There are a few places in the code to implement something.
#
# - Carry out an error analysis of Backward Euler.  Plot the error as you
#   refine in h and ht.
#   --> How should h and ht decrease, relative to each other, in order to get 
#       balanced error?  Remember Euler is O(h) and the spatial discretization
#       is O(h^2)
#   --> To understand this, run the code as the grid size (n) stays fixed, but 
#       you increase necrease nt repeatedly by a factor of 2.  This is the 
#       current set up in the code.  How does the error decrease?
#   --> Next, do the same, but in reverse.  Hold nt fixed, but increase n
#       repeatedly by a factor of 2.
#
# - Implement forward Euler above
#   --> To run forward Euler, you'll have to comment in the correct time 
#       stepping scheme down below
#   --> If I told you that the  max eigenvalue of A was roughly 4.0/h^2, 
#       Could you pre-compute the largest allowed time-step size? 
#   --> Try generating an error plot with forward Euler show the error convergence.
#
# - If bored...you could change the exact solution and forcing, so that you have
#   a nonzero boundary term.

def euler_backward(A, u, ht, f):
    '''
    Carry out forward Euler's method for one time step
    '''
    Ainv = splu( speye(A.shape[0], format='csc') - ht*A) 
    return Ainv.solve(u + ht*f)

def euler_forward(A, u, ht, f):
    '''
    Carry out forward Euler's method for one time step
    '''
    return u + ht*A*u + f # + ht*g (g=0 here)

##
# Problem Definition
#
# u_t = u_xx + u_yy + f,    on the unit box [0,1] x [0,1] and t in a user-defined interval
#
# with an exact solution of
# u(t,x,y) = sin(t)*sin(pi*x)*sin(pi*y)
#
# which in turn, implies a forcing term of f, where
# f(t,x,y) = cos(t)*sin(pi*x)*sin(pi*y) + 2*pi**2*sin(t)*sin(pi*x)*sin(pi*y) 
#
# u(t=0,x,y) = 0            zero initial condition
#
# g(t,x,y) = u(t,x,y) = 0   for x,y on the boundary
#

# Declare the problem
def uexact(t,x,y):
    # Exact solution
    return sin(pi*t)*sin(pi*x)*sin(pi*y)

def f(t,x,y):
    # Forcing term
    # This should equal u_t - u_xx - u_yy
    return (pi*cos(pi*t)*sin(pi*x)*sin(pi*y))+(2*(pi*pi)*sin(pi*t)*sin(pi*x)*sin(pi*y))

# Loop over various numbers of time points (nt) and spatial grid sizes (n)
for (nt, n) in zip([8, 8*2, 8*4, 8*8], [16, 16, 16, 16] ): 

    # Declare time domain
    t0 = 0.0
    T = 1.5
    ht = (T - t0)/float(nt-1)

    # Declare spatial domain
    h = 1.0 / (n-1.0)
    # Remember, we assume a zero boundary condition, which simplifies things a bit here
    # Thus, we only want a spatial grid from [h, 2h, ..., 1-h] x [h, 2h, ..., 1-h]
    pts = linspace(h, 1-h, n-2) 
    X,Y = meshgrid(pts, pts)
    X = X.reshape(-1,)
    Y = Y.reshape(-1,)

    # Declare spatial discretization matrix
    A = poisson((n-2, n-2), format='csc')
    A = (1.0/h**2)*A

    # Declare initial condition
    #   This initial condition obeys an all 0 boundary condition, meaning the
    #   solution is clamped to 0 at the boundary.
    u0 = uexact(0, X, Y) 

    # Declare storage
    u = zeros((nt,A.shape[0]))
    ue = zeros((nt,A.shape[0]))
    u[0,:] = u0
    ue[0,:] = u0

    # Run time-stepping
    for i in range(1,nt):
        ue[i,:] = uexact(i*ht, X, Y)
        
        # Option 1: Backward Euler
        #u[i,:] = euler_backward(A, u[i-1,:], ht, f(i*ht,X,Y))
        
        # Option 2: Forward Euler
        u[i,:] = euler_forward(A, u[i-1,:], ht, f(i*ht,X,Y))
        

    # Compute L2-norm of the error at final time
    e = (u - ue).reshape(-1,)
    enorm = sqrt((h*h*ht)*(sum(e**2)))
    print "Nt, N, Error is:  " + str(nt) + ",  " + str(n) + ",  " + str(enorm)

    # You can turn this on to visualize the solution
    if True:
        pyplot.figure(1)
        pyplot.imshow(u[0,:].reshape(n-2,n-2), origin='lower', extent=(0, 1, 0, 1))
        pyplot.colorbar()
        pyplot.xlabel('X')
        pyplot.ylabel('Y')
        pyplot.title("Initial Condition")
        
        pyplot.figure(2)
        pyplot.imshow(u[int(nt/2),:].reshape(n-2,n-2))
        pyplot.colorbar()
        pyplot.xlabel('X')
        pyplot.ylabel('Y')
        pyplot.title("Solution at time T/2")
        
        pyplot.figure(3)
        pyplot.imshow(u[-1,:].reshape(n-2,n-2))
        pyplot.colorbar()
        pyplot.xlabel('X')
        pyplot.ylabel('Y')
        pyplot.title("Solution at final time")
       
        pyplot.figure(4)
        pyplot.imshow(uexact(T,X,Y).reshape(n-2,n-2))
        pyplot.colorbar()
        pyplot.xlabel('X')
        pyplot.ylabel('Y')
        pyplot.title("Exact Solution at final time")
        
        pyplot.show()
