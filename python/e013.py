#!/usr/bin/python
#python version:2.7.2

# if the file is a binary file, please use "rb" mode to open
# "r" read
# "w" write
# "a" append
note1 = open("res/note1.txt", "r")

print '==============readlines============='
lines = note1.readlines()
print lines

for line in lines:
    print line
    content = line.split("=")

print '==============readline============='
note1.seek(0)
firstLine = note1.readline()
print "firstLine is ", firstLine
secondLine = note1.readline()
print "secondLine is ", secondLine
note1.seek(0)
firstLineAgain = note1.readline()
print firstLineAgain

note1.close()


note2 = open("res/note2.txt", "w")
note2.write("HelloWorld!\n")
# or redirecting output
print >> note2, "Hi, This is a test about how to write to a file\n"
note2.close()
