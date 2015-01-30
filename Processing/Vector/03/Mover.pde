class Mover
{
    PVector location;
    PVector velocity;
    PVector acceleration;
    float topSpeed;
    float r;
    float red;
    float green;
    float blue;

    Mover() {
        location = new PVector(width/2, height/2);
        velocity = new PVector(0, 0);

        topSpeed = 10;
        r = random(20) + 30;
        red = random(255);
        green = random(255);
        blue = random(255);
    }

    void checkEdge() {
        if (location.x > width) {
            location.x = 0;
            } else if (location.x < 0) {
                location.x = width;
            }

            if (location.y > height) {
                location.y = 0;
                } else if (location.y < 0) {
                    location.y = height;
                }
            }

    void update() {
        acceleration = PVector.random2D();
        acceleration.mult(random(10) / r);
        velocity.add(acceleration);
        velocity.limit(topSpeed);
        location.add(velocity);
    }

    void display() {
        noStroke();
        fill(red, green, blue);
        ellipse(location.x, location.y, r, r);
    }
}
