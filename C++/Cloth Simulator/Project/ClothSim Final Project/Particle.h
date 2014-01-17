#ifndef PARTICLE_H
#define PARTICLE_H

#include "Cloth.h"

using namespace std;

class Spring;


class Particle {

private:
	float mass;
	glm::vec3 pos;
	glm::vec3 old_pos;
	glm::vec3 acceleration;
	   //NOT normalized
	


public:
	bool frozen;              //if true, this particle should not move at all
	bool isDrag;
	static glm::vec3 base_accel;
	glm::vec3 accum_normal;

	Particle(glm::vec3 position);
	Particle() {};

	void addForce(glm::vec3 force);
	void particleTimeStep();
	void resetAccel();
	void resetNormal();
	void freeze();
	void updatePos(glm::vec3 delta);
	void addToAccumNormal(glm::vec3 n);
	void fixPos(glm::vec3 position, float delta_x, float delta_y);

	static void changeBase(glm::vec3 force);
	static void link(Particle *a, Particle *b);

	glm::vec3 getPos();
	glm::vec3 getNormal();

	vector<Spring*> neighbors;
};
#endif