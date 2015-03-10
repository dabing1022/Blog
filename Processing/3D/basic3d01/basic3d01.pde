float radian = 0;
float centerX;
float centerY;
float r;
float degree;

void setup() 
{
  size(600, 600);
  centerX = width / 2;
  centerY = height /2 ;
  r = 110;
}

void draw()
{
  clear();
  background(100);
  degree += 9;
  radian = degree * PI / 180;
  ellipse(cos(radian) * r + centerX, sin(radian) * r + centerY, r, r);
}
