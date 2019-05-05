from numpy import *
from scipy import *
from matplotlib import pyplot
import matplotlib.animation as manimation
import os, sys

# HW5 solution by Jacob

def RK4(f, y, t, dt, food_flag, alpha, gamma_1, gamma_2, kappa, rho, delta):
    '''
    Carry out a step of RK4 from time t using dt
    
    Input
    -----
    f:  Right hand side value function
    y:  state vector
    t:  current time
    dt: time step size
    
    food_flag:  0 or 1, depending on the food location
    alpha:      Parameter from homework PDF
    gamma_1:    Parameter from homework PDF
    gamma_2:    Parameter from homework PDF
    kappa:      Parameter from homework PDF
    rho:        Parameter from homework PDF
    delta:      Parameter from homework PDF
    

    Output
    ------
    Return updated vector y, according to RK4 formula
    '''

    # Task: Fill in the RK4 formula
    k1 = f(y, t, food_flag, alpha, gamma_1, gamma_2, kappa, rho, delta)
    k2 = f(y + (dt/2.)*k1, t + (dt/2.), food_flag, alpha, gamma_1, gamma_2, kappa, rho, delta)
    k3 = f(y + (dt/2.)*k2, t + (dt/2.), food_flag, alpha, gamma_1, gamma_2, kappa, rho, delta)
    k4 = f(y + dt*k3, t + dt, food_flag, alpha, gamma_1, gamma_2, kappa, rho, delta)

    return y + (dt/6.)*(k1 + 2*k2 + 2*k3 + k4)

def RHS(y, t, food_flag, alpha, gamma_1, gamma_2, kappa, rho, delta):
    '''
    Define the right hand side of the ODE

    '''
    N = y.shape[0]
    f = zeros_like(y)
    
    # Task:  Fill this in by assigning values to f
    # the leader
    f[0][0] = food(gamma_1, y, 0, 0) + predator(y, 0, 0)
    f[0][1] = food(gamma_1, y, 0, 1) + predator(y, 0, 1)
    # the smelly
    f[1][0] = leader(gamma_2, y, 1, 0) + predator(y, 1, 0) + center(kappa, y, 1, 0, N)# + repellant(rho, delta, y, 1, 0)
    f[1][1] = leader(gamma_2, y, 1, 1) + predator(y, 1, 1) + center(kappa, y, 1, 1, N)# + repellant(rho, delta, y, 1, 1) 
    # the rest
    for i in range(2, N):
        f[i][0] = leader(gamma_2, y, i, 0) + center(kappa, y, i, 0, N) + repellant(rho, delta, y, i, 0) \
                + predator(y, i, 0) + smelly(y, i, 0)
        f[i][1] = leader(gamma_2, y, i, 1) + center(kappa, y, i, 1, N) + repellant(rho, delta, y, i, 1) \
                + predator(y, i, 1) + smelly(y, i, 1)

    return f

def food(gamma_1, y, i, j):
    if food_flag == 0: C = (0.0, 0.0)
    elif food_flag == 1: C = (sin(alpha*t), cos(alpha*t))
    return gamma_1*(C[j] - y[i][j])

def leader(gamma_2, y, i, j):
    return gamma_2*(y[0][j] - y[i][j])

def center(kappa, y, i, j, n):
    return kappa*((sum(y[:,j])/float(n)) - y[i][j])
    
def repellant(rho, delta, y, i, j):
    f = 0.0
    neighbors = zeros((5,2))
    
    # get the indices of the closest five
    temp = argsort(sum(abs(y - y[i]), axis=1))
    for k in range(5):
        neighbors[k] = y[temp[k]]

    # formula for repellant force
    for k in range(5):
        f += rho*((y[i][j] - neighbors[k][j]) / (((y[i][j] - neighbors[k][j])**2)+delta))
    
    return f

def smelly(y, i, j):
    return -beta*(y[1][j] - y[i][j])

def predator(y, i, j):
    # lemniscate function - figure 8
    if pred_flag == 0: C = (0.0, 0.0)
    elif pred_flag == 1: C = (cos(alpha*t)/(1+sin(alpha*t)**2), \
                              cos(alpha*t)*sin(alpha*t)/(1+sin(alpha*t)**2))
    return -gamma_3*(C[j] - y[i][j])

##
# Set up problem domain
t0 = 0.0        # start time
T = 10.0        # end time
nsteps = 50     # number of time steps

# Task:  Experiment with N, number of birds
N = 30 #10, 30, 100

# Task:  Experiment with the problem parameters, and understand what they do
dt = (T - t0) / (nsteps-1.0)
gamma_1 = 2.0 # food attraction : -1.0, 2.0, 5.0
gamma_2 = 8.0 # leader charisma : 4.0-10.0
gamma_3 = 1.0 # predator intimidation : 0.0-2.0
alpha = 0.4 # period of C : 0.2, 0.4, 8.0
kappa = 4.0 # safety factor : 2.0, 4.0, 8.0
beta = 4.0 # smelly factor : 2.0-8.0
rho = 2.0 # repelling magnitude : 1.0, 2.0, 4.0
delta = 0.5 # reducing repulsion : 0.25, 0.5, 1.0
food_flag = 1   # food_flag == 0: C(x,y) = (0.0, 0.0)
                # food_flag == 1: C(x,y) = (sin(alpha*t), cos(alpha*t))
pred_flag = 1   # pred_flag == 0: C(x,y) = (0.0., 0.0)
                # pred_flag == 1: C(x,y) = parametric equation for lemniscate

# Intialize problem
y = rand(N,2)
flock_diam = zeros((nsteps,))

# Initialize the Movie Writer
FFMpegWriter = manimation.writers['ffmpeg']
writer = FFMpegWriter(fps=15)
fig = pyplot.figure(0)
pp, = pyplot.plot([],[], 'k+') 
rr, = pyplot.plot([],[], 'r+')
sm, = pyplot.plot([],[], 'y+')
pr, = pyplot.plot([],[], 'bo')
fo, = pyplot.plot([],[], 'go')
pyplot.xlabel(r'$X$', fontsize='large')
pyplot.ylabel(r'$Y$', fontsize='large')
pyplot.xlim(-3,3)       # you may need to adjust this, if your birds fly outside of this box!
pyplot.ylim(-3,3)       # you may need to adjust this, if your birds fly outside of this box!


# Begin writing movie frames
with writer.saving(fig, "movie.mp4", dpi=1000):

    # First frame
    pp.set_data(y[1:,0], y[1:,1]) 
    rr.set_data(y[0,0], y[0,1])
    sm.set_data(y[1,0], y[1,1])
    #pr.set_data(0.0, 0.0)
    pr.set_data(cos(alpha*t0)/(1+sin(alpha*t0)**2),cos(alpha*t0)*sin(alpha*t0)/(1+sin(alpha*t0)**2))
    #fo.set_data(0.0, 0.0)
    fo.set_data(sin(alpha*t0),cos(alpha*t0))

    writer.grab_frame()

    t = t0
    for step in range(nsteps):
        
        # Task: Fill these two lines in
        y = RK4(RHS, y, t, dt, food_flag, alpha, gamma_1, gamma_2, kappa, rho, delta) 

        # our flock diameter is approximated by the distance between the leader and farthest from the flock
        # get the farthest bird from the leader
        # center = mean(y, axis=0) 
        farthest = y[max(argsort(sum(abs(y - y[0]), axis=1)))]
        # apply distance formula 
        flock_diam[step] = sqrt((farthest[0] - y[0][0])**2 + (farthest[1] - y[0][1])**2)
        
        # Movie frame
        pp.set_data(y[:,0], y[:,1]) 
        rr.set_data(y[0,0], y[0,1])
        sm.set_data(y[1,0], y[1,1])
        #pr.set_data(0.0, 0.0)
        pr.set_data(cos(alpha*t)/(1+sin(alpha*t)**2), cos(alpha*t)*sin(alpha*t)/(1+sin(alpha*t)**2))
        #fo.set_data(0.0, 0.0)
        fo.set_data(sin(alpha*t),cos(alpha*t))
        writer.grab_frame()
        
        t += dt

# Task: Plot flock diameter
n = arange(nsteps)
pyplot.clf()
pyplot.plot(n, flock_diam)
pyplot.xlabel(r'$step$', fontsize='large')
pyplot.ylabel(r'$flock diameter$', fontsize='large') 
pyplot.show()
