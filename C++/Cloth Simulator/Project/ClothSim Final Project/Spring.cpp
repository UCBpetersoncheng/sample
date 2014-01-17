#include "Spring.h"



Spring::Spring(Particle *part1, Particle *part2, float _k, int type) {
	p1 = part1;
	p2 = part2;
	k = _k;
	spring_type = type;

	glm::vec3 dir = p1->getPos() - p2->getPos();
	ed = k * glm::dot(dir, dir);

}

float Spring::solveSpring() {
	glm::vec3 delta = p2->getPos() - p1->getPos();
	float scale = 0.5f - ed/(ed + glm::dot(delta, delta));
	delta *= scale;
	p1->updatePos(delta);
	p2->updatePos(-delta);
	return scale;
}
	

	

