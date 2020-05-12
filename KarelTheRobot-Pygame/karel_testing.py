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
karel = Robot(5, 5, 90, 10)
karel.set_zoom_fps(100)

world.add_robots(karel)
world.start()

karel.turn(-2)
karel.move_x(2)
karel.turn(1)
karel.move_x(3)
karel.turn(-1)
karel.move_x(1)
karel.turn(-1)
karel.move_x(2)
karel.turn(1)
karel.move_x(1)
karel.turn(1)

karel.move_x(3)
karel.pick_beeper()
karel.turn(1)
karel.move_x(1)
karel.pick_beeper()
karel.move_x(1)
karel.put_beeper()
karel.move_x(1)
karel.put_beeper()
karel.move_x(1)
karel.put_beeper()
karel.move_x(1)
karel.put_beeper()

karel.turn_off()
