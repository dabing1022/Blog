# coding:utf-8

import enum

class BugStatus(enum.Enum):
    test1 = 1
    test2 = 2
    test3 = 3
    test4 = 4

for status in BugStatus:
    print('{:15} = {}'.format(status.name, status.value))

print('\ntest1 name: {}'.format(BugStatus.test1.name))
print('test1 value: {}'.format(BugStatus.test1.value))
