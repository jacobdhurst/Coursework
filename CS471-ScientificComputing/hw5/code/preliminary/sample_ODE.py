from scipy import *
from matplotlib import pyplot

def euler(f, y0, n, t0, T):
    '''
    Carry out Euler's method on [t0, T]
    for y' = f(t,y), nsteps n, and initial 
    condition y0.
    '''

    y = zeros((n,))
    y[0] = y0
    h = float(T - t0)/(n - 1.0)

    for i in range(1, n):
        # Task: insert formula for Euler's method
        y[i] = y[i-1] + h*f(i*h, y[i-1])

    return y

def RK2(f, y0, n, t0, T):
    '''
    Carry out RK2 on [t0, T]
    for y' = f(t,y), nsteps n, and initial 
    condition y0.
    '''

    y = zeros((n,))
    y[0] = y0
    h = float(T - t0)/(n - 1.0)

    for i in range(1, n):
        # Task: insert formula for RK2 
        k1 = f((i-1)*h, y[i-1])
        k2 = f((i-1)*h + h, y[i-1] + h*k1)
        y[i] = y[i-1] + (h/2.)*(k1 + k2)

    return y

def RK4(f, y0, n, t0, T):
    '''
    Carry out RK4 on [t0, T]
    for y' = f(t,y), nsteps n, and initial 
    condition y0.
    '''

    y = zeros((n,))
    y[0] = y0
    h = float(T - t0)/(n - 1.0)

    for i in range(1, n):        
        # Task: insert formula for RK4
        k1 = f((i-1)*h, y[i-1])
        k2 = f((i-1)*h + (h/2.), y[i-1] + (h/2.)*k1)
        k3 = f((i-1)*h + (h/2.), y[i-1] + (h/2.)*k2)
        k4 = f((i-1)*h + h, y[i-1] + h*k3)

        y[i] = y[i-1] + (h/6.)*(k1 + 2*k2 + 2*k3 + k4)

    return y



def f(t, y):
    return t*y + t**3

def yexact(t):
    return 3*exp(t**2.0 / 2.0) - t**2 - 2.0

t0 = 0.0
T = 1.0
y0 = 1.0
n = 5
yE = euler(f, y0, n, t0, T)
yRK2 = RK2(f, y0, n, t0, T)
yRK4 = RK4(f, y0, n, t0, T)

pyplot.figure(1)
pts = linspace(t0, T, n)
manypts = linspace(t0, T, 2000)
pyplot.plot(pts, yE, linewidth='2') 
pyplot.plot(pts, yRK2, linewidth='4') 
pyplot.plot(pts, yRK4, linewidth='2') 
pyplot.plot(manypts, yexact(manypts), linewidth='2') 
pyplot.xlabel(r'$X$', fontsize='large')
pyplot.ylabel(r'$Y$', fontsize='large')
pyplot.legend(['Euler', 'RK2', 'RK4', 'Exact'], fontsize='large') 
pyplot.savefig('lec20_compare_E_RK2_RK4.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0,)

EulerError = zeros((10,))
RK2Error = zeros((10,))
RK4Error = zeros((10,))
ns = 10*arange(1,100,10)
for i,n in enumerate(ns):
    
    h = float(T - t0) / (n-1.0)
    yE = euler(f, y0, n, t0, T)
    yRK2 = RK2(f, y0, n, t0, T)
    yRK4 = RK4(f, y0, n, t0, T)
    yExact = yexact(linspace(t0, T, n))

    EulerError[i] = sqrt(h*sum((yE - yExact)**2))
    RK2Error[i] = sqrt(h*sum((yRK2 - yExact)**2))
    RK4Error[i] = sqrt(h*sum((yRK4 - yExact)**2))
    

pyplot.figure(2)
pyplot.loglog(1.0/ns, EulerError, 'k') 
pyplot.loglog(1.0/ns, RK2Error, 'r') 
pyplot.loglog(1.0/ns, RK4Error, 'm') 
pyplot.loglog(1.0/ns, 1.0/ns, '--k') 
pyplot.loglog(1.0/ns, (1.0/ns)**2, '--r') 
pyplot.loglog(1.0/ns, (1.0/ns)**4, '--m') 
pyplot.xlabel(r'$h$', fontsize='large')
pyplot.ylabel('Error', fontsize='large')
#pyplot.xticks() 
pyplot.legend(['Euler', 'RK2', 'RK4', r'$h$', r'$h^2$', r'$h^4$'], fontsize='large') 

pyplot.savefig('Compare_error_E_RK2_RK4.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0,)

