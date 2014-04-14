#!/usr/bin/python
#python version:2.7.2

def sayHello():
    print 'Hello world!'

sayHello()


def printMax(a, b):
    if a > b:
        print a, 'is maximum'
    else:
        print b, 'is maximum'
printMax(3, 4)


# local var
def func(x):
    print 'x is', x
    x = 2
    print 'Changed local x to', x
x = 50
func(x)
print 'x is still', x

# global var
def func2():
    global x
    print 'x is', x
    x = 2
    print 'Changed local x to', x
x = 50
func2()
print 'Value of x is', x

# default params
def say(message, times = 1):
    print message * times
say('Hello') #Hello
say('World', 5) #WorldWorldWorldWorldWorld

# the key params
def func(a, b = 5, c = 10):
    print 'a is', a, 'and b is', b, 'and c is', c

func(3, 7)
func(25, c = 24)
func(c = 50, a = 100)
