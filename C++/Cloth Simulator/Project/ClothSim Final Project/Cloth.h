#ifndef CLOTH_H
#define CLOTH_H


#include <vector>
#include <iostream>
#include <fstream>
#include <cmath>
#include <sstream>

#ifdef _WIN32
#include <windows.h>
#else
#include <sys/time.h>
#endif

#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <omp.h>
 

#include <glm.hpp>
#include <GL/gl.h>
#include <GL/glut.h> 

#include "Particle.h"
#include "Spring.h"
#include "Ball.h"
#include "Shader.h"
#include "Triangle.h"



extern float damping;
extern float timestep;
extern float gravity;
extern float width;
extern float height;
extern float step;
extern glm::vec3 wind;
extern int num_iterations;
extern bool frozen_toggle;
extern int rotate_x;
extern int rotate_y;

using namespace std;

class Particle;
class Spring;
class Ball;
class Triangle;

static void printVec(glm::vec3 vec) {
	glm::vec3 b = vec;
	cout << b.x << "\t" << b.y << "\t" << b.z << endl;
}


class Cloth {
public:

	Cloth(float width, float height, float subdiv);
	Cloth() {};

	float _width, _height;
	int numdiv_x, numdiv_y;
	bool dragging;
	Particle* draggedPart;

	
	void updateCloth(Ball* ball);
	void addWindToCloth(glm::vec3 wind);
	void addTriangle(Particle *p1, Particle *p2, Particle *p3);
	void dragCloth(glm::vec3 clickPos);
	void releaseDragged();

	static glm::vec3 calcTriNorm(Particle *p1, Particle *p2, Particle *p3);

	
	Particle* getParticle(int x, int y) {return particle_vector[y*numdiv_x + x];}
	Spring* link(Particle* a, Particle* b, int type);

	
	vector<Particle*> particle_vector;
	vector<Spring*> spring_vector;
	vector<Triangle*> tri_vector;

private:
	float sub_param;
	float spring_k;
};

#endif