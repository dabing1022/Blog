#coding:utf-8
import hashlib
import os
import codecs

PROJECT_DIR = '../../zhuanzhuan/zhuanzhuan/'

def hashAllImages():
    hash_map = {}
    index = 0
    for root, dirs, files in os.walk(os.path.abspath(PROJECT_DIR)):
        for file in files:
            if (file.endswith('.png') or file.endswith('.jpg') or file.endswith('.jpeg') or file.endswith('.gif') or file.endswith('.bmp')):
                abspath = os.path.join(root, file)
                fdata = open(abspath, "r", encoding='utf-8', errors='ignore').read()
                md5hex = hashlib.md5(fdata.encode('utf-8')).hexdigest()
                if (md5hex in hash_map):
                    paths = hash_map[md5hex]
                    paths.append(file)
                else:
                    hash_map[md5hex] = [file]

    return hash_map

def filterRepeatImages():
    result = hashAllImages()
    totalCount = 0
    for (key, value) in result.items():
        if (len(value) > 1):
            totalCount += len(value)
            print(value)
    print("重复图片总数为：" + str(totalCount))

if __name__ == "__main__":
    filterRepeatImages()
