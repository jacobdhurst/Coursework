import datetime
import subprocess
import threading
import psutil
import time
import os
import sys

'''
Globals
'''
runs = int(sys.argv[1])
runtime = float(sys.argv[2])
totaltime = runs*runtime
cmds = ["taskset -c 0 ./iobound "+str(runtime), "taskset -c 0 ./cpubound "+str(runtime), "taskset -c 0 ./mixedbound "+str(runtime)]
pids = []

'''
Calls OS to run given task, stores results.
'''
def os_run(cmd):
    proc = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True)
    while proc.poll() is None:
        line = str(proc.stdout.readline())[2:-3]
        if line: 
            print(line.strip())
            if(line.find("PID") != -1): 
                pids.append(parse_pid(line))

'''
Helper function to get the PID of the process being analyzed.
'''
def parse_pid(line):
    i = line.find('=')
    pid = line[i+1:-1]
    return int(pid)
    
'''
Cleans up old files then compiles and runs each process.
'''
def main():
    os.system('echo y | rm cpubound iobound mixedbound perf.data')
    print("Initializing experiment, this should take "+str(totaltime/60)+" minutes...")
    print("Current time: ", datetime.datetime.now().strftime("%H:%M:%S"))
    start = time.time()
    proc = subprocess.Popen('sudo perf sched record -- sleep '+str(totaltime), shell=True)
    os.system("gcc -o iobound iobound.c")
    os.system("gcc -o cpubound cpubound.c")
    os.system("gcc -o mixedbound mixedbound.c")
    
    for i in range(runs):
        procs = []
        for cmd in cmds:
            procs.append(threading.Thread(target=os_run, args=(cmd,)))
        for proc in procs:
            proc.start()
        for proc in procs:
            proc.join()

        print("Treatment " + str(i+1) + " of " + str(runs) + " complete.")      
        
    print("Experiment concluded.")
    print("Current time: ", datetime.datetime.now().strftime("%H:%M:%S"))
    while (time.time() - start) <= (totaltime): continue
    pidstr = ''
    for pid in pids:
        pidstr += str(pid) + ','
    print('Run \'sudo perf sched timehist -s --pid='+pidstr[:-1]+'\' to view results.')
    
main()
