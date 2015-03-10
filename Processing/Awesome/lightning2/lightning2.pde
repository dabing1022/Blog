float X1;
float Y1;
float X2;
float Y2;
 
float x1;
float y1;
float x2;
float y2;
void setup()
{
  size(600,600);
 
 
X1=random(0,width);
Y1=0;
}
 
void draw()
{
if(mousePressed){
    for(int i = 0;i<10;i=i+1){
 
    X2= X1+random(-50,50);
Y2= Y1+random(20,50);
 
strokeWeight(1.5*11-1.5*i);
stroke(250,230,110);
line(X1,Y1,X2,Y2);
 
strokeWeight(11-i);
stroke(200,200,80);
line(X1,Y1,X2,Y2);
 
X1=X2;
Y1=Y2;
 
    }
  for(int i = 0;i<10;i=i+1){
 
    x2= x1+random(-80,80);
y2= y1+random(0,80);
strokeWeight(11/2-i/2);
stroke(200,200,80);
line(x1,y1,x2,y2);
x1=x2;
y1=y2;
 
    }
  
  
  }else{  background(30,30,80);
X1=random(0,width);
Y1=0;
x1=random(0,width);
y1=0;
 
  } 
}

