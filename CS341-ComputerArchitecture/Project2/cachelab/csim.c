#include "cachelab.h"
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <getopt.h>
#include <math.h>

/**
Jacob Hurst: Jhurst
CS 341L: Computer Architecture & Organization
Project 2: Cache Simulator
Given valgrind traces, simulate the number of cache hits/misses/evictions.

Notes:
Trace format: operation address,size(bytes accessed). 
I = Instruction load, L = Data load, S = Data store, M = Data modify.

M = 2^m unique addresses
S = 2^s, number of sets.
E is associativity (number of lines per set).
Each line consists of a data block of B = 2^b bytes (b is block offset).
Each cacheline has a valid bit, tag, and possibly LRU counter.
Cache size is C = S*E*B

Locate set index and check if any line in set has a matching tag.
Hit if any line in the set has a matching tag and if the valid bit is set to 1.
**/

int VERBOSE = 0;

int hitCount, missCount, evictionCount;

typedef struct CacheLine 
{
    int valid;
    long long tag;
    /*int lru; - opted to push recently used to front of cacheLine.
                 least recently used fall to back of cacheLine. */
} cacheLine;

cacheLine** cache;

void printCache(int S, int E);
void printUsage();

/**
 * Frees the cacheLine (S*E).
 */
void terminate(int S, int E)
{
    for(int i = 0; i < S; i++) free(cache[i]);
    free(cache);
}

/**
 * Helper function for simulate(), returns a mask to select n bits.
 */
long long getMask(int n)
{
    int mask = 0, pow = 1;
    for(int i = 0; i < n; i++)
    {
        mask += pow;
        pow *= 2;
    }
    return mask;
}

/**
 * Helper function for simulate(), performs the relevant actions in simulating cache behavior.
 */
char* evaluate(long long tagged, int setIndex, int E)
{
    cacheLine temp;
    int found = 0;
    char status[20] = "";

    //Search through each line in setIndex for a valid bit + matching tag.
    for(int i = 0; i < E; i++)
    {
        //printf("1:TEST\tcache[%d][%d]=%llx\n", setIndex, i, cache[setIndex][i].tag);
        if(cache[setIndex][i].valid == 1 && cache[setIndex][i].tag == tagged)
        {
            found = 1;
            strcat(status, "hit ");
            hitCount++;

	        //Move the current cache entry to the front, shift the others back.
	        //Simulating LRU policy.
            temp = cache[setIndex][i];

            for(int j = i; j > 0; j--)
            {
                //printf("2:TEST\tcache[%d][%d]=%llx\n",setIndex, j, cache[setIndex][j].tag);
                cache[setIndex][j] = cache[setIndex][j-1];
            }
            cache[setIndex][0] = temp;
            break;
        }
    }

    //If we did not find the entry, then we have a miss.
    if(found != 1)
    {
        strcat(status, "miss ");
        missCount++;

	    //If we just missed and the last entry of the cache is occupied, 
	    //then its time to evict.
	    //printf("3:TEST\tcache[%d][%d]=%llx\n", setIndex, E-1, cache[setIndex][E-1].tag);
        if(cache[setIndex][E-1].valid == 1)
        {
            strcat(status, "eviction ");
            evictionCount++;
        }

	    //If we have miss or miss eviction, we now shift the cache entries back 
	    //and insert the new entry. Simulating LRU policy.
        for(int j = E-1; j > 0; j--)
        {
            //printf("4:TEST\tcache[%d][%d]=%llx\n", setIndex, j, cache[setIndex][j].tag);
            cache[setIndex][j] = cache[setIndex][j-1];
   	    }
        cache[setIndex][0].valid = 1;
        cache[setIndex][0].tag = tagged;
        //cache[setIndex][0].lru += 1;
    }
    
    char* result = status;
    return result;
}

/**
 * Given an address, gets the tag, setIndex, and blockOffset. 
 * Evaluates a load or store operation only once, evaluates a modify operation twice.
 * Returns string of what occurred, ex: hit, miss, eviction, etc..
 */
char* simulate(int s, int E, int b, char operation, long long address, int size)
{
    long long tagged;
    int setIndex /*, blockOffset*/;
    char* result;

    tagged = ((getMask(8*sizeof(address)-(b+s)) << (b+s)) & address) >> (b+s);
    setIndex = ((getMask(s) << b) & address) >> b;
    /*blockOffset = getMask(b) & address;*/
    
    //If the operation is a load or store we only need to evaluate once.
    if(operation == 'L' || operation == 'S')
    {
        result = evaluate(tagged, setIndex, E);
    }
    //If the operation is a modify we need to evaluate twice, once to load & once to store.
    else if(operation == 'M')
    {
    	result = evaluate(tagged, setIndex, E);
    	result = evaluate(tagged, setIndex, E);
    }
    return result;
}

/**
 * Mallocs a 2D array of a cacheLine struct.
 * Dimensions are cache[S][E], each cacheLine contains valid bit, tag, and LRU counter.
 */
void initialize(int s, int E, int b)
{       
    int S = pow(2, s);

    cache = (cacheLine**) malloc(S*sizeof(cacheLine*));
    for(int i = 0; i < S; i++)
    {
        cache[i] = (cacheLine*) malloc(E*sizeof(cacheLine));
	    for(int j = 0; j < E; j++)
	    {
	        cache[i][j].valid = 0;
	        cache[i][j].tag = -1;
	        //cache[i][j].lru = 0;
	    }
    }
}

/**
 * Helper method used to debug the cache, prints the entire cache and its contents.
 */
void printCache(int S, int E)
{
    for(int i = 0; i < S; i++)
    {
	    for(int j = 0; j < E; j++)
	    {
	        printf("Cache[%d][%d].valid = %d\tCache[%d][%d].tag = %llx\n", 
	                i, j, cache[i][j].valid, i, j, cache[i][j].tag);
	    }
    }
}

/**
 * Helper method to print the usage statement and exit the program.
 */
void printUsage()
{
    printf("./csim: Missing required command line argument\n"
          "Usage: ./csim [-hv] -s <num> -E <num> -b <num> -t <file>\n"
          "Options:\n"
          "  -h         Print this help message.\n"
          "  -v         Optional verbose flag.\n"
          "  -s <num>   Number of set index bits.\n"
          "  -E <num>   Number of lines per set.\n"
          "  -b <num>   Number of block offset bits.\n"
          "  -t <file>  Trace file.\n\n"
          "Examples:\n"
          "  linux>  ./csim-ref -s 4 -E 1 -b 4 -t traces/yi.trace\n"
          "  linux>  ./csim-ref -v -s 8 -E 2 -b 4 -t traces/yi.trace\n");
    exit(1);
}

/**
 * Main entry point for program, parses input, sets up data, & processes.
 */ 
int main(int argc, char **argv)
{
    if(argc < 9) printUsage();

    int c, s, E, b, size;
    unsigned long long address;
    char operation;
    char* status;
    char *filename = argv[argc-1];
    FILE *fp = fopen(filename, "r");

    //Parsing the input with getopt and fscanf.
    while((c = getopt(argc, argv, "hvs:E:b:t:")) != -1)
    {
	    switch(c)
	    {
	        case 'h': printUsage();
            case 'v': 
                VERBOSE = 1;
		        break;
	        case 's':
		        s = atoi(optarg);
	            break;
	        case 'E':
		        E = atoi(optarg);
		        break;
	        case 'b':
		        b = atoi(optarg);
		        break;
	        case 't':
                break;
	        default: printUsage();
	    }
    }
   
    //Initialize the cache.
    initialize(s, E, b);
    //printCache(pow(2,s), E);

    //Parsing the file and evaluating it line by line, ignoring instruction loads.
    while(fscanf(fp, " %c %llx,%d", &operation, &address, &size) != EOF)
    {
	    if(operation == 'I') continue;

	    status = simulate(s, E, b, operation, address, size);

        if(VERBOSE) printf("%c %llx,%d %s\n", operation, address, size, status);
    }
    fclose(fp);

    //Free the cache.
    terminate(pow(2, s), E);

    printSummary(hitCount, missCount, evictionCount);

    return 0;
}
