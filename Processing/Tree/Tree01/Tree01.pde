class Branch {
  PVector pos;
  float branchSize, diam, ang;
  color col;
 
  Branch extremeBranch, middleBranch;
  PVector extreme, middle;
  float branchSizeExtreme, branchSizeMiddle;
  float diamExtreme, diamMiddle;
  float angExtreme, angMiddle;
  color colSon;
  boolean withExtreme = false;
  boolean withMiddle = false;
  float sign, diamTop;
   
  Branch(PVector posTemp, float branchSizeTemp, float diamTemp, float angTemp, color colTemp) {
    pos = posTemp.get();
    branchSize = branchSizeTemp;
    diam = diamTemp;
    ang = angTemp;
    col = colTemp;
     
    if(branchSize > 30){
      colSon = color(red(col)*1.04,green(col)*1.02,blue(col)*1.01);
    }
    else{
      colSon = color(red(col)*0.8,green(col),blue(col)*0.9);
    }
     
    if((branchSize > 8) & (random(0,1) < 0.9)){
      withExtreme = true;
      if(branchSize > 30){
        branchSizeExtreme = random(0.85,0.95)*branchSize;
        diamExtreme = random(0.75,0.85)*diam;
        angExtreme = ang + random(-PI/7,PI/7);
        if(angExtreme > PI - 0.1*PI){
          angExtreme = angExtreme - random(PI*0.15,PI*0.3);
        }
        if(angExtreme < 0.1*PI){
          angExtreme = angExtreme + random(PI*0.15,PI*0.3);
        }
      }
      else{
        branchSizeExtreme = random(0.75,0.85)*branchSize;
        diamExtreme = 0.65*diam;
        angExtreme = ang + random(-PI/4,PI/4);
      }
      extreme = PVector.add(pos,new PVector(branchSize*cos(ang),-branchSize*sin(ang),0));
      extremeBranch = new Branch(extreme,branchSizeExtreme,diamExtreme,angExtreme,colSon);
    }
 
    if((branchSize > 8) & (random(0,1) < 40/branchSize) & (random(0,1) < 0.9)){
      withMiddle = true;
      if(branchSize > 30){
        branchSizeMiddle = random(0.85,0.95)*branchSize;
        diamMiddle = random(0.75,0.85)*diam;
        sign = random(-1,1);
        sign = sign/abs(sign);
        angMiddle = ang + sign*random(PI/8,PI/5);
      }
      else{
        branchSizeMiddle = random(0.75,0.9)*branchSize;
        diamMiddle = random(0.65,0.75)*diam;
        sign = random(-1,1);
        sign = sign/abs(sign);
        angMiddle = ang + sign*random(PI/9,PI/4);
      }
      float frac = random(0.6,0.9);
      middle = PVector.add(pos,new PVector(frac*branchSize*cos(ang),-frac*branchSize*sin(ang),0));
      middleBranch = new Branch(middle,branchSizeMiddle,diamMiddle,angMiddle,colSon);
    }
  }
   
  void paint() {
    fill(col);
    if(withExtreme){
      diamTop = diamExtreme;
    }
    else if(withMiddle){
      diamTop = diamMiddle;
    }
    else{
      diamTop = diam*0.5;
    }
    pushMatrix();
      translate(pos.x,pos.y);
      rotate(HALF_PI- ang);
      beginShape();
        vertex(-diam/2,0);
        vertex(-diamTop/2,-1.05*branchSize);
        vertex(diamTop/2,-1.05*branchSize);
        vertex(diam/2,0);
      endShape();
    popMatrix();
    if(withExtreme){
      extremeBranch.paint();
    }
    if(withMiddle){
      middleBranch.paint();
    }
  }
   
} 
Branch root;
float ang;
color col;
 
void setup() {
  noLoop();
  noStroke();
  smooth(); 
  size(800,600);
  background(240);
  ang = random(HALF_PI - PI/30,HALF_PI + PI/30); 
  col = color(140,80,20);
  root = new Branch(new PVector(0.5*width,0.92*height,0),70,20,ang,col);
}
 
void draw(){
  background(240);
  root.paint();
}
 
void mousePressed() {
  //save("pic.jpg");
  ang = random(HALF_PI - PI/30,HALF_PI + PI/30); 
  root = new Branch(new PVector(0.5*width,0.92*height,0),70,20,ang,col);
  redraw();
}

