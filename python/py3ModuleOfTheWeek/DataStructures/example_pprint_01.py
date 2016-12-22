# coding:utf-8

from pprint import pprint

data = [
        (1, {'a': 'A', 'b': 'B', 'c': 'C', 'd': 'D'}),
        (2, {'e': 'E', 'f': 'F', 'g': 'G', 'h': 'H',
                      'i': 'I', 'j': 'J', 'k': 'K', 'l': 'L'}),
        (3, ['m', 'n']),
        (4, ['o', 'p', 'q']),
        (5, ['r', 's', 't''u', 'v', 'x', 'y', 'z']),

]

print('PRINT:')
print(data)
print()
print('PPRINT:')
pprint(data)
