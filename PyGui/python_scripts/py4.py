import sys

print 'Number of arguments:', len(sys.argv), 'arguments.'
print 'Argument List:', str(sys.argv)

number1=sys.argv[1]
number2=sys.argv[2]

# Using arithmetic + Operator to add two numbers
sum = float(number1) + float(number2)
print('The sum of {0} and {1} is {2}'.format(number2, number1, sum))
