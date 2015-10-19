#!/bin/bash
# strings

str='this is a string'
your_name='andy'
str2="Hello, I know you are \"$your_name\"!\n"
echo $str
echo $str2

str3="hello, "$your_name" !"
str4="hello, ${your_name} !"
str5="hello, $your_name !"
echo $str3 #hello, andy !
echo $str4 #hello, andy !
echo $str5 #hello, andy !

# get the length of string
str6="abcd"
echo ${#str6}

# get the substring of string
str7="My name is ChildhoodAndy"
echo ${str7:1:13}

echo ${BASH_SOURCE[0]}
echo $(dirname ${BASH_SOURCE})
