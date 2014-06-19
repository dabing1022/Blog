#!/usr/bin/python
#python version:2.7.2

# Reg exercise01
import re

print '--------------------------------------------------'
pattern = re.compile('hello')

match1 = pattern.match('hello world!')
match2 = pattern.match('helloo world!')
match3 = pattern.match('helllo world!')

if match1:
    print match1.group()
else:
    print 'match1 failure!'


if match2:
    print match2.group()
else:
    print 'match2 failure!'

if match3:
    print match3.group()
else:
    print 'match3 failure!'

print '--------------------------------------------------'
a = re.compile("""\d +  # the integral part
                   \.    # the decimal point
                   \d *  # some fractional digits""", re.X)

b = re.compile("\d+\.\d*")

match11 = a.match('3.1415')
match12 = a.match('33')
match21 = b.match('3.1415')
match22 = b.match('33')

if match11:
    print match11.group()
else:
    print 'match11 not decimal'

if match12:
    print match12.group()
else:
    print 'match12 not decimal'

if match21:
    print match21.group()
else:
    print 'match21 not decimal'

if match22:
    print match22.group()
else:
    print 'match22 not decimal'
