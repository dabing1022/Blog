float angle = 20;
float treeSize = 100;
float divisor = 0.82;
float maxRandomBranchSize = 0.02;
float maxRandomAngle = 30;
float maxLevel = 16;
color trunk = #8b4513;
color leaves = #009C22 & 0x70FFFFFF;
 
void setup()
{
  size(640, 480);
  smooth();
  stroke(0,0,0,64);
  strokeCap(ROUND);
  drawTree(width / 2, height);
}
 
void draw()
{
}
 
void keyPressed()
{   
  drawTree(width / 2, height);
}
 
void drawTree(float x, float y)
{
  pushMatrix();
  pushStyle();
  background(255);
  translate(x, y);
  drawBranch(1);
  popStyle();
  popMatrix();
}
 
void drawBranch(int level)
{
  if (level > maxLevel) return;
  float branchSize = -treeSize * pow(divisor + random(maxRandomBranchSize * 2.) - maxRandomBranchSize, level);
  strokeWeight(0.1 * -(branchSize));
  stroke(lerpColor(trunk,leaves,level/maxLevel));
  line(0,0,0,branchSize);
  translate(0,branchSize);
  float thisAngle = angle + random(maxRandomAngle * 2.) - maxRandomAngle;
  rotate(radians(-thisAngle));
  drawBranch(level + 1);
  rotate(radians(thisAngle));
  thisAngle = angle + random(maxRandomAngle * 2.) - maxRandomAngle;
  rotate(radians(thisAngle));
  drawBranch(level + 1);
  rotate(radians(-thisAngle));
  translate(0,-branchSize);
}

