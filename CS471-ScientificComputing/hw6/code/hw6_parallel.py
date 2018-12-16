from scipy import *
from matplotlib import pyplot 
from poisson import poisson
from scipy.sparse import eye as speye
from mpi4py import MPI
import numpy
import time
import sys

def jacobi(A, b, x0, tol, maxiter, start, start_halo, end, end_halo, N, comm):
    '''
    Carry out the Jacobi method to invert A
      
    Input
    -----
    A <CSR matrix>  : Matrix to invert
    b <array>       : Right hand side
    x0 <array>      : Initial solution guess
    tol <float>     : Halting tolerance for Jacobi
    maxiter <int>   : Max number of allowed Jacobi iterations
    start <int>     : First domain row owned by this processor (same as HW4)
    start_halo <int>: Halo region "below" this processor (same as HW4)
    end <int>       : Last domain row owned by this processor (same as HW4)
    end_halo <int>  : Halo region "above" this processor (same as HW4)
    N <int>         : Size of a domain row, excluding boundary points
    comm            : MPI communicator
          
    Output
    ------
    x <array>       : Solution to A x = b
    '''
       
    rank = comm.Get_rank()

    # This useful function returns an array containing diag(A)
    D = A.diagonal()
    Ax0 = matrix_vector(A, x0, start, start_halo, end, end_halo, N, comm)

    # compute initial residual norm
    r0 = ravel(b - Ax0)
    r0 = vector_norm(r0, start, start_halo, end, end_halo, N, comm) # sqrt(dot(r0, r0))

    xk = x0

    # Start Jacobi iterations
    # Task in serial: implement Jacobi formula and halting if tolerance is satisfied
    # Task in parallel: extend the matrix-vector multiply to the parallel setting.  
    #                   Additionally, you'll have to compute a vector norm in parallel.
    #                   
    #                   It is suggested to write separate subroutines for the norm and 
    #                   matrix-vector product, as this will make your code much, much 
    #                   easier to debug and extend to CG.
    
    for i in range(maxiter):
        x0 = xk

        Ax0 = matrix_vector(A, x0, start, start_halo, end, end_halo, N, comm)

        xk = x0 - (Ax0)/D + b/D
                            
        # Carry out Jacobi 
        # for pll u(i)=(u(i-1)+u(i+1))/2

        Axk = matrix_vector(A, x0, start, start_halo, end, end_halo, N, comm)
        rk = ravel(b - Axk)
        rk = vector_norm(rk, start, start_halo, end, end_halo, N, comm) # sqrt(dot(rk, rk))

        error = rk / r0
        if error < tol:
            return xk		
    
    # Task: Print out if Jacobi did not converge.
    if(rank == 0): print("Jacobi did not converge")
    return xk

def euler_backward(A, u, ht, f, g, start, start_halo, end, end_halo, N, comm):
    '''
    Carry out backward Euler's method for one time step
       
    Input
    -----
    A <CSR matrix>  : Discretization matrix of Poisson operator
    u <array>       : Current solution vector at previous time step
    ht <scalar>     : Time step size
    f <array>       : Current forcing vector
    g <array>       : Current vector containing boundary condition information
    start <int>     : First domain row owned by this processor (same as HW4)
    start_halo <int>: Halo region "below" this processor (same as HW4)
    end <int>       : Last domain row owned by this processor (same as HW4)
    end_halo <int>  : Halo region "above" this processor (same as HW4)
    N <int>         : Size of a domain row, excluding boundary points
                      ===> THAT IS, N = n-2
    comm            : MPI communicator

    Output
    ------
    u at the next time step
    '''
    
    rank = comm.Get_rank()
    
    # Task: Form the system matrix for backward Euler
    I = speye(A.shape[0], format='csr') 
    G = I - ht*A #...
    b = u + ht*g + ht*f
    
    # Task: return solution from Jacobi 
    return jacobi(G, b, u, 10**-9, 300, start, start_halo, end, end_halo, N, comm)
    
def vector_norm(v, start, start_halo, end, end_halo, N, comm):
    ''' 
    Carry out || v ||_2
    '''

    # compute each processor's local portion of dot(v,v), then do a global all_reduce

    v = remove_halo(v, start, start_halo, end, end_halo, N)
    localnorm  = dot(v, v)
    globalnorm = zeros_like(localnorm) # globalnorm = sqrt(comm.allreduce(localnorm, op=MPI.SUM))

    comm.Allreduce(localnorm, globalnorm, op=MPI.SUM)

    # if(comm.Get_rank() == 0): print("NORM: " + str(sqrt(globalnorm)))

    return sqrt(globalnorm)

def matrix_vector(A, x, start, start_halo, end, end_halo, N, comm):
    '''
    Carry out a matvec A*x over the domain rows start:end
    '''

    # processor above is "my_rank+1", and the processor below is "my_rank-1"
    rank = comm.Get_rank()
    size = comm.Get_size()

    # Send an array of doubles to rank 1 (from rank 0)
    # comm.Send([data, MPI.DOUBLE], dest=1, tag=77)

    # Receive that array of doubles from rank 0 (on rank 1)
    # comm.Recv([data, MPI.DOUBLE], source=0, tag=77)
    data = zeros(N)

    # x = remove_halo(x, start, start_halo, end, end_halo, N)
    # print(str(rank) + " : " + str(x) + " " + str(start) + " " + str(end) + " " + str(start_halo) + " " + str(end_halo))

    # if my_rank is not 0, then send second domain row in x to the proc below
    #   This fills in the halo region on the proc below.
    if(rank != 0):
        comm.send(x[N:2*N], dest=(rank - 1)) # rank 1 sends row 2 to rank 0
        #print("rank", rank, "sending", x[N:2*N])

    # if my_rank is not nprocs-1, then send second-to-last domain row in x to the proc above 
    #   This fills in the halo region on the proc above.
    if(rank != size-1):
        comm.send(x[-2*N:-N], dest=(rank + 1)) # rank 2 sends row 5 to rank 3
        #print("rank", rank, "sending", x[-2*N:-N])

    #print(str(rank) + " : " + str(x) + " " + str(start) + " " + str(end) + " " + str(start_halo) + " " + str(end_halo))

    # if my_rank is not 0, then receive my bottom halo row from the proc below
    #   This fills in my lower halo row (first row in x)
    if(rank != 0):
        data = comm.recv(source=(rank - 1)) # rank 1 receives row 1
        # update x
        #print("rank", rank, data)
        x[:N] = data

    # if my_rank is not nprocs-1, then receive my top halo row from the next proc
    #   This fills in my upper halo row (last row in x)
    if(rank != size-1):
        data = comm.recv(source=(rank + 1)) # rank 2 receives row 6
        # update x
        #print("rank", rank, data)
        x[-N:] = data

    #print(str(rank) + " : " + str(x) + " " + str(start) + " " + str(end) + " " + str(start_halo) + " " + str(end_halo))

    return A*x



def remove_halo(x, start, start_halo, end, end_halo, N):
    '''
    Remove the halo region for vector x
    '''

    rowcount = int(end - start)

    if(start == start_halo): x = x[:rowcount*N]
    elif(end == end_halo): x = x[rowcount*(-N):]
    else: x = x[N:-N]

    return x

##
# Problem Definition
# Task: figure out the (1) exact solution, (2) f(t,x,y), and (3) g(t,x,y)
#
# u_t = u_xx + u_yy + f,    on the unit box [0,1] x [0,1] and t in a user-defined interval
#
# with an exact solution of
# u(t,x,y) = sin(pi*t)*cos(pi*x)*cos(pi*y)
#
# which in turn, implies a forcing term of f, where
# f(t,x,y) = (pi*cos(pi*t)*cos(pi*x)*cos(pi*y))+(2*(pi*pi)*sin(pi*t)*cos(pi*x)*cos(pi*y))
#
# u(t=0,x,y) = 0                                                zero initial condition
#
# g(t,x,y) = u(t,x,y) = sin(pi*t)*cos(pi*x)*cos(pi*y)           for x,y on the boundary
#

# Declare the problem
def uexact(t,x,y):
    # Task: fill in exact solution
    return sin(pi*t)*cos(pi*x)*cos(pi*y)

def f(t,x,y):
    # Forcing term
    # This should equal u_t - u_xx - u_yy
    
    # Task: fill in forcing term
    return (pi*cos(pi*t)*cos(pi*x)*cos(pi*y))+(2*(pi*pi)*sin(pi*t)*cos(pi*x)*cos(pi*y))

# Initialize MPI
comm = MPI.COMM_WORLD

# Loop over various numbers of time points (nt) and spatial grid sizes (n)
error = []
timings = []
efficiency = []
Nt_values = []
N_values = []
T = 0.0

# Ex: mpirun -np 8 python hw6_parallel.py weak
if(len(sys.argv) > 1):
    # Some larger sizes for a convergence study    
    if(sys.argv[1] == "convergence"):
        Nt_values = array([8, 8*4, 8*4*4, 8*4*4*4])
        N_values = array([8, 16, 32, 64, ])
        T = 0.5
    # Two very small sizes for debugging
    elif(sys.argv[1] == "debug"):
        Nt_values = array([8, 8*4])
        N_values = array([8,16])
        T = 0.5
    # Task: Change T and the sizes for the weak and strong scaling studies
    elif(sys.argv[1] == "strong"): # TODO # nodes:cores 1:2, 1:4, 1:8, 2:8, 4:8, 8:8, 16:8
        Nt_values = array([256])
        N_values = array([256])
        T = 0.03
    # Task: Change T and the sizes for the weak and strong scaling studies
    elif(sys.argv[1] == "weak:1"): # TODO # nodes:cores 1:1, 1:4, 2:8, 8:8
        # Run on 1 proc
        Nt_values = array([16])
        N_values = array([48])
    elif(sys.argv[1] == "weak:2"):
        # Run on 4 procs
        Nt_values = array([16*4])
        N_values = array([48*2])
    elif(sys.argv[1] == "weak:3"):
        # Run on 16 procs
        Nt_values = array([16*4*4])
        N_values = array([48*4])
    elif(sys.argv[1] == "weak:4"):
        # Run on 64 procs
        Nt_values = array([16*4*4*4])
        N_values = array([48*8])

for (nt, n) in zip(Nt_values, N_values):
    # Ensure domain rows divide evenly by processors
    n = n + 2
    
    # Declare time domain
    t0 = 0.0
    ht = (T - t0)/float(nt-1)

    # Declare spatial domain grid spacing
    h = 1.0 / (n-1.0)
    
    # Task in parallel: Compute which portion of the spatial domain the current MPI rank owns.
    # processor above is "my_rank+1", and the processor below is "my_rank-1"
    size = comm.Get_size()
    rank = comm.Get_rank()
    start = int(rank*((n-2)/size)) # Might just be n
    end = int((rank+1)*((n-2)/size)) # Might just be n
    
    if(rank != 0): start_halo = start-1
    else: start_halo = start
    
    if(rank != (size-1)): end_halo = end+1
    else: end_halo = end

    # print(str(rank) + " start: " + str(start))
    # print(str(rank) + " end: " + str(end))
    # print(str(rank) + " start_halo: " + str(start_halo))
    # print(str(rank) + " end_halo: " + str(end_halo))

    rowcount = end - start
    rowcount_halo = end_halo - start_halo

    # Remember, we assume a Dirichlet boundary condition, which simplifies
    # things a bit.  Thus, we only want a spatial grid from
    # [h, 2h, ..., 1-h] x [h, 2h, ..., 1-h].
    # We know what the solution looks like on the boundary, and don't need to solve for it.
    # Task: fill in the spatial grid vector "pts"
    # Task: in parallel, you'll need this to be just the local grid plus halo region.  Mimic HW4 here.
    # pts = linspace(h, 1-h, n-2)

    x_pts = linspace(h, 1-h, n-2)
    if(end == end_halo): y_pts = linspace(start_halo*h, 1-h, rowcount_halo)
    else: y_pts = linspace(start_halo*h, end*h, rowcount_halo) # Might be end_halo

    # print(str(rank) + " x_pts: " + str(x_pts))
    # print(str(rank) + " y_pts: " + str(y_pts))

    X,Y = meshgrid(x_pts, y_pts)
    X = X.reshape(-1,)
    Y = Y.reshape(-1,)

    # Declare spatial discretization matrix
    # Task: what dimension should A be?  remember the spatial grid is from
    #       [h, 2h, ..., 1-h] x [h, 2h, ..., 1-h]
    #       Pass in the right size to poisson.
    # Task: in parallel, A will be just a processors local part of A

    # print(str(rank) + " row count halo: " + str(rowcount_halo))
    # print(str(rank) + " row length: " + str(n-2))

    A = poisson((rowcount_halo, n-2), format='csr')

    # Task: scale A by the grid size
    A *= 1./h**2

    # Declare initial condition
    #   This initial condition obeys the boundary condition.
    u0 = uexact(0, X, Y) 

    # Declare storage 
    # Task: what size should u and ue be?  u will store the numerical solution,
    #       and ue will store the exact solution.
    # Task in parallel: the size should only be for this processor's local portion of the domain.
    u = zeros((nt, A.shape[0]))
    ue = zeros((nt, A.shape[0]))

    # Set initial condition
    u[0,:] = u0
    ue[0,:] = u0

    ntimings = 5
    min_time = 1000000

    for m in range(ntimings):
        start = time.time()

        # Run time-stepping
        for i in range(1,nt):
        
            # Task: We need to store the exact solution so that we can compute the error
            ue[i,:] = uexact(i*ht, X, Y)
        
            # Task: Compute boundary contribution vector for the current time i*ht
            g = zeros((A.shape[0],))
        
            boundary_points = abs(Y - h) < 1e-12
            g[boundary_points] += (1./h**2)*uexact(i*ht, X[boundary_points], Y[boundary_points]-h)
        
            boundary_points = abs(Y - (1 - h)) < 1e-12
            g[boundary_points] += (1./h**2)*uexact(i*ht, X[boundary_points], Y[boundary_points]+h)
        
            boundary_points = abs(X - h) < 1e-12
            g[boundary_points] += (1./h**2)*uexact(i*ht, X[boundary_points]-h, Y[boundary_points])
        
            boundary_points = abs(X - (1 - h)) < 1e-12
            g[boundary_points] += (1./h**2)*uexact(i*ht, X[boundary_points]+h, Y[boundary_points])

            # Backward Euler
            u[i,:] = euler_backward(A, u[i-1,:], ht, f(i*ht, X, Y), g, start, start_halo, end, end_halo, n-2, comm) # n-2 might be rowcount_halo

        end = time.time()
        min_time = min(end-start, min_time)

    # Compute L2-norm of the error at final time
    e = (u - ue).reshape(-1,) 
    enorm = ((ht*(h**2))*(vector_norm(e, start, start_halo, end, end_halo, n-2, comm))) # (sum(e**2)))**(1./2.)
    # Task: compute the L2 norm over space-time here.  
    #       In serial this is just one line.  
    #       In parallel, write a helper function.
    print("Nt, N, Error is:  " + str(nt) + ",  " + str(n) + ",  " + str(enorm))
    error.append(enorm)
    timings.append(min_time)
    efficiency.append((timings[0]/(min_time*nt)))

    # You can turn this on to visualize the solution.  Possibly helpful for debugging.
    # if True:
        # pyplot.figure(1)
        # pyplot.imshow(u[0,:].reshape(n-2,n-2), origin='lower', extent=(0, 1, 0, 1))
        # pyplot.colorbar()
        # pyplot.xlabel('X')
        # pyplot.ylabel('Y')
        # pyplot.title("Initial Condition")
        
        # pyplot.figure(3)
        # pyplot.imshow(u[-1,:].reshape(n-2,n-2))
        # pyplot.colorbar()
        # pyplot.xlabel('X')
        # pyplot.ylabel('Y')
        # pyplot.title("Solution at final time")
       
        # pyplot.figure(4)
        # pyplot.imshow(uexact(T,X,Y).reshape(n-2,n-2))
        # pyplot.colorbar()
        # pyplot.xlabel('X')
        # pyplot.ylabel('Y')
        # pyplot.title("Exact Solution at final time")
        
        # pyplot.show()

# Plot convergence 
if(rank == 0):
    numpy.savetxt((str(comm.Get_size()/8)+'n_'+str(comm.Get_size())+'c_u_final_rank'+str(rank)+'.txt'), u)
    numpy.savetxt((str(comm.Get_size()/8)+'n_'+str(comm.Get_size())+'c_error_rank'+str(rank)+'.txt'), error)
    numpy.savetxt((str(comm.Get_size()/8)+'n_'+str(comm.Get_size())+'c_timings_rank'+str(rank)+'.txt'), timings)
    numpy.savetxt((str(comm.Get_size()/8)+'n_'+str(comm.Get_size())+'c_efficiency_rank'+str(rank)+'.txt'), efficiency)
    # pyplot.loglog(1./N_values, 1./N_values**2, '-ok')
    # pyplot.loglog(1./N_values, array(error), '-sr')
    # pyplot.tick_params(labelsize='large')
    # pyplot.xlabel(r'Spatial $h$', fontsize='large')
    # pyplot.ylabel(r'$||e||_{L_2}$', fontsize='large')
    # pyplot.legend(['Ref Quadratic', 'Computed Error'], fontsize='large')
    # pyplot.show()
    # pyplot.savefig('error.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0,)

# weak scaling is problem size by times
# strong scaling is threads by times
# scaling efficiency is number of nodes by times

