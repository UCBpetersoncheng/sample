
#include <vector>
#include <iostream>
#include <fstream>
#include <cmath>

#ifdef _WIN32
#include <windows.h>
#else
#include <sys/time.h>
#endif

#ifdef OSX
#include <GLUT/glut.h>
#include <OpenGL/glu.h>
#else
#include <GL/glut.h>
#include <GL/glu.h>
#endif

#include <glm.hpp>

#include <time.h>
#include <math.h>
#include <stdlib.h>
#include <fstream>
#include <iostream>
#include <vector>


#define SHADE_NORM 399
#define SHADE_FLAT GL_FLAT
#define SHADE_SMOOTH GL_SMOOTH

#define TRAN_STEP .1
#define ANG_STEP 5
#define ZOOM_STEP .5
#define PLUS 43
#define MINUS 45
#define PATCH_SIZE 4

int round (float f) {
	return (int)floor(f + .5);
}

float dist(glm::vec3 a, glm::vec3 b) {
	//glm::vec3 c = a - b;
	//return glm::dot(c, c);
	return sqrt(pow((a.x-b.x), 2) +
				pow((a.y-b.y), 2) +
				pow((a.z-b.z), 2));
}

void printVec(glm::vec3* v) {
	printf("%f\t%f\t%f\t", v->x, v->y, v->z);
}

using namespace std;



//****************************************************
// Some Classes
//****************************************************

class PointNorm {
public:
	glm::vec3* point;
	glm::vec3* norm;

	PointNorm() {
		point = new glm::vec3();
		norm = new glm::vec3();
	}

	/* Assumes that n is a normalized vector */
	PointNorm(glm::vec3* p, glm::vec3* n) {
		point = p;
		norm = n;
	}
};

class Patch {
public:
	vector<vector<glm::vec3*>> upoints;
	vector<vector<glm::vec3*>> vpoints;
	vector<vector<PointNorm*>> subpoints;

	Patch() {
		upoints.resize(PATCH_SIZE);
		vpoints.resize(PATCH_SIZE);

		for (int i = 0; i<4; i++) {
			upoints[i].resize(PATCH_SIZE);
			vpoints[i].resize(PATCH_SIZE);
		}

		for (int i = 0; i<4; i++) {
			upoints[i][0] = vpoints[0][i] = new glm::vec3();
			upoints[i][1] = vpoints[1][i] = new glm::vec3();
			upoints[i][2] = vpoints[2][i] = new glm::vec3();
			upoints[i][3] = vpoints[3][i] = new glm::vec3();			
		}
	}
};


class Viewport;

class Viewport {
public:
	int w, h; // width and height
};



//****************************************************
// Global Variables
//****************************************************
Viewport	viewport;
vector<Patch*> patches;
float step = .01;
float xtran = 0;
float ytran = 0;
float zoom = 0;
float tipangle = 0;
float viewangle = 0;
int shadeflag = SHADE_FLAT;
int filltype = GL_FILL;
bool normalcolor = false;
bool adapt = false;
vector<glm::vec3*> vcurve;
vector<glm::vec3*> ucurve;

//****************************************************
// Forward Declaration Bullshit
//****************************************************
void adaptiveSubHelper(Patch* p, float error, glm::vec2* uva, glm::vec2* uvb, glm::vec2* uvc);
void edgeCheck(Patch* p, float error, glm::vec2* u1, glm::vec2* u2, bool* b, PointNorm* result);
void drawVertexNormal(PointNorm* p);

//****************************************************
// Simple init function
//****************************************************
void initScene(){
	// Nothing to do here for this simple example.
}

//****************************************************
// reshape viewport if the window is resized
//****************************************************
void myReshape(int w, int h) {
	viewport.w = w;
	viewport.h = h;

	glViewport (0,0,viewport.w,viewport.h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(50, (w/1.0) / (h/1.0), 0.1f, 100.0f);
}

glm::vec3 bezCurveInterp(vector<glm::vec3*> curve, float t, glm::vec3* dpdt) {
	glm::vec3 a = *curve[0] * (1.0f-t) + *curve[1] * t;
	glm::vec3 b = *curve[1] * (1.0f-t) + *curve[2] * t;
	glm::vec3 c = *curve[2] * (1.0f-t) + *curve[3] * t;

	glm::vec3 d = a * (1.0f-t) + b * t;
	glm::vec3 e = b * (1.0f-t) + c * t;


	*dpdt = 3.0f * (e - d);
	return (d * (1.0f-t) + e * t);
}

void bezPatchInterp(Patch* p, float u, float v, glm::vec3* point, glm::vec3* norm) {
	glm::vec3 *dpdv = new glm::vec3();
	glm::vec3 *dpdu = new glm::vec3();

	// Note that dpdv serves no purpose yet
	vcurve[0] = &bezCurveInterp(p->upoints[0], u, dpdv);
	vcurve[1] = &bezCurveInterp(p->upoints[1], u, dpdv);
	vcurve[2] = &bezCurveInterp(p->upoints[2], u, dpdv);
	vcurve[3] = &bezCurveInterp(p->upoints[3], u, dpdv);

	// Note that dpdu serves no purpose yet
	ucurve[0] = &bezCurveInterp(p->vpoints[0], v, dpdu);
	ucurve[1] = &bezCurveInterp(p->vpoints[1], v, dpdu);
	ucurve[2] = &bezCurveInterp(p->vpoints[2], v, dpdu);
	ucurve[3] = &bezCurveInterp(p->vpoints[3], v, dpdu);

	*point = bezCurveInterp(vcurve, v, dpdv);
	*point = bezCurveInterp(ucurve, u, dpdu);

	*norm = glm::cross(*dpdu, *dpdv);
	*norm = glm::normalize(*norm);
}

void edgeCheck(Patch* p, float error, glm::vec2* u1, glm::vec2* u2, bool* b, PointNorm* result) {
	glm::vec3* v = new glm::vec3();
	glm::vec3* n = new glm::vec3();
	bezPatchInterp(p, u1->x, u1->y, v, n);
	glm::vec3* v2 = new glm::vec3();
	glm::vec3* n2 = new glm::vec3();
	bezPatchInterp(p, u2->x, u2->y, v2, n2);
	glm::vec3* vmid = new glm::vec3();
	glm::vec3* nmid = new glm::vec3();
	glm::vec2 mid = (*u1+*u2)/2.0f;
	//bezPatchInterp(p, (u1->x + u2->x)/2.0f, (u1->y + u2->y)/2.0f, vmid, nmid);
	bezPatchInterp(p, mid.x, mid.y, vmid, nmid);

	*result = PointNorm(v, n);
	float d = dist((*v+*v2)/2.0f, *vmid);
	/*printVec(v);
	cout << endl;
	printVec(v2);
	cout << endl;
	printVec(vmid);
	cout << endl;
	cout << endl;*/
	//printf("%f\n", d);
	*b = (d >  error);
}

void adaptiveSubHelper(Patch* p, float error, glm::vec2* uva, glm::vec2* uvb, glm::vec2* uvc) {
	bool ab, bc, ca;
	PointNorm *p_ab = new PointNorm();
	PointNorm *p_bc = new PointNorm();
	PointNorm *p_ca = new PointNorm();

	edgeCheck(p, error, uva, uvb, &ab, p_ab);
	edgeCheck(p, error, uvb, uvc, &bc, p_bc);
	edgeCheck(p, error, uvc, uva, &ca, p_ca);

	if(ab && bc && ca) {
		adaptiveSubHelper(p, error, uva, new glm::vec2((*uva+*uvb)/2.0f), new glm::vec2((*uva+*uvc)/2.0f));
		adaptiveSubHelper(p, error, new glm::vec2((*uva+*uvb)/2.0f), uvb, new glm::vec2((*uvb+*uvc)/2.0f));
		adaptiveSubHelper(p, error, new glm::vec2((*uvc+*uvb)/2.0f), uvc, new glm::vec2((*uva+*uvc)/2.0f));
		adaptiveSubHelper(p, error, new glm::vec2((*uva+*uvb)/2.0f), new glm::vec2((*uvb+*uvc)/2.0f), new glm::vec2((*uvc+*uva)/2.0f));
	} else if (ab && !bc && !ca) {
		adaptiveSubHelper(p, error, uva, new glm::vec2((*uva+*uvb)/2.0f), uvc);
		adaptiveSubHelper(p, error, uvb, uvc, new glm::vec2((*uva+*uvb)/2.0f));
	} else if (!ab && bc && !ca) {										
		adaptiveSubHelper(p, error, uva, uvb, new glm::vec2((*uvb+*uvc)/2.0f));
		adaptiveSubHelper(p, error, uvc, uva, new glm::vec2((*uvb+*uvc)/2.0f));
	} else if (!ab && !bc && ca) {											
		adaptiveSubHelper(p, error, uva, uvb, new glm::vec2((*uva+*uvc)/2.0f));
		adaptiveSubHelper(p, error, uvb, uvc, new glm::vec2((*uva+*uvc)/2.0f));
	} else if (ab && bc && !ca) {
		adaptiveSubHelper(p, error, uva, new glm::vec2((*uva+*uvb)/2.0f), uvc);
		adaptiveSubHelper(p, error, uvc, new glm::vec2((*uva+*uvb)/2.0f), new glm::vec2((*uvb+*uvc)/2.0f));
		adaptiveSubHelper(p, error, new glm::vec2((*uva+*uvb)/2.0f), uvb, new glm::vec2((*uvb+*uvc)/2.0f));
	} else if (ab && !bc && ca) {																
		adaptiveSubHelper(p, error, uva, new glm::vec2((*uva+*uvb)/2.0f), new glm::vec2((*uvc+*uva)/2.0f));
		adaptiveSubHelper(p, error, new glm::vec2((*uva+*uvb)/2.0f), uvc, new glm::vec2((*uva+*uvc)/2.0f));
		adaptiveSubHelper(p, error, uvc, new glm::vec2((*uva+*uvb)/2.0f), uvb);			
	} else if (!ab && bc && ca) {															
		adaptiveSubHelper(p, error, uvb, new glm::vec2((*uvc+*uvb)/2.0f), uva);				 
		adaptiveSubHelper(p, error, uva, new glm::vec2((*uvc+*uvb)/2.0f), new glm::vec2((*uvc+*uva)/2.0f));
		adaptiveSubHelper(p, error, new glm::vec2((*uvc+*uvb)/2.0f), uvc, new glm::vec2((*uva+*uvc)/2.0f));
	} else if (!ab && !bc && !ca){
		glBegin(GL_TRIANGLES);
		if (normalcolor) glColor3f(p_ab->norm->x, p_ab->norm->y, p_ab->norm->z);
		drawVertexNormal(p_ab);
		drawVertexNormal(p_bc);
		drawVertexNormal(p_ca);
		glEnd();
		return;
	} else {
		return;
	}
}

void adaptiveSub(Patch* p) {

	glm::vec2* uv1 = new glm::vec2(0, 0);
	glm::vec2* uv2 = new glm::vec2(1, 1);
	glm::vec2* uv3 = new glm::vec2(0, 1);
	glm::vec2* uv4 = new glm::vec2(1, 0);

	adaptiveSubHelper(p, step, uv3, uv2, uv1);
	adaptiveSubHelper(p, step, uv2, uv4, uv1);

}





void subDividePatch(Patch* p, float step) {

	int numdiv = floor(1.0f/ step);
	step = 1.0f/numdiv;

	p->subpoints.resize(numdiv+1);
	for (int iu=0; iu <= numdiv; iu++) {

		float u = iu*step;
		p->subpoints[iu].resize(numdiv+1);

		for (int iv=0; iv <= numdiv; iv++) {
			float v = iv*step;
			glm::vec3* currpoint = new glm::vec3();
			glm::vec3* currnorm = new glm::vec3();
			bezPatchInterp(p, u, v, currpoint, currnorm);

			PointNorm* currpointnorm = new PointNorm(currpoint, currnorm);
			p->subpoints[iu][iv] = currpointnorm;
		}
	}
}


//************************************
// Handle Keyboard Input
//************************************

void processNormalKeys(unsigned char key, int x, int y) {
	//cout << key + 0 << " was normal pressed!"<<endl;
	switch(key){
	case PLUS:
		zoom += ZOOM_STEP;
		break;
	case MINUS:
		zoom -= ZOOM_STEP;
		break;
	case 's':
		if (shadeflag == SHADE_FLAT) shadeflag = SHADE_SMOOTH; else shadeflag = SHADE_FLAT;
		break;
	case 'w':
		if (filltype == GL_LINE) filltype = GL_FILL; else filltype = GL_LINE;
		break;
	case 'h':
		if (filltype != GL_POINT) filltype = GL_POINT; else filltype = GL_FILL;
		break;
	}
}


void processSpecialKeys(int key, int x, int y) {
	//cout << key + 0 << " was special pressed!"<<endl;
	int mod = glutGetModifiers();

	switch(key) {
	case GLUT_KEY_UP:
		(mod == GLUT_ACTIVE_SHIFT) ? ytran += TRAN_STEP : tipangle += ANG_STEP;
		break;	 
	case GLUT_KEY_DOWN:	 
		(mod == GLUT_ACTIVE_SHIFT) ? ytran -= TRAN_STEP : tipangle -= ANG_STEP;
		break;	 
	case GLUT_KEY_LEFT:	 
		(mod == GLUT_ACTIVE_SHIFT) ? xtran += TRAN_STEP : viewangle += ANG_STEP;
		break;
	case GLUT_KEY_RIGHT:
		(mod == GLUT_ACTIVE_SHIFT) ? xtran -= TRAN_STEP : viewangle -= ANG_STEP;
		break;
	}
}


//***************************************************
// read in control points from input file and 
// store them in patches vector
//***************************************************
void readControlPoints(string filename) {
	int numPatches = -1;
	ifstream inpfile(filename.c_str());
	if (!inpfile.is_open()) {
		cout << "unable to open file!" << endl;
		exit(1);
	}
	inpfile >> numPatches;

	patches.resize(numPatches);

	for (int i = 0; i < numPatches; i++) {
		patches[i] = new Patch;
		for (int j = 0; j < 4; j++) {
			for (int k = 0; k < 4; k++) {
				inpfile >> patches[i]->upoints[j][k]->x;
				inpfile >> patches[i]->upoints[j][k]->y; 
				inpfile >> patches[i]->upoints[j][k]->z;
			}
		}
	}
}

void drawVertexNormal(PointNorm* p) {
	glNormal3f(p->norm->x, p->norm->y, p->norm->z);
	glVertex3f(p->point->x, p->point->y, p->point->z);
}


//************************************
// factorial
//************************************
int fact(int n) {
	int result = 1;
	for (int x=1; x<=n; x++) {
		result *= x;
	}
	return result;
}

//**********************************
// renders an entire patch
//**********************************
void drawPatch(Patch* p) {
	for (int u = 0; u < p->subpoints.size() - 1; u++) {
		for (int v = 0; v < p->subpoints[u].size() - 1; v++) {
			glBegin(GL_TRIANGLES);
			if (normalcolor) glColor3f(p->subpoints[u][v]->norm->x, 
				p->subpoints[u][v]->norm->y, 
				p->subpoints[u][v]->norm->z);
			else glColor3f(.2, .8, .4);
			drawVertexNormal(p->subpoints[u][v]);
			drawVertexNormal(p->subpoints[u+1][v]);
			drawVertexNormal(p->subpoints[u][v+1]);
			glEnd();

			glBegin(GL_TRIANGLES);
			drawVertexNormal(p->subpoints[u+1][v]);
			drawVertexNormal(p->subpoints[u][v+1]);
			drawVertexNormal(p->subpoints[u+1][v+1]);
			glEnd();
		}
	}
}




//****************************************************
// function that does the actual drawing of stuff
//***************************************************
void myDisplay() {
	static bool firstRun = true;
	static GLuint myDisplayList = glGenLists(1);

	glMatrixMode(GL_MODELVIEW);			        // indicate we are specifying camera transformations
	glLoadIdentity();							// make sure transformation is "zero'd"
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);
	gluLookAt(0, 0, 10, 0, 0, 0, 0, 1, 0);


	if (!normalcolor) glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHT1);

	GLfloat lightpos[] = {10, 10, 5, 0};
	GLfloat lightpos2[] = {-10, 10, 5, 0};
	GLfloat bluelight[] = {0,.3f, 0.3f, 0};
	GLfloat yelolight[] = {1, 1, 0, 0};
	GLfloat white[] = {1, 1, 1, 0};
	GLfloat red[] = {0.5f, 0, 0, 0};
	GLfloat s[] = {128};
	glLightfv(GL_LIGHT0, GL_POSITION, lightpos);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, bluelight);
	glLightfv(GL_LIGHT0, GL_SPECULAR, white);
	glMaterialfv(GL_FRONT, GL_SPECULAR, yelolight);
	glMaterialfv(GL_FRONT, GL_SHININESS, s);
	glLightfv(GL_LIGHT1, GL_POSITION, lightpos2);
	glLightfv(GL_LIGHT1, GL_DIFFUSE, red);
	glLightfv(GL_LIGHT1, GL_SPECULAR, white);
	glEnable(shadeflag);
	glShadeModel(shadeflag);


	glTranslatef(-xtran,0,0);
	glTranslatef(0,ytran,0);
	glTranslatef(0,0,zoom);

	glRotatef (tipangle, 1,0,0);  // Up and down arrow keys 'tip' view.
	glRotatef (viewangle, 0,1,0);  // Right/left arrow keys 'turn' view.


	glPointSize(3.0f);
	glPolygonMode( GL_FRONT_AND_BACK, filltype);

	glPushMatrix();

	if (firstRun) {
		glNewList(myDisplayList, GL_COMPILE);
		for (int a = 0; a < patches.size(); a++) {
			if (adapt) {
				adaptiveSub(patches[a]);
			} else {
				subDividePatch(patches[a], step);
				drawPatch(patches[a]);
			}
		}
		glEndList();
		firstRun = false;
		glCallList(myDisplayList);
	} else {
		glCallList(myDisplayList);
	}

	glPopMatrix();

	glEnd();
	glFlush();
	glutSwapBuffers();					// swap buffers (we earlier set double buffer)
}


void myFrameMove() {
	//nothing here for now
#ifdef _WIN32
	//Sleep(10);                                   //give ~10ms back to OS (so as not to waste the CPU)
#endif
	glutPostRedisplay(); // forces glut to call the display function (myDisplay())
}

//****************************************************
// the usual stuff, nothing exciting here
//****************************************************
int main(int argc, char *argv[]) {
	string fname;
	if (argc < 3) return 1;
	fname = argv[1];
	step = atof(argv[2]);
	if (argc > 3) {
		string flag = argv[3];
		adapt = !strcmp(flag.c_str(), "-a");
	}

	readControlPoints(fname);

	//This initializes glut
	glutInit(&argc, argv);

	//This tells glut to use a double-buffered window with red, green, and blue channels 
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);


	vcurve.resize(PATCH_SIZE);
	ucurve.resize(PATCH_SIZE);

	// Initalize theviewport size
	viewport.w = 1600;
	viewport.h = 900;


	//The size and position of the window
	glutInitWindowSize(viewport.w, viewport.h);
	glutInitWindowPosition(0,0);
	glutCreateWindow(argv[0]);

	initScene();							// quick function to set up scene



	glutDisplayFunc(myDisplay);				// function to run when its time to draw something
	glutReshapeFunc(myReshape);				// function to run when the window gets resized
	glutSpecialFunc(processSpecialKeys);
	glutKeyboardFunc(processNormalKeys);
	glutIdleFunc(myFrameMove);                   // function to run when not handling any other task


	glutMainLoop();							// infinite loop that will keep drawing and resizing
	// and whatever else

	return 0;
}








