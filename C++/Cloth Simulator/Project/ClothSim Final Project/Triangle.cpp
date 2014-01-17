#include "Triangle.h"

Triangle::Triangle(Particle *p1, Particle *p2, Particle *p3) {
	_p1 = p1;
	_p2 = p2;
	_p3 = p3;
	_vis = true;
}

void Triangle::accumTriNorm() {
	glm::vec3 norm = Cloth::calcTriNorm(_p1, _p2, _p3);
	_p1->addToAccumNormal(norm);
	_p2->addToAccumNormal(norm);
	_p3->addToAccumNormal(norm);
}

void Triangle::addWind(glm::vec3 wind) {
	glm::vec3 trinorm = Cloth::calcTriNorm(_p1, _p2, _p3);
	glm::vec3 trinormdir = glm::normalize(trinorm);
	glm::vec3 windForce = trinorm*(glm::dot(trinormdir, wind));

	_p1->addForce(windForce);
	_p2->addForce(windForce);
	_p3->addForce(windForce);
}

void Triangle::toggleVisibility(bool val) {
	_vis = val;
}

void Triangle::render(int shade_type, bool normalColoring) {
	glm::vec3 norm = Cloth::calcTriNorm(_p1,_p2,_p3);
	norm = glm::normalize(norm);

	if (!_vis) { 
		return;

	}
	
	if (normalColoring) {
		glColor3f(norm.x, norm.y, norm.z);
		//glNormal3fv((GLfloat *) &norm);
		glVertex3fv((GLfloat *) &(_p1->getPos()));
		glVertex3fv((GLfloat *) &(_p2->getPos()));
		glVertex3fv((GLfloat *) &(_p3->getPos()));


	} else if (shade_type == GL_FLAT) {
		glNormal3fv((GLfloat *) &norm);
		glVertex3fv((GLfloat *) &(_p1->getPos()));
		glVertex3fv((GLfloat *) &(_p2->getPos()));
		glVertex3fv((GLfloat *) &(_p3->getPos()));


	} else if (shade_type == GL_SMOOTH) {          
		glNormal3fv((GLfloat *) &(glm::normalize((_p1->getNormal()))));
		glVertex3fv((GLfloat *) &(_p1->getPos()));

		glNormal3fv((GLfloat *) &(glm::normalize((_p2->getNormal()))));
		glVertex3fv((GLfloat *) &(_p2->getPos()));

		glNormal3fv((GLfloat *) &(glm::normalize((_p3->getNormal()))));
		glVertex3fv((GLfloat *) &(_p3->getPos()));
	} else {
		cout << "shade type error"<<endl;
	}
}