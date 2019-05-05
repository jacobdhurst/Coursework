from scipy import *
from matplotlib import pyplot
from trapezoidal import trapezoidal
from gauss import gauss

def f1(x):
    return exp(cos((pi) * x))

def f2(x):
    return exp(cos((pi**2) * x))

fs = [f1, f2]
upper = 1
lower = -1
tol = 10**(-16)
i = 0

errorT = zeros(1000)
errorG = zeros(1000)
errorT[0] = errorG[0] = 1
errorT[1] = errorG[1] = 1

for f in fs:
    #eT = eG = 0
    #eTn = eGn = 0
    for n in range(2, 1000):
        errorT[n] = abs(trapezoidal(f, lower, upper, n+1) - trapezoidal(f, lower, upper, n))
        #eT += 1
        #eTn = n
        #if errorT[n] < tol:
        #    break

    for n in range(2, 1000):
        errorG[n] = abs(gauss(f, n+1) - gauss(f, n))
        #eG += 1
        #eGn = n
        #if errorG[n] < tol:
        #    break

    i += 1
    n = linspace(0, 1000, 1000)

    print("\ntrapezoidal:")
    #print("The trapezoidal rule on function " + str(i) + " required " + str(eT) + " evaluations with n=2,3,..," + str(eTn) + " trapezoids to satisfy the error tolerance " + str(tol) + ".")
    print(errorT)
    pyplot.figure(i-1)
    pyplot.loglog(n, n**(-1), n, n**(-2), n, n**(-3), n, errorT)
    pyplot.xlabel('n')
    pyplot.ylabel('error')
    pyplot.ylim(10**(-16), 10**(1))
    pyplot.legend(['n^(-1)', 'n^(-2)', 'n^(-3)', 'errorT'])  # pyplot.show() #
    pyplot.savefig('trap' + str(i) + '.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0)


    print("\ngauss:")
    #print("Gauss quadrature on function " + str(i) + " required " + str(eG) + " evaluations with n=2,3,..," + str(eGn) + " weights/nodes to satisfy the error tolerance " + str(tol) + ".")
    print(errorG)
    pyplot.figure(i+1)
    pyplot.loglog(n, n**(-n), n, errorG, n, (1.5)**(-1.5*n), n, (2)**(-2*n), (2.5)**(-2.5*n))
    pyplot.xlabel('n')
    pyplot.ylabel('error')
    pyplot.ylim(10**(-16), 10**(1))
    pyplot.legend(['n^(-n)', 'errorG', 'C=1.5, alpha=1.5', 'C=2, alpha=2', 'C=2.5, alpha=2.5'])  # pyplot.show() #
    pyplot.savefig('gauss' + str(i) + 'updated.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0)

    #savetxt('trap' + str(i) + '.tex', errorT[999], fmt='%.5e', delimiter='  &  ', newline='\\\\\n')
    #savetxt('gauss' + str(i) + '.tex', errorG[999], fmt='%.5e', delimiter='  &  ', newline='\\\\\n')
