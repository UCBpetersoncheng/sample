#include <stdio.h>
#include <stdlib.h>
#include <nmmintrin.h>
#include <omp.h>

void sgemm( int m, int n, float *A, float *C )
{
  #pragma omp parallel
  {
  int i,j,k;
  int n5 = (n/5)*5;
  int m16 = (m/16)*16;
  int m4 = (m/4)*4;
  
  float *aj;
  float *ai, *ai4,*ai8, *ai12;
  float *cijm_0, *cijm_4, *cijm_8, *cijm_12;
  int jm, km0, km1, km2, km3, km4;
  __m128 t, ikm, mulps, cijm, addps;
  __m128 t2, ikm2, mulps2, cijm2, addps2;
  __m128 t3, ikm3, mulps3, cijm3, addps3;
  __m128 t4, ikm4, mulps4, cijm4, addps4;
  __m128 t5, ikm5, mulps5, cijm5, addps5;

  #pragma omp for
  for(j = 0; j < m; j++) {
      aj = A+j;
      jm = j*m;
      for(k = 0; k < n5; k+= 5) {
	  km0 = k*m;
	  km1 = km0 + m;
	  km2 = km1 + m;
	  km3 = km2 + m;
	  km4 = km3 + m;
  
	  t = _mm_load1_ps(aj+km0);
	  t2 = _mm_load1_ps(aj+km1);
	  t3 = _mm_load1_ps(aj+km2);
	  t4 = _mm_load1_ps(aj+km3);
	  t5 = _mm_load1_ps(aj+km4);
	  for (i = 0; i < m16; i+= 16) {
	      ai = A+i;
	      ai4 = ai+4;
	      ai8 = ai+8;
	      ai12 = ai+12;
	      
	      cijm_0 = C+i+jm;
	      cijm_4 = cijm_0 + 4;
	      cijm_8 = cijm_0 + 8;
	      cijm_12 = cijm_0 + 12;
	      
	      cijm = _mm_loadu_ps(cijm_0);
	      cijm2 = _mm_loadu_ps(cijm_4);
	      cijm3 = _mm_loadu_ps(cijm_8);
	      cijm4 = _mm_loadu_ps(cijm_12);
	      
	      ikm = _mm_loadu_ps(ai+km0);
	      ikm2 = _mm_loadu_ps(ai4+km0);
	      ikm3 = _mm_loadu_ps(ai8+km0);
	      ikm4 = _mm_loadu_ps(ai12+km0);
	      mulps = _mm_mul_ps(ikm, t);
	      mulps2 = _mm_mul_ps(ikm2, t);
	      mulps3 = _mm_mul_ps(ikm3, t);
	      mulps4 = _mm_mul_ps(ikm4, t);
	      addps = _mm_add_ps(cijm, mulps);
	      addps2 = _mm_add_ps(cijm2, mulps2);
	      addps3 = _mm_add_ps(cijm3, mulps3);
	      addps4 = _mm_add_ps(cijm4, mulps4);

	      
	      ikm = _mm_loadu_ps(ai+km1);
	      ikm2 = _mm_loadu_ps(ai4+km1);
	      ikm3 = _mm_loadu_ps(ai8+km1);
	      ikm4 = _mm_loadu_ps(ai12+	km1);
	      mulps = _mm_mul_ps(ikm, t2);
	      mulps2 = _mm_mul_ps(ikm2, t2);
	      mulps3 = _mm_mul_ps(ikm3, t2);
	      mulps4 = _mm_mul_ps(ikm4, t2);
	      addps = _mm_add_ps(addps, mulps);
	      addps2 = _mm_add_ps(addps2, mulps2);
	      addps3 = _mm_add_ps(addps3, mulps3);
	      addps4 = _mm_add_ps(addps4, mulps4);


	      ikm = _mm_loadu_ps(ai+km2);
	      ikm2 = _mm_loadu_ps(ai4+km2);
	      ikm3 = _mm_loadu_ps(ai8+km2);
	      ikm4 = _mm_loadu_ps(ai12+km2);
	      mulps = _mm_mul_ps(ikm, t3);
	      mulps2 = _mm_mul_ps(ikm2, t3);
	      mulps3 = _mm_mul_ps(ikm3, t3);
	      mulps4 = _mm_mul_ps(ikm4, t3);
	      addps = _mm_add_ps(addps, mulps);
	      addps2 = _mm_add_ps(addps2, mulps2);
	      addps3 = _mm_add_ps(addps3, mulps3);
	      addps4 = _mm_add_ps(addps4, mulps4);


	      ikm = _mm_loadu_ps(ai+km3);
	      ikm2 = _mm_loadu_ps(ai4+km3);
	      ikm3 = _mm_loadu_ps(ai8+km3);
	      ikm4 = _mm_loadu_ps(ai12+km3);
	      mulps = _mm_mul_ps(ikm, t4);
	      mulps2 = _mm_mul_ps(ikm2, t4);
	      mulps3 = _mm_mul_ps(ikm3, t4);
	      mulps4 = _mm_mul_ps(ikm4, t4);
	      addps = _mm_add_ps(addps, mulps);
	      addps2 = _mm_add_ps(addps2, mulps2);
	      addps3 = _mm_add_ps(addps3, mulps3);
	      addps4 = _mm_add_ps(addps4, mulps4);

	      ikm = _mm_loadu_ps(ai+km4);
	      ikm2 = _mm_loadu_ps(ai4+km4);
	      ikm3 = _mm_loadu_ps(ai8+km4);
	      ikm4 = _mm_loadu_ps(ai12+km4);
	      mulps = _mm_mul_ps(ikm, t5);
	      mulps2 = _mm_mul_ps(ikm2, t5);
	      mulps3 = _mm_mul_ps(ikm3, t5);
	      mulps4 = _mm_mul_ps(ikm4, t5);
	      addps = _mm_add_ps(addps, mulps);
	      addps2 = _mm_add_ps(addps2, mulps2);
	      addps3 = _mm_add_ps(addps3, mulps3);
	      addps4 = _mm_add_ps(addps4, mulps4);

	      
	      _mm_storeu_ps(cijm_0, addps);
	      _mm_storeu_ps(cijm_4, addps2);
	      _mm_storeu_ps(cijm_8, addps3);
	      _mm_storeu_ps(cijm_12, addps4);
	          
	  }
	  /** I LOOP FRINGE. MAIN K. */
	  for (i = m16; i < m4; i+=4) {
	      ai = A+i;
	      cijm_0 = C+i+jm;

	      cijm = _mm_loadu_ps(cijm_0);
	      ikm = _mm_loadu_ps(ai+km0);
	      ikm2 = _mm_loadu_ps(ai+km1);
	      ikm3 = _mm_loadu_ps(ai+km2);
	      ikm4 = _mm_loadu_ps(ai+km3);
	      ikm5 = _mm_loadu_ps(ai+km4);
	      mulps = _mm_mul_ps(ikm, t);
	      mulps2 = _mm_mul_ps(ikm2, t2);
	      mulps3 = _mm_mul_ps(ikm3, t3);
	      mulps4 = _mm_mul_ps(ikm4, t4);
	      mulps5 = _mm_mul_ps(ikm5, t5);
	      addps = _mm_add_ps(cijm, mulps);
	      addps = _mm_add_ps(addps, mulps2);
	      addps = _mm_add_ps(addps, mulps3);
	      addps = _mm_add_ps(addps, mulps4);
	      addps = _mm_add_ps(addps, mulps5);
	      
	      _mm_storeu_ps(cijm_0, addps);
	      
	  }
	  for (i = m4; i < m; i++) {
	      ai = A+i;
	      cijm_0 = C+i+jm;

	      cijm = _mm_load_ss(cijm_0);    
	      ikm = _mm_load_ss(ai+km0);
	      ikm2 = _mm_load_ss(ai+km1);
	      ikm3 = _mm_load_ss(ai+km2);
	      ikm4 = _mm_load_ss(ai+km3);
	      ikm5 = _mm_load_ss(ai+km4);

	      mulps = _mm_mul_ss(t, ikm);
	      mulps2 = _mm_mul_ss(t2, ikm2);
	      mulps3 = _mm_mul_ss(t3, ikm3);
	      mulps4 = _mm_mul_ss(t4, ikm4);
	      mulps5 = _mm_mul_ss(t5, ikm5);

	      addps = _mm_add_ss(cijm, mulps);
	      addps = _mm_add_ss(addps, mulps2);
	      addps = _mm_add_ss(addps, mulps3);
	      addps = _mm_add_ss(addps, mulps4);
	      addps = _mm_add_ss(addps, mulps5);
	      _mm_store_ss(cijm_0, addps);
	  }
      }

      /** K LOOP FRINGE. */
      for(k = n5; k < n; k++) {
	  km0 = k*m;
	  
	  t = _mm_load1_ps(aj+km0);
	  for (i = 0; i < m16; i+= 16) {
	      ai = A+i;
	      ai4 = ai+4;
	      ai8 = ai+8;
	      ai12 = ai+12;
	      cijm_0 = C+i+jm;
	      cijm_4 = cijm_0 + 4;
	      cijm_8 = cijm_0 + 8;
	      cijm_12 = cijm_0 + 12;
	      
	      cijm = _mm_loadu_ps(cijm_0);
	      cijm2 = _mm_loadu_ps(cijm_4);
	      cijm3 = _mm_loadu_ps(cijm_8);
	      cijm4 = _mm_loadu_ps(cijm_12);
	      
	      ikm = _mm_loadu_ps(ai+km0);
	      ikm2 = _mm_loadu_ps(ai4+km0);
	      ikm3 = _mm_loadu_ps(ai8+km0);
	      ikm4 = _mm_loadu_ps(ai12+km0);
 
	      mulps = _mm_mul_ps(ikm, t);
	      mulps2 = _mm_mul_ps(ikm2, t);
	      mulps3 = _mm_mul_ps(ikm3, t);
	      mulps4 = _mm_mul_ps(ikm4, t);

	      addps = _mm_add_ps(cijm, mulps);
	      addps2 = _mm_add_ps(cijm2, mulps2);
	      addps3 = _mm_add_ps(cijm3, mulps3);
	      addps4 = _mm_add_ps(cijm4, mulps4);

	      
	      _mm_storeu_ps(cijm_0, addps);
	      _mm_storeu_ps(cijm_4, addps2);
	      _mm_storeu_ps(cijm_8, addps3);
	      _mm_storeu_ps(cijm_12, addps4);
		
	  }
	  for (i = m16; i < m4; i+= 4) {
	      ai = A+i;
	      cijm_0 = C+i+jm;
	      
	      cijm = _mm_loadu_ps(cijm_0);	      
	      ikm = _mm_loadu_ps(ai+km0);
	      mulps = _mm_mul_ps(ikm, t);
	      addps = _mm_add_ps(cijm, mulps);
	      
	      _mm_storeu_ps(cijm_0, addps);

		
	  }
	  /** I LOOP FRINGE. FRINGE K. */
	  for (i = m4; i < m; i++) {
	      ai = A+i;
	      cijm_0 = C+i+jm;
	      
	      cijm = _mm_load_ss(cijm_0);    
	      ikm = _mm_load_ss(ai+km0);
	      mulps = _mm_mul_ss(t, ikm);
	      addps = _mm_add_ss(cijm, mulps);
	      _mm_store_ss(cijm_0, addps);

	  }
      }	
  }
  }
}
