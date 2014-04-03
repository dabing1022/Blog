#!/bin/bash
# case

while true
    do
        echo "Please input a interger number between 1-5"
        read number
        case $number in
        1)
            echo "you have input a number:1"
            ;;
        2)
            echo "you have input a number:2"
            ;;
        3)
            echo "you have input a number:3"
            ;;
        4)
            echo "you have input a number:4"
            ;;
        5)
            echo "you have input a number:5"
            ;;
        *)
            echo "Sorry, input wrong happened!"
            ;;
        esac
done
