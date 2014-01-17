#ifndef TRIANGLE_H
#define TRIANGLE_H

#include "Particle.h"
#include "Shader.h"
#include "Spring.h"

class Particle;

class Triangle {
public:
	Triangle(Particle *p1, Particle *p2, Particle *p3);
	Triangle() {};

	Particle *_p1, *_p2, *_p3;

	bool _vis;

	void render(int shadeType, bool normalShade);
	void toggleVisibility(bool val);
	void accumTriNorm();
	void addWind(glm::vec3 wind);

	glm::vec3 getMid();

	
};

#endif