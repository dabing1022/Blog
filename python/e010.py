#!/usr/bin/python
#python version:2.7.2

a = '76.3'
b = float(a)
print 'a type is ', type(a), ' value is ', a
print 'b type is ', type(b), ' value is ', b

print 'This is wrong, it will print <ValueError: could not convert string to float: ChildhoodAndy>'
print float("ChildhoodAndy")
