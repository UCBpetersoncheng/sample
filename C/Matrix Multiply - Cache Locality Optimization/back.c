#include <stdio.h>
#include <stdlib.h>
#include <nmmintrin.h>

void sgemm( int m, int n, float *A, float *C )
{
  int i,j,k;
  __m128 transposeItem,transpose, ikm, mulps, cijm, addps;
  __m128 tItem2, t2, ikm2, mulps2, ikm3, tItem3, t3, mulps3, ikm4, tItem4, t4, mulps4;
  int mod16 = (m % 16 == 0 || n % 16 == 0);

  if (mod16) {
      for(j = 0; j < m; j++){	
	  for(k = 0; k < (n/4)*4; k+= 4) {
	      transposeItem = _mm_load_ss(A+j+k*m);
	      transpose = _mm_load1_ps(A+j+k*m);
	      /*tItem2 = _mm_load_ss(A+j+(k+1)*m);
	      t2 = _mm_load1_ps(A+j+(k+1)*m);
	      tItem3 = _mm_load_ss(A+j+(k+2)*m);
	      t3 = _mm_load1_ps(A+j+(k+2)*m);
	      tItem4 = _mm_load_ss(A+j+(k+3)*m);
	      t4 = _mm_load1_ps(A+j+(k+3)*m);*/
	      for (i = 0; i < (m/16)*16; i+= 16) {
		  ikm = _mm_loadu_ps(A+i+k*m);	
		  mulps = _mm_mul_ps(ikm, transpose);
		  cijm = _mm_loadu_ps(C+i+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  _mm_storeu_ps(C+i+j*m, addps);


		  ikm = _mm_loadu_ps(A+i+4+k*m);
		  ikm2 = _mm_loadu_ps(A+i+4+(k+1)*m);
		  ikm3 = _mm_loadu_ps(A+i+4+(k+2)*m);
		  ikm4 = _mm_loadu_ps(A+i+4+(k+3)*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  mulps2 = _mm_mul_ps(ikm2, t2);
		  mulps3 = _mm_mul_ps(ikm3, t3);
		  mulps4 = _mm_mul_ps(ikm4, t4);
		  cijm = _mm_loadu_ps(C+i+4+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  addps = _mm_add_ps(addps, mulps2);
		  addps = _mm_add_ps(addps, mulps3);
		  addps = _mm_add_ps(addps, mulps4);
		  _mm_storeu_ps(C+i+4+j*m, addps);

		  ikm = _mm_loadu_ps(A+i+8+k*m);
		  ikm2 = _mm_loadu_ps(A+i+8+(k+1)*m);
		  ikm3 = _mm_loadu_ps(A+i+8+(k+2)*m);
		  ikm4 = _mm_loadu_ps(A+i+8+(k+3)*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  mulps2 = _mm_mul_ps(ikm2, t2);
		  mulps3 = _mm_mul_ps(ikm3, t3);
		  mulps4 = _mm_mul_ps(ikm4, t4);
		  cijm = _mm_loadu_ps(C+i+8+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  addps = _mm_add_ps(addps, mulps2);
		  addps = _mm_add_ps(addps, mulps3);
		  addps = _mm_add_ps(addps, mulps4);
		  _mm_storeu_ps(C+i+8+j*m, addps);

		  ikm = _mm_loadu_ps(A+i+12+k*m);
		  ikm2 = _mm_loadu_ps(A+i+12+(k+1)*m);
		  ikm3 = _mm_loadu_ps(A+i+12+(k+2)*m);
		  ikm4 = _mm_loadu_ps(A+i+12+(k+3)*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  mulps2 = _mm_mul_ps(ikm2, t2);
		  mulps3 = _mm_mul_ps(ikm3, t3);
		  mulps4 = _mm_mul_ps(ikm4, t4);
		  cijm = _mm_loadu_ps(C+i+12+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  addps = _mm_add_ps(addps, mulps2);
		  addps = _mm_add_ps(addps, mulps3);
		  addps = _mm_add_ps(addps, mulps4);
		  _mm_storeu_ps(C+i+12+j*m, addps);
	          
	      }
	      for (i = (m/16)*16; i < m; i++) {
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+k*m), transposeItem)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+1)*m), tItem2)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+2)*m), tItem3)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+3)*m), tItem4)));
	      }
	  }
	  /** K LOOP FRINGE. */
	  for(k = (n/4)*4; k < n; k++) {
	      transposeItem = _mm_load_ss(A+j+k*m);
	      transpose = _mm_load1_ps(A+j+k*m);
	      for (i = 0; i < (m/16)*16; i+= 16) {
		  ikm = _mm_loadu_ps(A+i+k*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  cijm = _mm_loadu_ps(C+i+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  _mm_storeu_ps(C+i+j*m, addps);
	      
		  ikm = _mm_loadu_ps(A+i+4+k*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  cijm = _mm_loadu_ps(C+i+4+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  _mm_storeu_ps(C+i+4+j*m, addps);

		  ikm = _mm_loadu_ps(A+i+8+k*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  cijm = _mm_loadu_ps(C+i+8+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  _mm_storeu_ps(C+i+8+j*m, addps);

		  ikm = _mm_loadu_ps(A+i+12+k*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  cijm = _mm_loadu_ps(C+i+12+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  _mm_storeu_ps(C+i+12+j*m, addps);
		  
	      }	
	      for (i = (m/16)*16; i < m; i++) {
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+k*m), transposeItem)));
	      }
	  }	
      }
  } else {
      for(j = 0; j < m; j++){	
	  for(k = 0; k < (n/4)*4; k+= 4) {
	      transposeItem = _mm_load_ss(A+j+k*m);
	      transpose = _mm_load1_ps(A+j+k*m);
	      tItem2 = _mm_load_ss(A+j+(k+1)*m);
	      t2 = _mm_load1_ps(A+j+(k+1)*m);
	      tItem3 = _mm_load_ss(A+j+(k+2)*m);
	      t3 = _mm_load1_ps(A+j+(k+2)*m);
	      tItem4 = _mm_load_ss(A+j+(k+3)*m);
	      t4 = _mm_load1_ps(A+j+(k+3)*m);
	      for (i = 0; i < (m/12)*12; i+= 12) {
		  ikm = _mm_loadu_ps(A+i+k*m);
		  ikm2 = _mm_loadu_ps(A+i+(k+1)*m);
		  ikm3 = _mm_loadu_ps(A+i+(k+2)*m);
		  ikm4 = _mm_loadu_ps(A+i+(k+3)*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  mulps2 = _mm_mul_ps(ikm2, t2);
		  mulps3 = _mm_mul_ps(ikm3, t3);
		  mulps4 = _mm_mul_ps(ikm4, t4);
		  cijm = _mm_loadu_ps(C+i+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  addps = _mm_add_ps(addps, mulps2);
		  addps = _mm_add_ps(addps, mulps3);
		  addps = _mm_add_ps(addps, mulps4);
		  _mm_storeu_ps(C+i+j*m, addps);
	      
		  ikm = _mm_loadu_ps(A+i+4+k*m);
		  ikm2 = _mm_loadu_ps(A+i+4+(k+1)*m);
		  ikm3 = _mm_loadu_ps(A+i+4+(k+2)*m);
		  ikm4 = _mm_loadu_ps(A+i+4+(k+3)*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  mulps2 = _mm_mul_ps(ikm2, t2);
		  mulps3 = _mm_mul_ps(ikm3, t3);
		  mulps4 = _mm_mul_ps(ikm4, t4);
		  cijm = _mm_loadu_ps(C+i+4+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  addps = _mm_add_ps(addps, mulps2);
		  addps = _mm_add_ps(addps, mulps3);
		  addps = _mm_add_ps(addps, mulps4);
		  _mm_storeu_ps(C+i+4+j*m, addps);

		  ikm = _mm_loadu_ps(A+i+8+k*m);
		  ikm2 = _mm_loadu_ps(A+i+8+(k+1)*m);
		  ikm3 = _mm_loadu_ps(A+i+8+(k+2)*m);
		  ikm4 = _mm_loadu_ps(A+i+8+(k+3)*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  mulps2 = _mm_mul_ps(ikm2, t2);
		  mulps3 = _mm_mul_ps(ikm3, t3);
		  mulps4 = _mm_mul_ps(ikm4, t4);
		  cijm = _mm_loadu_ps(C+i+8+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  addps = _mm_add_ps(addps, mulps2);
		  addps = _mm_add_ps(addps, mulps3);
		  addps = _mm_add_ps(addps, mulps4);
		  _mm_storeu_ps(C+i+8+j*m, addps);
	          
	      }
	      for (i = (m/12)*12; i < (m/4)*4; i++) {
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+k*m), transposeItem)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+1)*m), tItem2)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+2)*m), tItem3)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+3)*m), tItem4)));
	      }
	      for (i = (m/4)*4; i < m; i++) {
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+k*m), transposeItem)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+1)*m), tItem2)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+2)*m), tItem3)));
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+(k+3)*m), tItem4)));
	      }
	  }
	  /** K LOOP FRINGE. */
	  for(k = (n/4)*4; k < n; k++) {
	      transposeItem = _mm_load_ss(A+j+k*m);
	      transpose = _mm_load1_ps(A+j+k*m);
	      for (i = 0; i < (m/12)*12; i+= 12) {
		  ikm = _mm_loadu_ps(A+i+k*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  cijm = _mm_loadu_ps(C+i+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  _mm_storeu_ps(C+i+j*m, addps);
	      
		  ikm = _mm_loadu_ps(A+i+4+k*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  cijm = _mm_loadu_ps(C+i+4+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  _mm_storeu_ps(C+i+4+j*m, addps);

		  ikm = _mm_loadu_ps(A+i+8+k*m);
		  mulps = _mm_mul_ps(ikm, transpose);
		  cijm = _mm_loadu_ps(C+i+8+j*m);
		  addps = _mm_add_ps(cijm, mulps);
		  _mm_storeu_ps(C+i+8+j*m, addps);
	      }	
	      for (i = (m/12)*12; i < m; i++) {
		  _mm_store_ss(C+i+j*m,
			       _mm_add_ss(_mm_load_ss(C+i+j*m),
					  _mm_mul_ss(_mm_load_ss(A+i+k*m), transposeItem)));
	      }
	  }	
      }
  }	
}
