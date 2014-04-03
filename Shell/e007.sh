#!/bin/bash
# while

echo "    ==== while"
flag=1
while [ $flag -le 4 ]
    do
        echo "Hello, world!"
        let flag="$flag + 1"
done

# if 
echo "    ==== if"
office=1
name="andy"
if [ $office -eq 1 ]
then
    if [ $name=="andy" ] 
    then
        echo "hello, andy:)"
    fi
else
    echo "Sorry:("
fi
