from scipy import *
from scipy import integrate
from lglnodes import *

def gauss(f, n):
    (nodes, weights) = lglnodes(n)
    integral = sum(f(nodes)*weights)

    return integral