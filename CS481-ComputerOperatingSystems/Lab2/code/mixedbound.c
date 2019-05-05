#define _GNU_SOURCE

#include <dirent.h>
#include <fcntl.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>

/*
Alarm handler to terminate after an amount of time.
*/
static void ALARMhandler(int sig)
{
    printf("Stopping mixed bound process.\n");
    exit(EXIT_SUCCESS);
}

/*
Modified cpu bound code to calculate fibonacci for a portion of the total time.
*/
int cpu()
{
    clock_t start = clock();
    int total = 5, current = 0;
    
    volatile unsigned long long fz, fy, fx;
    fx = 0;
    fy = 1;
    
    while(current < total)
    {
        clock_t elapsed = clock() - start;
        current = elapsed * 1000 / CLOCKS_PER_SEC;
        
        fz = fy + fx;
        fx = fy;
        fy = fz;
    }
}

/*
IO bound process, sleeps for 5 milliseconds and does a small amount of CPU bound processing.
Run by "taskset -c 0 ./iobound <timing>"
*/
int main(int argc, char *argv[]) 
{
    printf("Starting mixed bound process. PID=%d.\n", getpid());
    signal(SIGALRM, ALARMhandler);
    alarm(atoi(argv[1]));

    while(1)
    {
        sleep(0.005); /* 1ms io intervals. */
        cpu();        /* 5ms cpu intervals */
    }
    exit(EXIT_SUCCESS);
}
