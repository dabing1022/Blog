# coding:utf-8

import os.path
import time

PATH = [
    '/one/two/three',
    '/one/two/three/',
    '/',
    '.',
    '',
    'filename.txt',
    'filename',
    '/path/to/filename.txt',
    '/',
    '',
    'my-archive.tar.gz',
    'no-extension.',
]

for path in PATH:
    print('{!r:>17} : {}'.format(path, os.path.split(path)))
    print('{!r:>21} : {!r}'.format(path, os.path.splitext(path)))



print('File            ', __file__)
print('Access time  :', time.ctime(os.path.getatime(__file__)))
print('Modified time:', time.ctime(os.path.getmtime(__file__)))
print('Change time  :', time.ctime(os.path.getctime(__file__)))
print('Size         :', os.path.getsize(__file__))
