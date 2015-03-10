/*
 * L-System
 * Ben Chun, 1 February 2011
 */
 
boolean drawRoof = true;
 
void setup() {
  size(800,800);
  smooth();
  fill(0,150);
  noStroke();
}
 
void draw() {
  background(255);
  drawHouse(width/2 - 50, width/2 + 50, height/2+150, 1+(int)14*mouseY/height);
}
 
void drawHouse(float x1, float x2, float y, int level) {
  if(level > 0) {
    float angle = (mouseX/(float)width) * (PI/3.0);
    float w = x2 - x1;
    float dy =  w/2 * tan(angle);
 
    quad(x1,y, x2,y, x2,y-w, x1,y-w);
    if (drawRoof) {
      triangle(x1,y-w, x2,y-w, x1+w/2, y-w-dy);
    }
    float d = dist(x2,y-w, x1+w/2, y-w-dy);
 
    // left branch
    pushMatrix();
    translate(x1,y-w);
    rotate(-angle);
    drawHouse(0,d,0,level-1);
    popMatrix();
 
    // right branch
    pushMatrix();
    translate(x2,y-w);
    rotate(angle);
    drawHouse(-d,0,0,level-1);
    popMatrix();
  }
}
 
void mousePressed()
{
  drawRoof = !drawRoof;
}
