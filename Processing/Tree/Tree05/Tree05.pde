float xoff;
 
void setup(){
  size(640, 640);
  stroke(0, 150, 255, 100);
}
 
void draw(){
  background(0);
  pushMatrix();
  translate(width/2, height);
  branch(160);
  popMatrix();
}
 
void branch(float len){
  float t = map(len, 1, 160, 1, 30);
  float angle = sin(radians(xoff+len)) * 5;
  float theta = 30;
  strokeWeight(t);
  
  fill(0, 150, 255, 100);
  bezier(0, 0, 0, 0, -t, -t, 0, -len);
  bezier(0, 0, 0, 0, t, -t, 0, -len);
  fill(255);
  ellipse(0, 0, t, t);
   
  translate(0, -len);
  len *= 0.70;
   
  if(len > 5){
    pushMatrix();
    rotate(radians(theta+angle));
    branch(len);
    popMatrix();
     
    pushMatrix();
    rotate(radians(-theta+angle));
    branch(len);
    popMatrix();
  }
  xoff += 0.005;
}

