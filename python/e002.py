#!/usr/bin/python
#python version:2.7.2

number = 23
guess = raw_input('Enter the number:')

if guess == number:
    print 'Congratulations, you guessed it.'
    print '(but you do not win any prizes)'
elif guess < number:
    print 'No, it is a litter higher than that'
elif guess > number:
    print 'No, it is a litter lower than that'
print 'Done'
