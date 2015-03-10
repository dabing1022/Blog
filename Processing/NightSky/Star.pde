class Star
{
  PVector pos;
  float diameter;
  float step;
  float MAX_DIAMETER = 6; // 最大直径
  float MIN_DIAMETER = 2; // 最小直径
  Star(PVector pos) {
    this.pos = pos;
    this.diameter = random(MIN_DIAMETER, MAX_DIAMETER); // 直径随机 星星大小不一
    this.step = random(0, 5);
  }
  
  void display() {
    step += 0.05;
    float d = abs(diameter*sin(step));    //直径正弦变化
    noStroke();
    fill(255);
    ellipse(pos.x, pos.y, d, d);
  }
  

}