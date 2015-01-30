void setup() {
    size(640,360);
    background(0);
    drawCircle(width/2,height/2,width);
}

void drawCircle(int x, int y, float r) {
    stroke(255);
    noFill();
    ellipse(x, y, r, r);
    if(r > 2) {
        r *= 0.90f;
        drawCircle(x, y, r);
    }
}
