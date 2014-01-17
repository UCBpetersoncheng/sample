#include "Ball.h"


Ball::Ball(float rad, glm::vec3 _pos) {
	radius = rad;
	pos = _pos;
	old_pos = _pos;
}

void Ball::drawBall() {
	
	glPushMatrix();
	glTranslatef(old_pos.x, old_pos.y, old_pos.z);
	glutSolidSphere(radius, 50, 50);
	
	glPopMatrix();
	old_pos = pos;

}


void Ball::moveBallForward(float amount) {
	pos.z -= amount;
}

void Ball::moveBallLeft(float amount) {
	pos.x -= amount;
}

void Ball::moveBallRight(float amount) {
	pos.x += amount;
}

void Ball::moveBallBack(float amount) {
	pos.z += amount;
}

void Ball::moveBallUp(float amount) {
	pos.y += amount;
}

void Ball::moveBallDown(float amount) {
	pos.y -= amount;
}

