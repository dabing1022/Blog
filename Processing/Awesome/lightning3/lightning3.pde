/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/2924*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
int width = 900;
int height = 500;


float maxDTheta = PI/10;
float minDTheta = PI/20;
float maxTheta = PI/2;
float childGenOdds = .01;

float minBoltWidth = 3;
float maxBoltWidth = 10;

float minJumpLength = 1;
float maxJumpLength = 10;

boolean stormMode = true;
boolean fadeStrikes = true;
boolean randomColors = false;
float maxTimeBetweenStrikes = 3000;

//color yellow = color(59,99,99);
//color red = color(0,99,99);
color boltColor;
color skyColor;


lightningBolt bolt;
float lastStrike = 0;
float nextStrikeInNms = 0;

boolean playThunder = false;
boolean useDing = false;
//AudioSample thunderSound;

//distance, in milliseconds, of the storm.
float meanDistance = 0;
//if the current time matches the time in this arraylist, it should fire!
ArrayList thunderTimes = new ArrayList();


void setup(){
  colorMode(HSB,100);
  smooth();
  size(width, height);
//  thunderSound = minim.loadSample("thunder.mp3");
  noFill();
  meanDistance = 1000*.5;
  
//  yellow = color(60/3.6,99,99);
//  red = color(0,99,99);
  boltColor = color(0,0,99);
  skyColor = color(0,0,10,20);
  background(skyColor);

  bolt = new lightningBolt(random(0,width),0,random(minBoltWidth,maxBoltWidth),0,minJumpLength,maxJumpLength,boltColor);
}

void draw(){
  //check if any of the stored times need to make a 'ding'
  if(playThunder && thunderTimes.size() > 0)
    if(millis() > (Float)thunderTimes.get(0)){
      thunderTimes.remove(0);
//      thunderSound.trigger();
      println("boom!");
    }
  
  if(stormMode && millis()-lastStrike>nextStrikeInNms){//time for a new bolt?
    lastStrike = millis();
    nextStrikeInNms = random(0,maxTimeBetweenStrikes);
    
    bolt = new lightningBolt(random(0,width),0,random(minBoltWidth,maxBoltWidth),0,minJumpLength,maxJumpLength,boltColor);
    bolt.draw();
    if(playThunder)
      thunderTimes.add(bolt.getThunderTime());
  }
  else{
    if(fadeStrikes){
      noStroke();
      fill(skyColor);
      rect(0,0,width,height);
      noFill();
    }
  }
}

void stop(){
//  thunderSound.close();
  super.stop();
}

int randomSign() //returns +1 or -1
{
  float num = random(-1,1);
  if(num==0)
    return -1;
  else
    return (int)(num/abs(num));
}

color randomColor(){
  return color(random(0,100),99,99);
}

color slightlyRandomColor(color inputCol,float length){
  float h = hue(inputCol);
  h = (h+random(-length,length))%100;
  return color(h,99,99);
}
