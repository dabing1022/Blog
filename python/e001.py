#!/usr/bin/python
#python version:2.7.2
print "Hello world!"

i = 5
print i
i = i + 1
print i
s = '''This is a multi-line string
                This is the second line'''
print s

i = 7; print i;

s = 'This ia a string \
This continues the string'
print s

width = 5
height = 2
area = width * height
print 'area is', area

print '------------------str--------------------'
print 'age ' + str(5)
print 'age', 6

def fibonacci(n):
    a, b = 1, 1
    for i in range(n - 1):
        a, b = b, a + b
    return a

print fibonacci(10)
