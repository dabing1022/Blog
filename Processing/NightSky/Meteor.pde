class Meteor 
{
  float headDiameter; //头部直径
  float tailLength; //尾巴长度

  float tailWidth; //尾巴宽度
  float lifeTime; //生命周期
  
  PVector pos; // 速度
  PVector speed; //位置
  Meteor() {
    reset();
  }
  
  void update() {
    pos.add(speed);
    
    lifeTime -= 0.35;
        
    if (isDead()) {
      reset(); //死亡后重置
    }
  }
  
  void display() {
    noStroke();
    fill(255);
    ellipse(pos.x, pos.y, headDiameter, headDiameter);
    
    translate(pos.x, pos.y);
   
    for (int i = 0; i < tailLength; i++) {
      float alpha = map(i, 0, tailLength, 100, 0);
      fill(255, alpha);
      tailWidth = map(i, 0, tailLength, headDiameter * 0.6, 0);
      ellipse(i, -i * abs(speed.y/speed.x), tailWidth, tailWidth);
    }
  }
  
  void reset() {
    this.headDiameter = random(6, 8);
    this.pos = new PVector(width/2 + random(width/2), -random(200)); //流星的起始位置
    this.tailLength = random(20)+150;
    this.lifeTime = random(20)+50; 
    
    float speedX = -(random(1) + 8);
    float speedY = -speedX;
    this.speed = new PVector(speedX, speedY); //速度向量，决定流星的坠落方向
  }
  
  
  boolean isDead() {
    return lifeTime < 0;
  }
}