#!/usr/bin/python
#python version:2.7.2

print "===========split============"
a = "a, b, c, d, e, f, g"
b = a.split(",")
print b


c = a.split("d,")
print c

print "============join============="
d = ["My", "name", "is", "Frank!"]
e = " ".join(d)
f = "==WOOF WOOF==".join(d)
print e
print f

print "=============search==========="
name = "ChildhoodAndy"
print name.startswith("Ch")
print name.endswith("dx")

sentence = "This is my my book!"
if "my" in sentence:
    print "the word(my) is in the sentence"
    print sentence.index("my")


stripped_sentence = sentence.strip("k!")
print stripped_sentence

sentence = "This is my book!     "
blank_stripped = sentence.strip()
print blank_stripped

print "==========upper/lower============="
a = "abc"
b = "DEF"
print a.upper()
print b.lower()
