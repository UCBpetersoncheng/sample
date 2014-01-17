#include "Cloth.h"

Spring* Cloth::link(Particle* a, Particle* b, int type) {
	float k;
	if (type == STRUCT) { k = 1.0f; }
	else if (type == SHEAR) {k = 1.0f; }
	else if (type == BEND) {k= 1.0f;}
	Spring *s = new Spring(a, b, k, type);
	spring_vector.push_back(s);
	return s;
}

void Cloth::addTriangle(Particle* p1, Particle* p2, Particle* p3) {
	Triangle *t = new Triangle(p1, p2, p3);
	tri_vector.push_back(t);
}


void Cloth::releaseDragged() {
	draggedPart->frozen = false;
}

Cloth::Cloth(float width, float height, float subdiv) {
	_width = width;
	_height = height;
	dragging = false;

	numdiv_x = floor(width/subdiv);
	float subdiv_x = width/numdiv_x;


	numdiv_y = floor(height/subdiv);
	float subdiv_y =height/numdiv_y; 

	particle_vector.resize(numdiv_x * numdiv_y);

	/* Creating the particles that make up our cloth, no springs yet */
	for (int x=0; x<numdiv_x; x++) {
		for (int y=0; y<numdiv_y; y++) {
			glm::vec3 curr_pos = glm::vec3(x*subdiv_x - _width/2, -(y*subdiv_y - _height/2),0);
			particle_vector[y*numdiv_x + x] = new Particle(curr_pos);
			if (y ==0) {
				particle_vector[y*numdiv_x + x]->freeze();
			}
		}
	}


	/* Adding in springs. Broken into 3 seperate stages for structural, shear, and bending constraints */
	Particle *p1, *p2, *p3, *p4;
	Spring *s1, *s2, *s3, *s4;

	for(int y = 0; y < (numdiv_y - 1); y++) {
		for (int x = 0; x < (numdiv_x - 1); x++) {
			p1 = particle_vector[y*numdiv_x + x];
			p2 = particle_vector[y*numdiv_x + x + 1];
			p3 = particle_vector[(y+1)*numdiv_x + x];
			p4 = particle_vector[(y+1)*numdiv_x + x + 1];


			s1 = link(p1, p2, STRUCT);
			s2 = link(p1, p3, STRUCT);
			s3 = link(p1, p4, SHEAR);
			s4 = link(p2, p4, SHEAR);

			// Forms two triangles and pushes it to Triangle Vector
			addTriangle(p2, p1, p3);
			addTriangle(p4, p2, p3);

		}
	}

	// Edge Case Along Bottom Y-AXIS
	for(int x = 0; x < (numdiv_x - 1); x++) {
		link(particle_vector[(numdiv_y-1)*numdiv_x + x], particle_vector[(numdiv_y-1)*numdiv_x + x + 1], STRUCT);
	}

	// Edge Case Along Far-Right X-AXIS
	for(int y = 0; y < (numdiv_y - 1); y++) {
		link(particle_vector[y*numdiv_x + numdiv_x - 1], particle_vector[(y+1)*numdiv_x + numdiv_x - 1], STRUCT);
	}

	// Adding in Bending Constraints

	for(int y = 0; y < (numdiv_y - 2); y++) {
		for (int x = 0; x < (numdiv_x - 2); x++) {
			p1 = particle_vector[y*numdiv_x + x];
			p2 = particle_vector[y*numdiv_x + x + 2];
			p3 = particle_vector[(y+2)*numdiv_x + x];
			p4 = particle_vector[(y+2)*numdiv_x + x + 2];

			link(p1, p2, BEND);
			link(p1, p3, BEND);
			link(p1, p4, BEND);
			link(p2, p4, BEND);
		}
	}
}


/* Takes a time step for each particle and checks for any collisions with the ball, then resolves constraints*/

void Cloth::updateCloth(Ball* ball) {
	glPushMatrix();

	//accumulate wind forces before verlet integration
	addWindToCloth(wind);
	//#pragma omp parallel for
	for (int i=0; i < particle_vector.size(); i++) {

		//Simple step according to verlet integration
		particle_vector[i]->particleTimeStep();

		//Ball collision resolution
		Particle* p = particle_vector[i];
		glm::vec3 distToCent = p->getPos() - ball->pos;
		float distMag = sqrt(glm::dot(distToCent, distToCent));
		if (distMag < ball->radius + 0.3f) {
			p->updatePos(glm::normalize(distToCent) * (ball->radius + 0.3f - distMag));
		}
	}

	//constraint resolution
	for (int t=0; t<num_iterations; t++) {
		//#pragma omp parallel for
		for (int j = 0; j < spring_vector.size(); j++) {
			float scale = spring_vector[j]->solveSpring();
		}
	}


	glPopMatrix();
}



void Cloth::dragCloth(glm::vec3 clickPos) {
	float minDist = INT_MAX;
	float currDist;
	glm::vec3 p;
	Particle *closestParticle = particle_vector[0];
	for (int i = 0; i<particle_vector.size(); i++) {
		p = particle_vector[i]->getPos();
		currDist = pow(clickPos.x - p.x, 2) + pow(clickPos.y - p.y, 2) + pow(clickPos.z - p.z, 2);
		if (currDist < minDist) { 
			minDist = currDist;
			closestParticle = particle_vector[i];
		}
	}
	draggedPart = closestParticle;
}

/*Returns the triangle's normal vector, NOT GUARANTEED to be unit length*/
glm::vec3 Cloth::calcTriNorm(Particle *p1, Particle *p2, Particle *p3) {
	glm::vec3 a = p1->getPos();
	glm::vec3 b = p2->getPos();
	glm::vec3 c = p3->getPos();

	return glm::cross(b-a, c-a);
}


void Cloth::addWindToCloth(glm::vec3 wind) {
	//#pragma omp parallel for
	for (int i=0; i<tri_vector.size(); i++) {
		tri_vector[i]->addWind(wind);
	}
}

