#!/bin/bash
# create a dir and create some empty files

cd /Volumes/Work/testProjects/test 
mkdir shellTest
cd shellTest

for ((i = 0; i < 5; i++)); do
    touch shellTest_$i.txt
done

