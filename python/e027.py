# -*- coding: utf-8 -*-
import os, sys

print '''os.listdir() -- 返回指定目录下的所有文件和目录名\n'''

print 'sys.argv: ', sys.argv
print os.listdir("/Volumes/Work/github/Blog/python/")

# ['.DS_Store', 'e001.py', 'e002.py', 'e003.py', 'e004.py', 'e005.py', 'e006.py', 'e007.py', 'e008.py', 'e009.py', 'e010.py', 'e011.py', 'e012.py', 'e013.py', 'e014.py', 'e015.py', 'e016.py', 'e017.py', 'e018.py', 'e019.py', 'e020.py', 'e021.py', 'e022.py', 'e023.py', 'e024.py', 'e025.py', 'e026.py', 'e027.py', 'e028.py', 'res']

export = ''
for root, dirs, files in os.walk("/Volumes/Work/github/Blog/python/"):
    print 'root:', root
    print 'dirs:', dirs
    print 'files:', files
    export += "%s; %s; %s\n\n" % (root, dirs, files)
#open('res/note4.txt', 'a').write("%s; %s; %s\n\n" % (root, dirs, files))
