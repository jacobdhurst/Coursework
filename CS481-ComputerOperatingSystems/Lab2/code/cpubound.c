#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

/*
Alarm handler to terminate after an amount of time.
*/
static void ALARMhandler(int sig)
{
    printf("Stopping CPU bound process.\n");
    exit(EXIT_SUCCESS);
}

/*
CPU bound process, inefficiently calculates fibonacci sequence.
Run by "taskset -c 0 ./cpubound <timing>".
*/
int main(int argc, char *argv[]) 
{
    printf("Starting CPU bound process. PID=%d.\n", getpid());
    signal(SIGALRM, ALARMhandler);
    alarm(atoi(argv[1]));

    volatile unsigned long long fz, fy, fx;
    fx = 0;
    fy = 1;
    while(1)
    {
        fz = fy + fx;
        fx = fy;
        fy = fz;
    }
    exit(EXIT_SUCCESS);
}
