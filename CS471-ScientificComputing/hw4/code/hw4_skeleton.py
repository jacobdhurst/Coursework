from scipy import *
import numpy
import sys
import time
from threading import Thread
from matplotlib import pyplot
from poisson import poisson

DEBUG = False

def l2norm(e, h):
    '''
    Take L2-norm of e
    '''
    
    # ensure e has a compatible shape for taking a dot-product
    e = e.reshape(-1)
    
    # Task:
    # Return the L2-norm, i.e., the square roof of the integral of e^2
    # Assume a uniform grid in x and y, and apply the midpoint rule.
    # Assume that each grid point represents the midpoint of an equally sized region
    return ((h**2)*(sum(e**2)))**(1./2.)


def compute_fd(n, nt, k, f, fpp_num):
    '''
    Compute the numeric second derivative of the function 'f'

    Input
    -----
    n   <int>       :   Number of grid points in x and y for global problem
    nt  <int>       :   Number of threads
    k   <int>       :   My thread number
    f   <func>      :   Function to take second derivative of
    fpp_num <array> :   Global array of size n**2


    Output
    ------
    fpp_num will have this thread's local portion of the second derivative
    written into it


    Notes
    -----
    We do a 1D domain decomposition.  Each thread 'owns' the k(n/nt) : (k+1)(n/nt) rows
    of the domain.  
    
    For example, 
    Let the global points in the x-dimension be [0, 0.33, 0.66, 1.0] 
    Let the global points in the y-dimension be [0, 0.33, 0.66, 1.0] 
    Let the number of threads be two (nt=2)
    
    Then for the k=0 case (for the 0th thread), the rows of the 
    domain 'owned' are
    y = 0,    and x = [0, 0.33, 0.66, 1.0]
    y = 0.33, and x = [0, 0.33, 0.66, 1.0]
    
    Then For the k = 1, case, the rows of the domain owned are
    y = 0.66, and x = [0, 0.33, 0.66, 1.0]
    y = 1.0,  and x = [0, 0.33, 0.66, 1.0]

    We assume that n/nt divides evenly.

    '''

    # Task: 
    # Compute start, end, start_halo, and end_halo
    start = k*(n/nt)
    end = (k+1)*(n/nt)
    
    # Task:
    # Halo regions essentially expand a thread's local domain to include enough
    # information from neighboring threads to carry out the needed computation.
    # For the above example, that means including 
    #   - the y=0.66 row of points for the k=0 case
    #   - the y=0.33 row of points for the k=1 case
    if(k != 0): start_halo = start - 1
    else: start_halo = start

    if(k != (nt-1)): end_halo = end + 1
    else: end_halo = end

    rowcount = end - start
    rowcount_halo = end_halo - start_halo

    # Construct local CSR matrix.  Here, you're given that function for free
    A = poisson((rowcount_halo, n), format='csr')
    h = 1./(n-1)
    A *= 1./h**2

    # Task:
    # Inspect a row or two of A, and verify that it's the correct 5 point stencil
    # This is done towards the bottom of the method so I could print other useful information with it.

    # Task:
    # Construct grid of evenly spaced points over this thread's halo region 
    x_pts = linspace(0,1,n)
    if(end_halo == end): y_pts = linspace(start_halo * h, 1, (rowcount_halo))
    else: y_pts = linspace(start_halo * h, end * h, (rowcount_halo))

    # Task:
    # There is no coding to do here, but examime how meshgrid works and 
    # understand how it gives you the correct uniform grid.
    X,Y = meshgrid(x_pts, y_pts)
    X = X.reshape(-1,)
    Y = Y.reshape(-1,)

    # Compute local portion of f 
    f_vals = f(X,Y)
    
    # Task:
    # Compute the correct range of output values for this thread
    if(nt == 1):
        output = (A*f_vals)
    elif(k == 0):
        output = (A*f_vals)[:n*rowcount]
    elif(k == nt-1):
        output = (A*f_vals)[-n*rowcount:]
    else:
        output = (A*f_vals)[n:-n]

    # Task:
    # Set the output array
    fpp_num[n*rowcount*k:n*rowcount*(k+1)] = output

    if(DEBUG): sys.stderr.write(
        "\n************compute_fd************" +
        "\nthread = " + str(k+1) + " of " + str(nt) +
        "\nrange  = (" + str(start) + ", " + str(end) + "), " +
        "halo_range = (" + str(start_halo) + ", " + str(end_halo) + ")" +
        "\n\nx = " + str(x_pts) + "\ny = " + str(y_pts) +
        "\n\nA[0,:] indices = " + str(A[0,:].indices) +
        "\nA[0,:] data    = " + str(A[0,:].data) +
        "\n\nA[16,:] indices = " + str(A[16,:].indices) +
        "\nA[16,:] data    = " + str(A[16,:].data) +
        "\n\nA[17,:] indices = " + str(A[17,:].indices) +
        "\nA[17,:] data    = " + str(A[17,:].data) +
        "\n\nA shape        = " + str(A.shape) + "\n" +
        "**************************************\n")

def fcn(x,y):
    '''
    This is the function we are studying
    '''
    return cos((x+1)**(1./3.) + (y+1)**(1./3.)) + sin((x+1)**(1./3.) + (y+1)**(1./3.))

def fcnpp(x,y):
    '''
    This is the second derivative of the function we are studying (sum of fxx and fyy)
    '''
    # Task:
    # Fill this function in with the correct second derivative.  You end up with terms like
    # -cos((x+1)**(1./3.) + (y+1)**(1./3.))*(1./9.)*(x+1)**(-4./3)
    return ((-sin((x+1)**(1./3.)+(y+1)**(1./3.)))/(9*(x+1)**(4./3.))) + (2*(sin((x+1)**(1./3.)+(y+1)**(1./3.)))/(9*(x+1)**(5./3.))) + \
           ((-cos((x+1)**(1./3.)+(y+1)**(1./3.)))/(9*(x+1)**(4./3.))) + (2*(-cos((x+1)**(1./3.)+(y+1)**(1./3.)))/(9*(x+1)**(5./3.))) + \
           ((-sin((x+1)**(1./3.)+(y+1)**(1./3.)))/(9*(y+1)**(4./3.))) + (2*(sin((x+1)**(1./3.)+(y+1)**(1./3.)))/(9*(y+1)**(5./3.))) + \
           ((-cos((x+1)**(1./3.)+(y+1)**(1./3.)))/(9*(y+1)**(4./3.))) + (2*(-cos((x+1)**(1./3.)+(y+1)**(1./3.)))/(9*(y+1)**(5./3.)))


##
# Here are three problem size options for running.  The instructor has chosen these
# for you.
option = 1
if option == 1:
    # Choose this if doing a final run on CARC
    NN = array([840*6])
    num_threads = [1,2,3,4,5,6,7,8]
elif option == 2:
    # Choose this for printing convergence plots on your laptop/lab machine,
    # and for initial runs on CARC.  
    # You may want to start with just num_threads=[1] and debug the serial case first.
    NN = 210*arange(1,6)
    num_threads = [1,2,3]
elif option == 3:
    # Choose this for code development and debugging on your laptop/lab machine
    # You may want to start with just num_threads=[1] and debug the serial case first.
    NN = array([6])
    num_threads = [1, 2, 3]
else:
    sys.stderr.write("Incorrect Option!")

##
# Begin main computation loop
##

# Task:
# Initialize your data arrays
error = zeros((len(num_threads), len(NN)))
timings = zeros((len(num_threads), len(NN)))
efficiency = zeros((len(num_threads), len(NN)))

# Loop over various numbers of threads
for i,nt in enumerate(num_threads):
    # Loop over various problem sizes 
    for j,n in enumerate(NN):
        
        # Task:
        # Initialize output array
        fpp_numeric = zeros((n*n))
        
        # Task:
        # Choose the number of timings to do for each thread number, domain size combination 
        ntimings = 10

        # Time over 10 tries
        min_time = 10000
        for m in range(ntimings):

            # Compute fpp numerically in the interior of each thread's domain
            # compute_fd(n, nt, 0, fcn, fpp_numeric)
           
            t_list = []
            for k in range(nt):
                # Task:
                # Finish this call to Thread(), passing in the correct target and arguments
                t_list.append(Thread(target=compute_fd, args=(n, nt, k, fcn, fpp_numeric)))

            start = time.time()
            
            # Task:
            # Launch and Join Threads
            for t in t_list:
                t.start()

            for t in t_list:
                t.join()
            
            end = time.time()
            min_time = min(end-start, min_time)
        
        ##
        # End loop over timings

        # Construct grid of evenly spaced points for a reference evaluation of
        # the double derivative
        h = 1./(n-1)
        pts = linspace(0,1,n)
        X,Y = meshgrid(pts, pts)
        X = X.reshape(-1,)
        Y = Y.reshape(-1,) 
        fpp = fcnpp(X,Y)

        # Account for domain boundaries.  
        #
        # The boundary_points array is a Boolean array, that acts like a
        # mask on an array.  For example if boundary_points is True at 10
        # points and False at 90 points, then x[boundar_points] will be a
        # length 10 array at those True locations
        boundary_points = (Y == 0)
        fpp_numeric[boundary_points] += (1./h**2)*fcn(X[boundary_points], Y[boundary_points]-h)
        
        # Task:
        # Account for the domain boundaries at Y == 1, X == 0, X == 1
        boundary_points = (Y == 1)
        fpp_numeric[boundary_points] += (1./h**2)*fcn(X[boundary_points], Y[boundary_points]+h)
        boundary_points = (X == 0)
        fpp_numeric[boundary_points] += (1./h**2)*fcn(X[boundary_points]-h, Y[boundary_points])
        boundary_points = (X == 1)
        fpp_numeric[boundary_points] += (1./h**2)*fcn(X[boundary_points]+h, Y[boundary_points])        

        # Task:
        # Compute error
        e = fpp_numeric - fpp
        error[i,j] = l2norm(e, h)
        timings[i,j] = min_time
        efficiency[i, j] = (timings[0,j]/(timings[i,j]*nt))

        if(DEBUG): print("\n\n************results************" +
              "\n\nproblem size = " + str(n) +
              "\n\nthread(s)    = " + str(nt) +
              "\n\nmin_time     = " + str(min_time) + "s" +
              "\n\nf_approx     = \n" + str(fpp_numeric) +
              "\n\nf_exact      = \n" + str(fpp) +
              "\n\nerror        = " + str(error[i,j]) +
              "\n\n*******************************")
    ##
    # End Loop over various grid-sizes
        
    # Task:
    # Generate and save plot showing convergence for this thread number
    # Do not try to plot on CARC. 
    #pyplot.loglog(NN, error[i])
    #pyplot.loglog(NN, NN**(-2.))
    #pyplot.xlabel('NN')
    #pyplot.ylabel('error')
    #pyplot.legend(['Finite Difference Convergence (' + str(nt) + ' thread(s))', 'Quadratic Reference'])
    #pyplot.show()
    #pyplot.savefig('error'+str(i)+'.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0,)

##
# End Loop over various thread counts

# Save timings for future use
savetxt('num_threads.txt', num_threads)
savetxt('timings.txt', timings)
savetxt('efficiency.txt', efficiency)

# Strong Scaling Study
#pyplot.plot(num_threads, timings[0])
#pyplot.xlabel('Threads')
#pyplot.ylabel('Time(s)')
#pyplot.title('Strong Scaling Timings')
#pyplot.savefig('SSTiming.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0)

#pyplot.plot(num_threads, efficiency[0])
#pyplot.xlabel('Threads')
#pyplot.ylabel('Efficiency')
#pyplot.title('Strong Scaling Efficiency')
#pyplot.savefig('SSEfficiency.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0)
