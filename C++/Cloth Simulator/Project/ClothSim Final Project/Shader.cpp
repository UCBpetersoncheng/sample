#include "Shader.h"


Shader::Shader(int _shade_type, int _fill_type, Cloth* myCloth) {
	shade_type = _shade_type;
	fill_type = _fill_type;
	cloth = myCloth;
}

void Shader::resetNormals() {

	for(int i=0; i<cloth->particle_vector.size(); i++)
	{
		cloth->particle_vector[i]->resetNormal();
	}
}

void Shader::renderTriangles(bool normalColor) {
	for(int i =0; i < cloth->tri_vector.size(); i++) {
		glBegin(GL_TRIANGLES);
		cloth->tri_vector[i]->render(shade_type, normalColor);
		glEnd();
	}
}

void Shader::accumNormals() {
	for(int i =0; i < cloth->tri_vector.size(); i++) {
		cloth->tri_vector[i]->accumTriNorm();
	}
}

void Shader::shadeCloth() {

	GLfloat diffuse1[] = {0.0,0.9,0.9,0.0};
	GLfloat ambient1[] = {0,0,0, 0.0};
	GLfloat specular1[] = {1.0,1.0,1.0,0.0};
	GLfloat light1_pos[] = {-10.0,5.0,2.0,0.0};

	GLfloat diffuse0[] = {.5,0.2,0.9,1.0};
	GLfloat ambient0[] = {0,0,0, 0.0};
	GLfloat specular0[] = {1.0,1.0,1.0,0.0};
	GLfloat shininess0[] = { 30.0f};
	GLfloat light0_pos[] = {11.0,6.0,10.0,0.0};


	glEnable(GL_LIGHTING);
	glShadeModel(GL_SMOOTH); 
	glLightModeli(GL_LIGHT_MODEL_TWO_SIDE, GL_TRUE);
	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHT1);
	glLightfv(GL_LIGHT0, GL_POSITION, light0_pos);
	glLightfv(GL_LIGHT0, GL_AMBIENT, ambient0);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse0);
	glLightfv(GL_LIGHT0, GL_SPECULAR, specular0);
	glLightfv(GL_LIGHT1, GL_POSITION, light1_pos);
	glLightfv(GL_LIGHT1, GL_AMBIENT, ambient1);
	glLightfv(GL_LIGHT1, GL_DIFFUSE, diffuse1);
	glLightfv(GL_LIGHT1, GL_SPECULAR, specular1);
	resetNormals();
	accumNormals();

	glPolygonMode( GL_FRONT_AND_BACK, fill_type);
	if (shade_type == GL_FLAT || shade_type == GL_SMOOTH) {
		renderTriangles(false);
		

	} else if (shade_type == SHADE_NORMAL) {
		glDisable(GL_LIGHTING);
		renderTriangles(true);
		glEnable(GL_LIGHTING);
	} else {
		cout << "shade type not found" << endl;
	}
}


void Shader::updateShadeType(int shadeType) {
	shade_type = shadeType;
}

void Shader::updateFillType(int fillType) {
	fill_type = fillType;
}



