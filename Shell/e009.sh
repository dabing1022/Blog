#!/bin/bash
# for in

echo "Mary"
echo "Joe"
echo "Sue"

echo "-------use while ---------"
counter=1
while [ $counter -lt 5 ]
do
    case $counter in
    1)
        echo "Hello, Marry"
    ;;
    2)
        echo "Hello, Joe"
    ;;
    3)
        echo "Hello, Sue"
    ;;
    esac
    let counter="$counter + 1"
done


echo "-------use for in ----------"
for friend in Mary Joe Sue
do
    echo "Hello, $friend"
done

# 如果空格也是字符串的一部分，需要在字符串的两边加上引号
for friend in "Mary Jones" "Joe Smith" "Sue Jones"
do
    echo "Hello, $friend"
done
