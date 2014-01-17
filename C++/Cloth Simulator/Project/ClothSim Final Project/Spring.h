#ifndef SPRING_H
#define SPRING_H

#define STRUCT 0
#define SHEAR 1
#define BEND 2


#include "Particle.h"

class Particle;

class Spring {
public:
	Spring(Particle *part1, Particle *part2, float _k, int spring_type);
	Spring() {};

	Particle *p1, *p2;     //the two particles on either end of this spring
	float ed, k;

	float solveSpring();    //called many times per frame, sets the two particles connected by this constraint to their equilibrium positions. Look at parallelising this method
	
	int getType() { return spring_type; }
	
private:
	int spring_type;
};
#endif