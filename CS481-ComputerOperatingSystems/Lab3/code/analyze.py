import matplotlib.pyplot as plt
import numpy as np
import sys

samples = range(int(sys.argv[1]))

unthreaded_access = []
unthreaded_insert = []
coarse_grained_access = []
coarse_grained_insert = []
fine_grained_access = []
fine_grained_insert = []

for i in samples:
    f1 = open('unthreaded-results'+str(i), 'r')
    f2 = open('coarse-grained-results'+str(i), 'r')
    f3 = open('fine-grained-results'+str(i), 'r')

    for j, line in enumerate(f1.readlines()):
        k = line.index(':')+1
        l = line.index('.')
        digit = int(line[k:l])
        if (j == 0): unthreaded_insert.append(digit)
        elif (j == 1): unthreaded_access.append(digit)

    for j, line in enumerate(f2.readlines()):
        k = line.index(':')+1
        l = line.index('.')
        digit = int(line[k:l])
        if (j == 0): coarse_grained_insert.append(digit)
        elif (j == 1): coarse_grained_access.append(digit)

    for j, line in enumerate(f3.readlines()):
        k = line.index(':')+1
        l = line.index('.')
        digit = int(line[k:l])
        if (j == 0): fine_grained_insert.append(digit)
        elif (j == 1): fine_grained_access.append(digit)

labels = ['Unthreaded Access','Unthreaded Insert','Coarse-grained Access','Coarse-grained Insert','Fine-grained Access','Fine-grained Insert']
avgs = [np.mean(unthreaded_access),np.mean(unthreaded_insert),np.mean(coarse_grained_access),np.mean(coarse_grained_insert),np.mean(fine_grained_access),np.mean(fine_grained_insert),]

plt.bar(range(len(labels)), avgs, align='center')
plt.xticks(range(len(labels)), labels)
plt.ylabel('Average count (' + sys.argv[1] + ' samples)')
plt.title('Comparison of threaded linked list efficiencies.')

plt.show()