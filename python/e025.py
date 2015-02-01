#!/usr/bin/python
#python version:2.7.2

# Reg exercise02
import re

print '--------------------------------------------------'
s = r'ABC\-001'
print re.match(r'^\d{3}\-\d{3,8}$', '010-12345').group()
print re.match(r'^(\d{3})\-(\d{3,8})$', '010-12345').group(0)
print re.match(r'^(\d{3})\-(\d{3,8})$', '010-12345').group(1)
print re.match(r'^(\d{3})\-(\d{3,8})$', '010-12345').group(2)


print '--------------------------------------------------'
testStr = 'someone@gmail.com'
testStr2 = 'bill.gates@microsoft.com'
testStr3 = '<Tom Paris> tom@voyager.org'

s = r'^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$'
print re.match(s, testStr).group()
print re.match(s, testStr2).group()

s = r'^<(\w)+(\s)*(\w)+>'
name = re.match(s, testStr3).group()
print name
print name[1:-1]

print '--------------------------------------------------'
pattern = 'this'
text = 'Does this text match the pattern?'

match = re.search(pattern, text)

s = match.start()
e = match.end()

print 'Found "%s"\nin "%s" from %d to %d "%s"' % (match.re.pattern, match.string, s, e, text[s:e])