import os
import sys

time = sys.argv[1]
samples = int(sys.argv[2])

os.system('rm unthreaded coarse-grained fine-grained')# *results*')

os.system('gcc -o unthreaded unthreaded.c')
os.system('gcc -pthread -o coarse-grained coarse-grained.c')
os.system('gcc -pthread -o fine-grained fine-grained.c')

for i in range(samples):
    os.system('./unthreaded ' + time)# + ' > unthreaded-results' + str(i))
    os.system('./coarse-grained ' + time)# + ' > coarse-grained-results' + str(i))
    os.system('./fine-grained ' + time)# + ' > fine-grained-results' + str(i))