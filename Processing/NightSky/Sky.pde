class Sky 
{
  int Y_AXIS = 1;
  int X_AXIS = 2;
  private color c1;
  private color c2;
  private float skyW;
  private float skyH;
  Sky(float skyWidth, float skyHeight) {
    this.skyW = skyWidth;
    this.skyH = skyHeight;
    this.c1 = color(8, 40, 90);
    this.c2 = color(190, 220, 250);
  }
  
  void display() {
    setGradient(0, 0, skyW, skyH, c1, c2, Y_AXIS); //夜空背景色为纵向渐变色，渐变色两端颜色为c1和c2
  }
  
  void setGradient(int x, int y, float w, float h, color c1, color c2, int axis ) {
    noFill();
    if (axis == Y_AXIS) {  // Top to bottom gradient
      for (int i = y; i <= y+h; i++) {
        float inter = map(i, y, y+h, 0, 1);
        color c = lerpColor(c1, c2, inter);
        stroke(c);
        line(x, i, x+w, i);
      }
    }  
    else if (axis == X_AXIS) {  // Left to right gradient
      for (int i = x; i <= x+w; i++) {
        float inter = map(i, x, x+w, 0, 1);
        color c = lerpColor(c1, c2, inter);
        stroke(c);
        line(i, y, i, y+h);
      }
    }
  }
}