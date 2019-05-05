#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <limits.h>

static int *malloc_wrap(size_t size)
{
    int *p = malloc(size);
    if (p) {
        printf("Successfully allocated %zu bytes from %p to %p.\n", size, p, p+size);
    }
    else {
        printf("Failed to allocated %zu bytes.\n", size);
    }
    return p;
}

int main()
{
    int len  = 6000000000/sizeof(int);
    int *p = malloc_wrap(6000000000);

    sleep(10);

    printf("Generating page faults by random reads/writes.\n");
    for(int i = 0; i < 1000000; i++)
    {
        p[rand() % len] = rand();
    }
    printf("Done generating faults.");
    fflush(stdout);

    sleep(10);
}
