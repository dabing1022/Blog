float PHI = .6;
float gap = 20;
int branch = 9;
int SqLength = 80;
int SqWidth = 20;
int SqRatio = SqLength / SqWidth;
int ALPH;
float LENGTH;
float WIDTH;
float X = 200;
float max = 5;
float m = 7;
int Mx = 300, My = 200;
void setup(){
  size(500,400);
  stroke(0);
  fill(0,100,250);
    /*
  translate(width/2, 100);
  fractal(11,100,15);
    */
}
    
void draw(){
  if (mousePressed){
  if (mouseY<height-gap) {
    Mx = mouseX;
    My = mouseY;
  }else{
    X=mouseX;
    PHI = .002*(width-mouseX);
  }
  }
   
   
  SqLength = (height-My)/3;
  //SqWidth = (mouseY+100)/2;
  ALPH = (Mx)/10;
  //blue background
  background(0,200,250);
  //the info on the screen
  fill(0);
  text("Ratio: "+PHI,0,10);
  text("branches: "+branch,0,20);
  text("angle: "+ALPH,0,30);
  text("stick length: "+SqLength,0,40);
  text("stick width: "+SqWidth,0,50);
  //the scroll bar
  fill(0,150,0);
  rect(0,height-gap,width,gap);
  fill(200,0,100);
  line(0,height-gap/2,width,height-gap/2);
  ellipse(X,height-gap/2,10,20);
   
  translate(width,height);//  just
  rotate(PI);             //flipping it
   
  translate(width/2-SqWidth/2, gap); // centered
  fractal(branch,SqWidth,SqLength,ALPH);
}
    
void fractal(int BRANCH, float LENGTH,float HEIGHT, int ALPH){
  BRANCH --;
  float RATIO = HEIGHT / LENGTH;
  colorMode(HSB);
  fill(100/branch*(branch-BRANCH),256,256);  //colored from red to green
  colorMode(RGB);
  fill(150,50,0);
  float PH= LENGTH * PHI;
  float A = PH * tan(radians(ALPH));//the height from the head intersection to the base
  float B = LENGTH * (1-PHI); //the small part of the small triALPH
  float C = sqrt(sq(A)+sq(B));//the small square length
  float D = PH *(1/cos(radians(ALPH)));//the bigger square length
  int BET = int(degrees(atan(A/B)));
  noStroke();
  triangle(0,HEIGHT,LENGTH,HEIGHT,PH,HEIGHT+A);
  //rect(0,0,LENGTH,HEIGHT);
  rect(LENGTH/10,0,4*LENGTH/5,HEIGHT); //the one big first square
  stroke(0);
  noFill();
  bezier(0,0,LENGTH/3*sin(radians(ALPH)),HEIGHT/5,LENGTH/3*sin(radians(ALPH)),4*HEIGHT/5,0,HEIGHT);
  bezier(LENGTH,0,LENGTH-LENGTH/4*sin(radians(BET)),HEIGHT/5,LENGTH-LENGTH/4*sin(radians(BET)),4*HEIGHT/5,LENGTH,HEIGHT);
  if(BRANCH > 0){
    pushMatrix(); //the first branched square
    translate(0,HEIGHT); //located one LENGTH straight up
    rotate(radians(ALPH));
    fractal(BRANCH, D,D*RATIO, ALPH);
    popMatrix();
     
    pushMatrix(); //the second branched square
    translate(PH,HEIGHT+A); //located (PH forward)and(the square LENGTH + A upward)
   // [ellistrated at the end of the code]
    rotate(radians(-BET));//rotated with the slope created from A and B
    fractal(BRANCH, C,C*RATIO,ALPH);
    popMatrix();
 
  }else if (BRANCH == 0){
    leaf(LENGTH/2,HEIGHT,(height-My)/50,0);
  }
}
void leaf(float Xleaf, float Yleaf, float leafSize, int leafColor){
  translate(Xleaf,Yleaf);
    fill(leafColor,200,0);
    bezier(0,0,leafSize,leafSize/2,0,4*leafSize/3,0,2*leafSize);
    bezier(0,0,-leafSize,leafSize/2,0,4*leafSize/3,0,2*leafSize);
}
/*PH    B
----- ----
 \)AL|BE(/
  \  |A /
  D\ | / C
    \|/
 
       just a small illistration for A, B, C, D
*/



