#!/usr/bin/python
#python version:2.7.2

# list
friends = []
friends.append('David')
friends.append('Lily')
friends.append('Kaer')
friends.append("Andy")

print friends
print friends[:]
print friends[1:3]
print friends[:3]
print friends[1:]


# modify
friends[2] = "Childhood"
print friends

# extend
friends.extend(['Tom', 'Jerry', 'Frank'])
# append
friends.append(['Tom', 'Jerry', 'Frank'])
print friends

# insert
friends.insert(2, 'Alan')
print friends

print '========remove============='
friends.remove('Andy')
print friends

print '========del============='
del friends[3]
print friends

print '========pop============='
print '1. pop()  remove the last element'
print '2. pop(index) remove the index element'
popElement = friends.pop()
print popElement
print friends

popElement = friends.pop(0)
print popElement
print friends

print '========search=============='
if 'Tom' in friends:
    print 'Tom in friends, and the index is', friends.index('Tom')
else:
    print 'Tom not in friends'


print '========loop================'
for friend in friends:
    print friend

print '========sort================'
friends.sort()
print friends

# friends.sort(reverse = True)
friends.reverse()
print friends

print '========sorted=============='
sorted(friends)
print friends
