#!/usr/bin/python
#python version:2.7.2

# use pickle
import pickle

myList = ['China', 2, "Hi", 200.0, 10e-13]

myListFile = open("res/note3.txt", "w")
pickle.dump(myList, myListFile)

myListFile.close()

myListFile = open("res/note3.txt", "r")
recovered_list = pickle.load(myListFile)
myListFile.close()

print recovered_list
