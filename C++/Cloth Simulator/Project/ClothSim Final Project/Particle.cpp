#include "Particle.h"


glm::vec3 Particle::base_accel = glm::vec3(0);


Particle::Particle(glm::vec3 position) {
	pos = position;
	isDrag = false;
	old_pos = position;
	acceleration = glm::vec3(0);
	mass = 1;
	frozen = false;
	accum_normal = glm::vec3(0);
}

void Particle::addForce(glm::vec3 force) {acceleration += force/mass;}   //increments a particle's acceleration by adding in another force

void Particle::resetAccel() {acceleration = base_accel;}

void Particle::resetNormal() {accum_normal = glm::vec3(0);}

void Particle::freeze() {frozen = true;}

void Particle::changeBase(glm::vec3 force) {base_accel = force;} 

glm::vec3 Particle::getPos() {return pos;}

void Particle::fixPos(glm::vec3 position, float delta_x, float delta_y) {
	if (position.z >= pos.z - 20 && position.z <= pos.z + 20 || isDrag) {
		glm::vec3 oldpos = pos;
		glm::vec3 newpos = glm::vec3(oldpos.x + delta_x, oldpos.y + delta_y, oldpos.z);
		pos = newpos;
		frozen = true;
		isDrag = true;
	}
}

glm::vec3 Particle::getNormal() {return accum_normal;}   //NOT normalized

void Particle::updatePos(glm::vec3 delta) { if (!frozen || frozen_toggle) { pos += delta;}}

/*increases a particle's accumulated normal, for smooth shading purposes
We normalize the input normal but leave accum_normal not normalized */
void Particle::addToAccumNormal(glm::vec3 n) {accum_normal += glm::normalize(n);}  


/*takes one step in time, updating a particle's position using verlet integration*/
void Particle::particleTimeStep() {
	glm::vec3 temp = pos;
	updatePos((pos - old_pos)*(1.0f - damping) + acceleration*(timestep));  //verlet integration equation
	old_pos = temp;
	resetAccel();   //we reset accelleration because it has been translated into a change in position
}


