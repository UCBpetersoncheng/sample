#include <stdio.h>
#include <omp.h>
#include <nmmintrin.h>

void sgemm( int m, int n, float *A, float *C)
{
    #pragma omp parallel
    {
    int block = n;
    if (block > 64) {
	block = 64;
    }
    int numT = omp_get_num_threads();
    int t_ID = omp_get_thread_num();
    for( int j = 0; j < m; j+=block){
	for( int k = 0; k < n; k+=block) {
	    for( int i = 0; i < m; i+=numT*block) {
		for (int jj = j; jj < j+block && jj < m; jj++) {
		    for (int kk = k; kk<k+block && kk < n; kk++) {
			for (int ii = i+(t_ID*block); ii<i+((t_ID+1)*block) && ii < m; ii++) {
			    C[ii+jj*m] += A[ii+kk*m] * A[jj+kk*m];
			}
		    }
		}
	    }
	}
    }
    }
	/*
	#pragma omp for
	for( int j = 0; j < m; j++)
	    for( int k = 0; k < n; k++ ) 
		for( int i = 0; i < m; i++ ) 
		    C[i+j*m] += A[i+k*m] * A[j+k*m];
    }*/
}
