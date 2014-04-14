#!/usr/bin/python
#python version:2.7.2

# DocStrings

def printMax(x, y):
    '''Prints the maximum of two numbers.
The two values must be integers.'''
    x = int(x)
    y = int(y)
    
    if x > y:
        print x, 'is maximum'
    else:
        print y, 'is maximum'
printMax(3, 5)
print printMax.__doc__
