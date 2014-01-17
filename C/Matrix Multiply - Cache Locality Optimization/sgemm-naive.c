void sgemm( int m, int n, float *A, float *C )
{
    for( int k = 0; k < n; k++)
	for( int j = 0; j < m; j++ ) 
	    for( int i = 0; i < m; i++ ) 
		C[i+j*m] += A[i+k*m] * A[j+k*m];
}
