#include "Cloth.h"

#define TRAN_STEP .3
#define ANG_STEP 5
#define ZOOM_STEP .5
#define PLUS 43
#define MINUS 45

//********************************
// OpenGL Classes
//********************************	
class Viewport;

class Viewport {
public:
	int w, h;
};


//*******************************************
// GLOBAL VARIABLES
//*******************************************


POINT startDragMouse;
glm::vec3 leftMouseVec, oldLeftMouseVec;
float leftMouseX, leftMouseY, rightMouseX, rightMouseY;
bool leftMouseDown = false;
bool rightMouseDown = false;
float xtran = 0;
float windmod = 0;
float ytran = 0;
float zoom = 0;
bool windtog = true;
bool follow_cam = false;
float ball_rad = 3.0f;
float ballstep = ball_rad * .05f;
float damping = 0.01f;
float timestep = 0.07f;
float gravity = -0.09f;
float width = 20.0f;
float height = 20.0f;
float step = 0.5f;
bool frozen_toggle = false;
glm::vec3 wind = glm::vec3(0);
glm::vec3 c;
int W = 5;
int num_iterations = 2;

int rotate_x = 0;
int rotate_y = 0;
float old_x, delta_x,old_y, delta_y;


float PI = 3.1415926535f;

//options: GL_FILL  GL_LINE  GL_POINT
int fill_type = GL_FILL;

//options: GL_SMOOTH  GL_FLAT  SHADE_NORMAL
int shade_type = SHADE_NORMAL;

Cloth myCloth(width, height, step);
Viewport viewport;
Ball myBall(ball_rad, glm::vec3(0,0,3));
Shader myShader(shade_type, fill_type, &myCloth);



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
	gluPerspective(50, (w/1.0) / (h/1.0), 0.1f, 1000.0f);
}

float randf() {
	return ((float) rand())/RAND_MAX;
}

//****************************************************
// function that does the actual drawing of stuff
//***************************************************
void myDisplay() {


	glMatrixMode(GL_MODELVIEW);			        // indicate we are specifying camera transformations
	glLoadIdentity();							// make sure transformation is "zero'd"
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);
	//gluLookAt(5, 20, 30, myCloth._width/2.0f, -myCloth._height/2.0f, 0, 0, 1, 0);

	if (follow_cam) {
		c = myCloth.getParticle((int)(myCloth.numdiv_x / 2), (int)(myCloth.numdiv_y / 2))->getPos();
	}
	gluLookAt(0, 0, 40,
		c.x,c.y,c.z,
		0, 1, 0);




	glTranslatef(-xtran,0,0);
	glTranslatef(0,ytran,0);
	glTranslatef(0,0,zoom);

	glTranslatef(c.x, c.y, c.z);
	glRotatef(rotate_x, 0.0, 1.0, 0.0);
	glRotatef(rotate_y, 1.0, 0.0, 0.0);
	glTranslatef(-c.x, -c.y, -c.z);


	float r = (float)rand()/((float)RAND_MAX/(0.1 + windmod));
	if (windtog) wind = glm::vec3(r,r,r);


	myCloth.updateCloth(&myBall);
	myShader.shadeCloth();
	myBall.drawBall();



	glEnd();
	glPopMatrix();

	glFlush();
	glutSwapBuffers(); // swap buffers (we earlier set double buffer)
}



void mouseMotion(int x, int y){

	delta_x = x-old_x;
	delta_y = y-old_y;

	if (delta_x <= 10 && delta_x >=-10) {
		if (rightMouseDown) rotate_x += delta_x;	
	}
	if (delta_y <= 10 && delta_y >=-10)
		if (rightMouseDown) rotate_y += delta_y;


	if (leftMouseDown) {
		


		Particle *dragPart = myCloth.draggedPart;
		GLint viewport[4];
		glGetIntegerv(GL_VIEWPORT, viewport);

		GLdouble modelview[16];
		glGetDoublev(GL_MODELVIEW_MATRIX, modelview);

		GLdouble projection[16];
		glGetDoublev(GL_PROJECTION_MATRIX, projection);

		POINT mouse;
		GetCursorPos(&mouse);

		GLfloat winX, winY, winZ;

		winX = (float)mouse.x;
		winY = (float)mouse.y;
		winY = (float)viewport[3] - winY;

		glReadPixels(winX, winY, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, &winZ);

		GLdouble posX, posY, posZ;

		gluUnProject(winX, winY, winZ, modelview, projection, viewport, &posX, &posY, &posZ);
		oldLeftMouseVec = leftMouseVec;
		leftMouseVec = glm::vec3(posX, posY, posZ);

		POINT currDragMouse;
		GetCursorPos(&currDragMouse);
		float dx = currDragMouse.x - startDragMouse.x;
		float dy = currDragMouse.y - startDragMouse.y;

		dragPart->fixPos(leftMouseVec, dx / 50.0, -dy/50.0);
		startDragMouse = currDragMouse;
		
	}
	old_x = x;
	old_y = y;




}



void mouseClick(int button, int state, int x, int y) {
	if (button == GLUT_LEFT_BUTTON) {
		if (state == GLUT_DOWN) {
			leftMouseX = x;
			leftMouseY = y;
			leftMouseDown = true;

			GLint viewport[4];
			glGetIntegerv(GL_VIEWPORT, viewport);

			GLdouble modelview[16];
			glGetDoublev(GL_MODELVIEW_MATRIX, modelview);

			GLdouble projection[16];
			glGetDoublev(GL_PROJECTION_MATRIX, projection);

			POINT mouse;
			GetCursorPos(&mouse);

			GLfloat winX, winY, winZ;

			winX = (float)mouse.x;
			winY = (float)mouse.y;
			winY = (float)viewport[3] - winY;

			glReadPixels(winX, winY, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, &winZ);

			GLdouble posX, posY, posZ;

			gluUnProject(winX, winY, winZ, modelview, projection, viewport, &posX, &posY, &posZ);
			oldLeftMouseVec = leftMouseVec;
			leftMouseVec = glm::vec3(posX, posY, posZ);
			startDragMouse = mouse;

			myCloth.dragCloth(leftMouseVec);
		} else {
			leftMouseDown = false;
			myCloth.releaseDragged();
		}

	}
	if (button == GLUT_RIGHT_BUTTON) {
		if (state == GLUT_DOWN) rightMouseDown = true;
		else rightMouseDown = false;
		cout << rightMouseDown <<  endl;
	}


}


void draw() {

	glutPostRedisplay();
}

void arrow_keys( int keys, int x, int y ) {
	int mod = glutGetModifiers();
	switch(keys) {
	case GLUT_KEY_UP:
		if (mod == GLUT_ACTIVE_SHIFT) { 
			myBall.moveBallForward(ballstep);
		} else if (mod == GLUT_ACTIVE_CTRL) {
			ytran += TRAN_STEP;
		} else {
			myBall.moveBallUp(ballstep);
		}
		break;
	case GLUT_KEY_DOWN: 
		if (mod == GLUT_ACTIVE_SHIFT) { 
			myBall.moveBallBack(ballstep);
		} else if (mod == GLUT_ACTIVE_CTRL) {
			ytran -= TRAN_STEP;
		} else {
			myBall.moveBallDown(ballstep);
		}
		break;
	case GLUT_KEY_LEFT: 
		if (mod == GLUT_ACTIVE_CTRL) {
			xtran += TRAN_STEP;
		} else {
			myBall.moveBallLeft(ballstep);
		}
		break;
	case GLUT_KEY_RIGHT: 
		if (mod == GLUT_ACTIVE_CTRL) {
			xtran -= TRAN_STEP;
		} else {
			myBall.moveBallRight(ballstep);
		}	
		break;

	default:
		break;
	}
}

void normal_keys(unsigned char key, int x, int y) {

	switch(key) {
	case 's':
		cout<<"s pressed"<<endl;
		cout<<"shade type is" << shade_type;
		if (shade_type == GL_SMOOTH) shade_type = GL_FLAT;
		else if (shade_type == GL_FLAT) shade_type = SHADE_NORMAL;
		else if (shade_type == SHADE_NORMAL) shade_type = GL_SMOOTH;
		else cout<< "shade type error" <<endl;

		myShader.updateShadeType(shade_type);
		break;
	case 'f':
		if (fill_type == GL_LINE) fill_type = GL_POINT;
		else if (fill_type == GL_POINT) fill_type = GL_FILL;
		else if (fill_type == GL_FILL) fill_type = GL_LINE;
		else cout << "fill type error" << endl;

		myShader.updateFillType(fill_type);
		break;
	case 'q':
		frozen_toggle = !frozen_toggle;
		break;
	case 'l':
		follow_cam = !follow_cam;
		break;

	case PLUS:
		zoom += ZOOM_STEP;
		break;
	case MINUS:
		zoom -= ZOOM_STEP;
		break;
	case 'r':

		if (!leftMouseDown && !rightMouseDown) myCloth = Cloth(width, height, step);
		break;
	case 'W':
		windmod += .05f;
		break;
	case 'w':
		windmod -= .05f;
		break;
	case 'd':
		if (windtog) wind = glm::vec3(0);
		windtog = !windtog;
		break;
	case 'b':
		myBall.radius -= .3334;
		break;
	case 'B':
		myBall.radius += .3334;
		break;

	case 'm':
		GLint viewport[4];
		glGetIntegerv(GL_VIEWPORT, viewport);

		GLdouble modelview[16];
		glGetDoublev(GL_MODELVIEW_MATRIX, modelview);

		GLdouble projection[16];
		glGetDoublev(GL_PROJECTION_MATRIX, projection);

		POINT mouse;
		GetCursorPos(&mouse);

		GLfloat winX, winY, winZ;

		winX = (float)mouse.x;
		winY = (float)mouse.y;
		winY = (float)viewport[3] - winY;

		glReadPixels(winX, winY, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, &winZ);

		GLdouble posX, posY, posZ;

		gluUnProject(winX, winY, winZ, modelview, projection, viewport, &posX, &posY, &posZ);

		myBall = Ball(4, glm::vec3(posX, posY, posZ));



	}

}

int main(int argc, char *argv[]) {


	//This initializes glut
	glutInit(&argc, argv);


	//This tells glut to use a double-buffered window with red, green, and blue channels 
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);


	// Initalize theviewport size
	viewport.w = 1440;
	viewport.h = 900;



	//The size and position of the window
	glutInitWindowSize(viewport.w, viewport.h);
	glutInitWindowPosition(0,0);
	glutCreateWindow(argv[0]);


	Particle::changeBase(glm::vec3(0, gravity, 0));
	//glm::vec3 a = myCloth.getParticle(myCloth.numdiv_x / 2, 0)->getPos();
	//glm::vec3 b = myCloth.getParticle(myCloth.numdiv_x / 2, myCloth.numdiv_y-1)->getPos();
	c = myCloth.getParticle((int)(myCloth.numdiv_x / 2), (int)(myCloth.numdiv_y / 2))->getPos();



	initScene();							// quick function to set up scene

	glutKeyboardFunc(normal_keys);
	glutSpecialFunc(arrow_keys);
	glutDisplayFunc(myDisplay);				// function to run when its time to draw something
	glutMotionFunc(mouseMotion);
	glutMouseFunc(mouseClick);
	glutReshapeFunc(myReshape);				// function to run when the window gets resized
	glutIdleFunc(draw);
	glutMainLoop();							// infinite loop that will keep drawing and resizing


	return 0;
}

