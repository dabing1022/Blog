void setup()
{
    size(400, 400);
}

void draw()
{
    background(255);

    PVector mouse = new PVector(mouseX, mouseY);
    PVector center = new PVector(width/2, height/2);

    mouse.sub(center);
    // mouse.mult(0.5);
    mouse.normalize();
    mouse.mult(200);
    translate(center.x, center.y);
    line(0, 0, mouse.x, mouse.y);
}
