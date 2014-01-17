#ifndef SHADER_H
#define SHADER_H

#include "Cloth.h"

#define SHADE_SMOOTH 0
#define SHADE_FLAT 1
#define SHADE_NORMAL 2

class Cloth;
class Particle;

class Shader {
public:
	
	Shader() {};
	Shader(int _shade_type, int _fill_type, Cloth* myCloth);

	int shade_type, fill_type;
	bool wire;
	Cloth* cloth;

	void resetNormals();
	void shadeCloth();
	void renderTriangles(bool normalColor);
	void accumNormals();
	void updateShadeType(int shadeType);
	void updateFillType(int fillType);
	
private:
};

#endif