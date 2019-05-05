from scipy import *
from newton import newton
from matplotlib import pyplot


# Define your functions to test Newton on.  You'll need to
# define two other functions for f(x) = x and f(x) = sin(x) + cos(x**2)

def f1(x, d):
    if d == 0:
        return x ** 2
    elif d == 1:
        return 2 * x


def f2(x, d):
    if d == 0:
        return x
    elif d == 1:
        return 1


def f3(x, d):
    if d == 0:
        return sin(x) + cos(x ** 2)
    elif d == 1:
        return cos(x) - 2 * x * sin(x ** 2)


# We use this function to determine whether our function converges linearly.
# If the values are consistently between 1 & 0 we can say the function converges linearly.
def calculate_k1(data):
    k1_values = []
    for i in range(0, len(data) - 2):
        err1 = abs(data[i + 2] - data[i + 1])
        err2 = abs(data[i + 1] - data[i])
        k1_values.append(err1 / err2)
    return k1_values


def calculate_k2(data):
    k2_values = []
    for i in range(0, len(data) - 2):
        err1 = abs(data[i + 2] - data[i + 1])
        err2 = abs(data[i + 1] - data[i]) ** 2
        k2_values.append(err1 / err2)
    return k2_values


fcn_list = [f1, f2, f3]

i = 0
x0 = -0.5
for fcn in fcn_list:
    data = newton(fcn, x0, (10 ** -10), 50)

    # Here, you can post-process data, and test convergence rates
    h = linspace(1, 0, len(data[:, 1]))  # len(k))
    pyplot.figure(i + 1)
    pyplot.loglog(h, h, h, h ** 2, h, data[:, 1])
    pyplot.xlabel('1 > x > 0')
    pyplot.ylabel('Error in approximation')
    pyplot.legend(['linear', 'quadratic', 'data'])  # pyplot.show() #
    pyplot.savefig('fig' + str(i) + '.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0)

    k1 = calculate_k1(data[:, 0])
    k2 = calculate_k2(data[:, 0])

    h = linspace(0, 1, len(k1))
    pyplot.figure(i + 4)
    pyplot.plot(h, h, h, k1)
    pyplot.xlabel('0 < x < 1')
    pyplot.ylabel('linearly convergent: k values')
    pyplot.legend(['y=x', 'data'])
    pyplot.savefig('k1_fig' + str(i) + '.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0)

    h = linspace(0, 1, len(k2))
    pyplot.figure(i + 4)
    pyplot.plot(h, h, h, k2)
    pyplot.xlabel('0 < x < 1')
    pyplot.ylabel('quadratically convergent: k values')
    pyplot.legend(['y=x', 'data'])
    pyplot.savefig('k2_fig' + str(i) + '.png', dpi=500, format='png', bbox_inches='tight', pad_inches=0.0)

    # Also, use your skills from the last lab, and use savetxt() to dump
    # data and/or convergence rates to a text file
    savetxt('data' + str(i) + '.tex', data, fmt='%.5e', delimiter='  &  ', newline='\\\\\n')
    savetxt('k1_data' + str(i) + '.tex', k1, fmt='%.5e', delimiter='  &  ', newline='\\\\\n')
    savetxt('k2_data' + str(i) + '.tex', k2, fmt='%.5e', delimiter='  &  ', newline='\\\\\n')

    i += 1
