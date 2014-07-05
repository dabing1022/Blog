#!/usr/bin/python
#python version:2.7.2

print "\n1. range---------------------------"
for i in range(1, 5):
    print i
else:
    print 'The for loop is over'

print range(2, 8)
print range(10)

print "\n2. []---------------------------"
print [i for i in range(10) if i % 2 == 0]

print "\n3. enumerate---------------------------"
i = 0
seq = ["one", "two", "three"]
for element in seq:
	seq[i] = '%d: %s' % (i, element)
	i += 1

print seq

seq2 = ["one2", "two2", "three2"]
for i, element in enumerate(seq2):
	seq2[i] = '%d: %s' % (i, element)

print seq2

print "\n4. function and enumerate---------------------------"
def _treatment(pos, element):
	return '%d: %s' % (pos, element)

seq3 = ["one3", "two3", "three3"]
print [_treatment(i, el) for i, el in enumerate(seq3)]

print "\n5. iter---------------------------"
i = iter('abcd')
print i.next()
print i.next()
print i.next()
print i.next()