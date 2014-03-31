#!/bin/bash
# save to file
clear
echo ""
echo "Enter your First Name:"
read FirstName
echo "Enter your Last Name:"
read LastName
echo $FirstName $LastName

cd /Volumes/Work/github/Blog/Shell/shellTest/
# > save file
# echo "$FirstName $LastName" > info.dat
echo "$FirstName $LastName" >> info.dat

# >> add data to file
echo "This is a test file" >> info.dat

# cat show the file data
echo "============show the file data============="
cat info.dat
