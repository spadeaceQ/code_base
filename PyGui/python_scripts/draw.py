import turtle               # allows us to use the turtles library
wn = turtle.Screen()        # creates a graphics window

alex = turtle.Turtle()
alex.shape('turtle')

deg=360# create a turtle named alex
print(deg)
alex.speed(0)
#p= int(input('Enter number to squares:'))

p=10
for n in range(0,p):     # First Example

    alex.forward(150)           # tell alex to move forward by 150 units
    alex.left(90)
    alex.forward(150)  # tell alex to move forward by 150 units
    alex.left(90)  # turn by 90 degrees
    alex.forward(150)  # tell alex to move forward by 150 units
    alex.left(90)  # turn by 90 degrees
    alex.forward(150)  # tell alex to move forward by 150 units
    alex.left(90)
    alex.left(deg/p)# turn by 90 degrees
# turn by 90 degrees


turtle.exitonclick()
