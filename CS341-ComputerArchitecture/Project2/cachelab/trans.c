/*
 * Jacob Hurst - Jhurst
 *
 * trans.c - Matrix transpose B = A^T
 *
 * Each transpose function must have a prototype of the form:
 * void trans(int M, int N, int A[N][M], int B[M][N]);
 *
 * A transpose function is evaluated by counting the number of misses
 * on a 1KB direct mapped cache with a block size of 32 bytes.
 */ 
#include <stdio.h>
#include "cachelab.h"

#define min(a, b) (((a) < (b)) ? (a) : (b))

void trans_32(int M, int N, int A[N][M], int B[M][N]);
void trans_64(int M, int N, int A[N][M], int B[M][N]);
void trans_general(int M, int N, int A[N][M], int B[M][N]);

int is_transpose(int M, int N, int A[N][M], int B[M][N]);

/* 
 * transpose_submit - This is the solution transpose function that you
 *     will be graded on for Part B of the assignment. Do not change
 *     the description string "Transpose submission", as the driver
 *     searches for that string to identify the transpose function to
 *     be graded. 
 *
 *     Notes: 
 *     At most, 12 local int variables per transpose function (no tricks to sidestep this).
 *     If helper functions are used, no more than 12 loocal variables can be on the stack.
 *     No recursion is allowed.
 *     Must not modify array A.
 *     No malloc or arrays.
 *     It is okay to have specialized operations depending on the test sizes.
 *     Valgrind and the cache simulator will be used to evaluate performance via test-trans.c.
 *     You can register variant test solutions below with registerFunctions().
 *     Experimented with different block sizes to find optimal block size for transposes (4 for 64, 8 for 32, 16 for general).
 */
char transpose_submit_desc[] = "Transpose submission";
void transpose_submit(int M, int N, int A[N][M], int B[M][N])
{
    if(M == 32 && N == 32) trans_32(M, N, A, B);
    else if(M == 64 && N == 64) trans_64(M, N, A, B);
    else trans_general(M, N, A, B);
}

void trans_32(int M, int N, int A[N][M], int B[M][N])
{
    int i, j, ii, jj, blocksize, index, save;

    blocksize = 8;

    //blocked matrix transpose
    for(i = 0; i < N; i+=blocksize) {
        for(j = 0; j < M; j+=blocksize) {
	    //step through each block
	    //process the block row-wise then col-wise
            for(jj = j; jj < j+blocksize; jj++) {
                for(ii = i; ii < i+blocksize; ii++) {
                    if(ii == jj) {
			save = A[jj][ii];
                        index = ii;
                    } else B[ii][jj] = A[jj][ii];
                }
                if (i == j) {
                    B[index][index] = save;
                }
            }
        }
    }
}

void trans_64(int M, int N, int A[N][M], int B[M][N])
{
    int i, j, ii, jj, blocksize, index, save;

    blocksize = 4;

    //blocked matrix transpose
    for(i = 0; i < N; i+=blocksize) {
        for(j = 0; j < M; j+=blocksize) {
            //step through each block
            //process the block row-wise then col-wise
            for(jj = j; jj < j+blocksize; jj++) {
                for(ii = i; ii < i+blocksize; ii++) {
                    if(ii == jj) {
                        save = A[jj][ii];
                        index = ii;
                    } else B[ii][jj] = A[jj][ii];
                }
                if (i == j) {
                    B[index][index] = save;
                }
            }
        }
    }

}

void trans_general(int M, int N, int A[N][M], int B[M][N])
{
    int i, j, ii, jj, blocksize;

    blocksize = 16;

    //blocked matrix transpose
    for(i = 0; i < N; i+=blocksize) {
        for(j = 0; j < M; j+=blocksize) {
            //step through each block
            if(i + blocksize <= N && j + blocksize <= M) {
                //process a full block row-wise then col-wise
                for(jj = 0; jj < blocksize; jj++) {
                    for(ii = 0; ii < blocksize; ii++) {
                        B[j + jj][i + ii] = A[i + ii][j + jj];
                    }
                }
            }
            else {
                //process a partial block col-wise then row-wise
		//min function makes sure we stay in bounds
                for(ii = i; ii < min(N, i + blocksize); ii++) {
                    for(jj = j; jj < min(M, j + blocksize); jj++) {
                        B[jj][ii] = A[ii][jj];
                    }
                }
            }
        }
    }
}

/* 
 * You can define additional transpose functions below. We've defined
 * a simple one below to help you get started. 
 */ 

/* 
 * trans - A simple baseline transpose function, not optimized for the cache.
 */
char trans_desc[] = "Simple row-wise scan transpose";
void trans(int M, int N, int A[N][M], int B[M][N])
{
    int i, j, tmp;

    for (i = 0; i < N; i++) {
        for (j = 0; j < M; j++) {
            tmp = A[i][j];
            B[j][i] = tmp;
        }
    }    

}

/*
 * registerFunctions - This function registers your transpose
 *     functions with the driver.  At runtime, the driver will
 *     evaluate each of the registered functions and summarize their
 *     performance. This is a handy way to experiment with different
 *     transpose strategies.
 */
void registerFunctions()
{
    /* Register your solution function */
    registerTransFunction(transpose_submit, transpose_submit_desc); 

    /* Register any additional transpose functions */
    registerTransFunction(trans, trans_desc); 

}

/* 
 * is_transpose - This helper function checks if B is the transpose of
 *     A. You can check the correctness of your transpose by calling
 *     it before returning from the transpose function.
 */
int is_transpose(int M, int N, int A[N][M], int B[M][N])
{
    int i, j;

    for (i = 0; i < N; i++) {
        for (j = 0; j < M; ++j) {
            if (A[i][j] != B[j][i]) {
                return 0;
            }
        }
    }
    return 1;
}

