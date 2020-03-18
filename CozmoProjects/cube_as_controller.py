import datetime
import math
import sys
import time

try:
    from PIL import Image, ImageDraw, ImageFont
except ImportError:
    sys.exit("Cannot import from PIL. Do `pip3 install --user Pillow` to install")

import cozmo

NUM = 0


def make_text_image(text_to_draw, x, y, font=None):
    '''Make a PIL.Image with the given text printed on it

    Args:
        text_to_draw (string): the text to draw to the image
        x (int): x pixel location
        y (int): y pixel location
        font (PIL.ImageFont): the font to use

    Returns:
        :class:(`PIL.Image.Image`): a PIL image with the text drawn on it
    '''

    # make a blank image for the text, initialized to opaque black
    text_image = Image.new('RGBA', cozmo.oled_face.dimensions(), (0, 0, 0, 255))

    # get a drawing context
    dc = ImageDraw.Draw(text_image)

    # draw the text
    dc.text((x, y), text_to_draw, fill=(255, 255, 255, 255), font=font)

    return text_image


def handle_object_moving_started(evt, **kw):
    # This will be called whenever an EvtObjectMovingStarted event is dispatched -
    # whenever we detect a cube starts moving (via an accelerometer in the cube)
    # print("Object %s started moving: acceleration=%s" %
    # (evt.obj.object_id, evt.acceleration))
    print("Object %s started moving: acceleration=%s" %
          (evt.obj.object_id, vector_magnitude(evt.acceleration)))


def handle_object_moving(evt, **kw):
    global NUM
    # This will be called whenever an EvtObjectMoving event is dispatched -
    # whenever we detect a cube is still moving a (via an accelerometer in the cube)
    # print("Object %s is moving: acceleration=%s, duration=%.1f seconds" %
    # (evt.obj.object_id, evt.acceleration, evt.move_duration))
    print("Object %s is moving: acceleration=%s, duration=%.1f seconds" %
          (evt.obj.object_id, vector_magnitude(evt.acceleration), evt.move_duration))
    NUM = (NUM + vector_magnitude(evt.acceleration)) % 100


def handle_object_moving_stopped(evt, **kw):
    # This will be called whenever an EvtObjectMovingStopped event is dispatched -
    # whenever we detect a cube stopped moving (via an accelerometer in the cube)
    print("Object %s stopped moving: duration=%.1f seconds" %
          (evt.obj.object_id, evt.move_duration))


def vector_magnitude(vector):
    return math.sqrt(math.pow(vector.x, 2) + math.pow(vector.y, 2) + math.pow(vector.z, 2))


def alarm_clock(robot: cozmo.robot.Robot):
    """robot.add_event_handler(cozmo.objects.EvtObjectMovingStarted, handle_object_moving_started)
    robot.add_event_handler(cozmo.objects.EvtObjectMoving, handle_object_moving)
    robot.add_event_handler(cozmo.objects.EvtObjectMovingStopped, handle_object_moving_stopped)"""
    cube = robot.world.wait_for_observed_light_cube(timeout=30)
    print(cube.pose.position.x)

    while True:
        X = cube.pose.position.y
        Y = cube.pose.position.z  # 135 is center
        # Create the updated image with this time
        image = make_text_image("1", X * 2 + 34, 135 - Y)  # y=25 is bottom of screen
        oled_face_data = cozmo.oled_face.convert_image_to_screen_data(image)

        # display for 1 second
        robot.display_oled_face_image(oled_face_data, 1000.0)

        # only sleep for a fraction of a second to ensure we update the seconds as soon as they change
        time.sleep(0.001)


cozmo.robot.Robot.drive_off_charger_on_connect = False  # Cozmo can stay on his charger for this example
cozmo.run_program(alarm_clock)
