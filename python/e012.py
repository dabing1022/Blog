#!/usr/bin/python
#python version:2.7.2

# class simple test
class Ball:
    def __init__(self, color, size, direction):
        self.color = color
        self.size = size
        self.direction = direction

    def bounce(self):
        if self.direction == "down":
            self.direction = "up"

    def __str__(self):
        msg = "Hi, I'm a " + self.size + " " + self.color + " ball!"
        return msg


myBall = Ball("red", "small", "down")
print "I just created a ball"
print 
print "myball color is ", myBall.color
print "myball size is ", myBall.size
print "myball direction is ", myBall.direction

myBall.bounce()
print "After myBall bounce, now myball direction is", myBall.direction

print myBall
