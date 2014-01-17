#ifndef BALL_H
#define BALL_H

#include "Cloth.h"

class Ball {
public:
	Ball() {};
	Ball(float rad, glm::vec3 pos);

	void drawBall();
	void moveBallForward(float amount);
	void moveBallLeft(float amount);
	void moveBallRight(float amount);
	void moveBallBack(float amount);
	void moveBallUp(float amount);
	void moveBallDown(float amount);

	float radius;
	float forward;

	glm::vec3 pos, old_pos;
private:
};

#endif