PVector location;
PVector velocity;

void setup()
{
    size(600, 300);
    background(200);
    smooth();

    location = new PVector(100, 100);
    velocity = new PVector(1.5, 3.3);
}

void draw()
{
    location.add(velocity);

    if ((location.x > width) || (location.x < 0)) {
        velocity.x *= -1;
    }

    if((location.y > height) || (location.y < 0)) {
        velocity.y *= -1;
    }

    stroke(0);
    fill(175);
    ellipse(location.x, location.y, 16, 16);
}
