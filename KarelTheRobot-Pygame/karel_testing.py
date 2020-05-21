from karel_the_robot import World, Robot


@Robot.add_method(Robot)
def turn(Robot, x):
    x = (4 - (x % 4)) % 4
    for i in range(0, x):
        Robot.turn_left()


@Robot.add_method(Robot)
def move_x(Robot, x):
    for i in range(0, x):
        Robot.move()


world = World.from_file("example_world")
karel = Robot(2, 2, 90, 10, 1)
barel = Robot(3, 3, 0, 10)
sarel = Robot(4, 4, 180, 10)
warel = Robot(5, 5, 270, 10)

world.add_robots(karel, barel, sarel, warel)
world.start()
print(karel.get_sleeve_color())
print(barel.get_sleeve_color())
print(sarel.get_sleeve_color())
print(warel.get_sleeve_color())

sarel.turn_off()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_left()
karel.turn_off()
