void keyTyped(){
  if(key==' ')
  {
    noStroke();
    fill(0);
    rect(0,0,width,height);
    noFill();
    stroke(255,255,0);
  }
  if(key=='f'){
    fadeStrikes = !fadeStrikes;
  }
  if(key=='s'){
    //   println("toggle storm @"+lastStrike/1000);
    stormMode = !stormMode;
  }
  if(key=='-' || key=='_'){
    maxTimeBetweenStrikes = maxTimeBetweenStrikes*1.2;
  } 
  if(key=='=' || key=='+'){
    maxTimeBetweenStrikes = maxTimeBetweenStrikes/1.2;
  }  
  if(key=='R' || key=='r'){
    maxTimeBetweenStrikes = 3000;
  }  
  if(key=='C' || key=='c'){
    randomColors = !randomColors;
  }  
  if(key=='A' || key=='a'){
    playThunder = !playThunder;
  }
  //lets you switch between thunder and bell!
//  if(key=='p' || key=='P'){
//    if(useDing)
//      thunderSound = minim.loadSample("thunder.mp3");
//    else
//      thunderSound = minim.loadSample("bell.wav");
//    useDing = !useDing;
//  }
}


void mouseClicked(){
//  println("click!");
  bolt = new lightningBolt(random(0,width),0,random(minBoltWidth,maxBoltWidth),0,minJumpLength,maxJumpLength,boltColor);
  bolt.draw();
  thunderTimes.add(bolt.getThunderTime());
}

