float radian = 0;
float centerX;
float centerY;
float centerZ;
float posY;

float r;
float d1;
float d2;
float scale;
float y;
float degree;


float speed;

void setup() 
{
  size(600, 600);
  centerX = width / 2;
  centerY = height /2 ;
  posY = centerY;
  centerZ = 100;
  r = 110;
  d1 = 50;
  speed = 2;
}

void draw()
{
//  background(100);
  degree += speed;
  radian = degree * PI / 180;
  
  d2 = sin(radian) * r + centerZ;
  scale = d1 / (d1 + d2);
  posY = centerY + y * scale;
  
  ellipse(cos(radian) * r + centerX, posY, r * scale, r * scale);
}

void mouseMoved()
{
  y = (centerY - mouseY) / 3;
}
