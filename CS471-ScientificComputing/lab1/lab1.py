from scipy import *
from matplotlib import pyplot
import numpy as np

# Task 1: look over these basic plotting commands
x = linspace(-1,1,120) # 120 evenly spaced numbers between -1 and 1
y = exp(1/2-sin(5*pi*x)) # our function

pyplot.figure(1) 
pyplot.plot(x,y)

# Task 2: Try these options to make figure more readable, one at a time
#
# As you update your figure, with new options, view it with 
#
# $ open -a preview lab1.png

pyplot.tick_params(labelsize='large')

pyplot.xlabel(r'$X$', fontsize='large')

# Now, to the same for pyplot.ylabel()
pyplot.ylabel(r'$Y$', fontsize='large')

# Reduce the number of xticks with "pyplot.xticks()"  
pyplot.xticks(np.arange(-1, 1, step=0.5))
# Use ">>> pyplot.xticks? " to understand the interface 

# Add a parameter to the legend command to make the fontsize large
pyplot.legend(['First Dataset']) 

pyplot.savefig('lab1.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0,)

# Task 3: Try pyplot.show() instead of pyplot.savefig()

# pyplot.show()

# Task 4: To understand loglog plots better, here are the commands from the
# last lecture.  Try making the loglog plot more readable, as above

pyplot.figure(2)
h = 2**-linspace(0,10,1000) 
pyplot.plot(h, h, h, h**2, h, h**3) 
pyplot.show()

