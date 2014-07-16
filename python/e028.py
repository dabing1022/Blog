# -*- coding: utf-8 -*-
import os

def cdWalker(cdrom, cdcfile):
    export = ''
    for root, dirs, files in os.walk(cdrom):
        export += "%s; %s; %s\n\n" % (root, dirs, files)

    open(cdcfile, 'a').write(export)

cdWalker("/Volumes/Work/github/Blog/python/", 'res/note5.txt')
cdWalker("/Volumes/Work/github/Blog/Chipmunk/", 'res/note6.txt')
