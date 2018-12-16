from scipy import *

def newton(f, x0, tol, maxiter):
    '''
    Newton's method of root finding
    
    input
    -----
    f           : function to find zero of
                  Interface for f:  f(x, 0)  returns f(x)
                                    f(x, 1)  returns f'(x)
    x0          : initial guess
    <maxiter>   : cap for iterations
    <tol>       : desired tolerance
    
    output
    ------
    data   : array of approximate roots, added second column for errors
    '''
 
    data = zeros((maxiter,2))
    x = x0
    
    for i in range(maxiter):
        xold = x
        x = x - f(x, 0)/f(x, 1)
        e = abs(x - xold)

        s = "Iter  " + str(i) + "  Approx. root  " + str(x) + "  Error  " + str(e)
        print(s)
    
        data[i,0] = x
        data[i,1] = e

        # Insert some logic for halting, if the tolerance is met
        if e < tol:
           break
    ##
    # end for-loop

    return data[0:(i+1)]
