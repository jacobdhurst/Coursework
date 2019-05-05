import os
import subprocess
import sys
import time

os.system("gcc pt_req.c -Wall -O2")

for i in range(0, 30):
    os.system("cat /proc/meminfo | grep MemFree > mem_before"+str(i))
    os.system("cat /proc/meminfo | grep PageTables > pt_before"+str(i))
    
    proc = subprocess.Popen(["./a.out"], stdout=subprocess.PIPE)
    
    time.sleep(5)

    os.system("cat /proc/meminfo | grep MemFree > mem_during"+str(i))
    os.system("cat /proc/meminfo | grep PageTables > pt_during"+str(i))

    time.sleep(10)
    
    os.system("cat /proc/meminfo | grep MemFree > mem_after"+str(i))
    os.system("cat /proc/meminfo | grep PageTables > pt_after"+str(i))
    
    print("MemFree & PageTables before malloc versus after malloc.")
    os.system("diff -y mem_before"+str(i)+" mem_during"+str(i))
    os.system("diff -y pt_before"+str(i)+" pt_during"+str(i))

    print("MemFree & PageTables after malloc versus after access.")
    os.system("diff -y mem_during"+str(i)+" mem_after"+str(i))
    os.system("diff -y pt_during"+str(i)+" pt_after"+str(i))

    time.sleep(5)
    proc.communicate()
