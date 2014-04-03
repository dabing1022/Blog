#!/bin/bash
# linux operation

# + - * / %
let a=30
let b=16
let s1="$a + $b"
let s2="$a - $b"
let s3="$a * $b"
let s4="$a / $b"
let s5="$a % $b"
echo $s1
echo $s2
echo $s3
echo $s4
echo $s5

# ! && ||
# -eq
echo "=================="
m1=3000
m2=2000
echo $m1
echo $m2
echo "(=)"
[ $m1 -eq $m2 ]
echo "$?"

echo "(!=)"
if [ $m1 -eq $m2 ]
then  
    echo "equal"
else
    echo "nequal"
fi
echo "$?"

echo "(>)"
test $m1 -gt $m2
echo "$?"

# >=  ------> -ge
# <   ------> -lt
# <=  ------> -le

let n=100
if [ $n -eq 100 ]
    then
        echo "The box is full."
    else
        echo "The box is not full."
fi
