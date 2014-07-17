# -*- coding: utf-8 -*-
import os
import sys

# 多个参数 运行命令 python e029.py -e res/cd.cdc
CDROM = '/Volumes/Work/github/Blog/python/'

def cdWalker(cdrom, cdcfile):
    export = ''
    for root, dirs, files in os.walk(cdrom):
        export += "%s; %s; %s\n\n" % (root, dirs, files)

    open(cdcfile, 'a').write(export)

if "-e" == sys.argv[1]:
    cdWalker(CDROM, sys.argv[2])
    print "记录光盘信息到 %s" % sys.argv[2]
else:
    print '''PyCDC使用方式：
    python e029.py -e "res/cd.cdc"
    # 将光盘内容记录为 cd.cdc
    '''
