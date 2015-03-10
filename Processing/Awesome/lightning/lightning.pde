int num = 300;
Line[] line_array = new Line[num];


void setup()
{
  size (600,600);

  for(int i=0; i<num; i++)
  {
    line_array[i] = new Line();

  }

}


void draw()
{

  background(0);

  for(int i=0; i<num; i++)
  {
   line_array[i].update();
   line_array[i].display();

  }

stroke(250,255,3, 40);

for(int i = 0; i<num; i++)
{
 for(int j = i+1; j<num; j++)
 {
  float x1 = line_array[i].x;
  float y1 = line_array[i].y;
  float x2 = line_array[j].x;
  float y2 = line_array[j].y;
  if(dist(x1,y1,x2,y2) < 40)
  {
    line (x1,y1,x2,y2);
  }
 }

}

}

void mouseReleased()
{
  for(int i=0; i<num; i++)
  {
   line_array[i].reset();

  }

}



class Line
{
  float x, y, vx, vy, grav;
  float bounciness;


  Line()
  {
   x = random (width);
   y = random (height);
   bounciness = random(-1,-.5);
   vx = random(-5,5);
   vy = random(-5,5);
   grav = -.015;

  }
  void update()
  {
    vy+=grav;
    vx+=grav;
    x+= vx;
    y+= vy;

  if (y>height)
  {
    y = height;
    vy *= bounciness;
  }

  if (x<0)
  {
    x= 0;
    vx*= bounciness;
  }

  if (x>width)
  {
    x = width;
    vx*= bounciness;
  }

  if (y<0)
  {
    y = 0;
    vy*= bounciness;
  }

  }

  void display()
  {

    pushMatrix();
    point(x,y);
    translate(x, y);
    popMatrix();

  }

  void reset()
  {
   x = mouseX;
   y = mouseY;
   vx = random(-5,5);
   vy = random(-5,5);
  }

}
