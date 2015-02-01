#!/usr/bin/python
#python version:2.7.2

a = '76.3'
b = float(a)
print 'a type is ', type(a), ' value is ', a
print 'b type is ', type(b), ' value is ', b

c = 100.0234324
print 'c int is %d' % c
print 'c .2f is %e.2f' % c
print 'c is %+f' % c
c = -1 * c
print 'c is %+f' % c

print 'c is % .2f' % c
d = 200.234234
print 'd is % .2f' % d

print 'This is wrong, it will print <ValueError: could not convert string to float: ChildhoodAndy>'
print float("ChildhoodAndy")
