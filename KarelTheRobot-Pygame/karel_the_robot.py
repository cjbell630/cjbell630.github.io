import pygame
import numpy
import threading
import time
from functools import wraps
from itertools import count

SCREEN_WIDTH = 600
SCREEN_HEIGHT = 600
TILE_WIDTH = 10
#TODO: maybe make has moved this frame a global
FPS = 4

BEEPERS = pygame.sprite.Group()
WALLS = []

# COLORS
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
L_GREY = (158, 158, 158)
D_GREY = (56, 56, 56)
GREEN = (91, 153, 0)
RED = (240, 0, 0)
L_BLUE = (0, 124, 150)
D_BLUE = (34, 25, 117)
YELLOW = (255, 236, 0)
CLR = (1, 1, 1)
ORANGE = (255, 165, 0)
PINK = (255, 165, 245)
TEAL = (0, 160, 180)
PURPLE = (200, 0, 255)

SLEEVE_COLORS = [
    ("Green", GREEN), ("Orange", ORANGE), ("Pink", PINK), ("Blue", TEAL), ("Purple", PURPLE)
]

KAREL_ON = numpy.array([
    [CLR, CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, BLACK, BLACK, CLR, CLR, CLR, BLACK, BLACK, BLACK, BLACK, CLR, CLR, CLR,
     CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, RED, RED, RED, BLACK, CLR, CLR, CLR, BLACK, RED, RED, RED, BLACK, CLR, CLR, CLR,
     CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, RED, RED, RED, RED, BLACK, D_BLUE, BLACK, RED, RED, RED, RED, BLACK, CLR, CLR, CLR,
     CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK,
     CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, BLACK, BLACK, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW,
     YELLOW, BLACK, BLACK, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, D_GREY, BLACK, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW,
     YELLOW, YELLOW, BLACK, D_GREY, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, D_GREY, BLACK, YELLOW, YELLOW, YELLOW, BLACK, YELLOW, YELLOW, YELLOW, BLACK, YELLOW, YELLOW,
     YELLOW, BLACK, D_GREY, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, GREEN, BLACK, YELLOW, YELLOW, YELLOW, BLACK, YELLOW, YELLOW, BLACK, YELLOW, YELLOW, YELLOW,
     YELLOW, BLACK, GREEN, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, GREEN, BLACK, YELLOW, YELLOW, YELLOW, BLACK, YELLOW, BLACK, YELLOW, YELLOW, YELLOW, YELLOW,
     YELLOW, BLACK, GREEN, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, GREEN, BLACK, YELLOW, YELLOW, YELLOW, BLACK, BLACK, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW,
     YELLOW, BLACK, GREEN, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, GREEN, BLACK, YELLOW, YELLOW, YELLOW, BLACK, YELLOW, BLACK, YELLOW, YELLOW, YELLOW, YELLOW,
     YELLOW, BLACK, GREEN, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, GREEN, BLACK, YELLOW, YELLOW, YELLOW, BLACK, YELLOW, YELLOW, BLACK, YELLOW, YELLOW, YELLOW,
     YELLOW, BLACK, GREEN, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, GREEN, BLACK, YELLOW, YELLOW, YELLOW, BLACK, YELLOW, YELLOW, YELLOW, BLACK, YELLOW, YELLOW,
     YELLOW, BLACK, GREEN, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, BLACK, BLACK, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW, YELLOW,
     YELLOW, BLACK, BLACK, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, YELLOW, YELLOW, YELLOW, BLACK, BLACK, BLACK, YELLOW, YELLOW, YELLOW, BLACK,
     BLACK, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, BLACK, BLACK, L_GREY, L_GREY, L_GREY, BLACK, BLACK, BLACK, BLACK, CLR,
     CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, BLACK, L_GREY, L_GREY, L_GREY, L_GREY,
     L_GREY, BLACK, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, L_GREY, L_GREY, D_GREY, D_GREY, L_GREY, L_GREY, L_GREY, D_GREY, D_GREY, L_GREY,
     L_GREY, BLACK, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, L_GREY, D_GREY, BLACK, BLACK, D_GREY, L_GREY, D_GREY, BLACK, BLACK, D_GREY, L_GREY,
     BLACK, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, L_GREY, D_GREY, L_BLUE, BLACK, D_GREY, L_GREY, D_GREY, L_BLUE, BLACK, D_GREY,
     L_GREY, BLACK, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, L_GREY, L_GREY, D_GREY, D_GREY, L_GREY, L_GREY, L_GREY, D_GREY, D_GREY, L_GREY,
     L_GREY, BLACK, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, CLR, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, BLACK,
     CLR, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, CLR, CLR, CLR,
     CLR, CLR, CLR, CLR]
])

BEEPER = [
    [CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, CLR, CLR, CLR, CLR, CLR,
     CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, D_GREY, D_GREY, D_GREY, D_GREY, D_GREY, D_GREY, D_GREY, BLACK, BLACK,
     CLR, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, D_GREY, D_GREY, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, D_GREY, D_GREY,
     BLACK, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, BLACK, D_GREY, BLACK, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, BLACK,
     BLACK, D_GREY, BLACK, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, BLACK, D_GREY, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY,
     L_GREY, BLACK, D_GREY, D_GREY, BLACK, CLR, CLR, CLR],
    [CLR, CLR, BLACK, D_GREY, BLACK, BLACK, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY,
     L_GREY, BLACK, BLACK, BLACK, D_GREY, BLACK, CLR, CLR],
    [CLR, CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, BLACK, L_GREY, L_GREY, BLACK, BLACK, BLACK, L_GREY, L_GREY, BLACK,
     L_GREY, L_GREY, BLACK, D_GREY, BLACK, CLR, CLR],
    [CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, BLACK, RED, RED, RED, BLACK, L_GREY, L_GREY,
     L_GREY, L_GREY, L_GREY, BLACK, D_GREY, BLACK, CLR],
    [CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, BLACK, RED, RED, RED, RED, RED, BLACK, L_GREY, L_GREY,
     L_GREY, L_GREY, BLACK, D_GREY, BLACK, CLR],
    [CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, BLACK, RED, RED, RED, RED, RED, RED, RED, BLACK, L_GREY, L_GREY,
     L_GREY, BLACK, D_GREY, BLACK, CLR],
    [CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, BLACK, RED, RED, RED, RED, RED, RED, RED, BLACK, L_GREY, L_GREY,
     L_GREY, BLACK, D_GREY, BLACK, CLR],
    [CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, BLACK, RED, RED, RED, RED, RED, RED, RED, BLACK, L_GREY, L_GREY,
     L_GREY, BLACK, D_GREY, BLACK, CLR],
    [CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, BLACK, RED, RED, RED, RED, RED, BLACK, L_GREY, L_GREY,
     L_GREY, L_GREY, BLACK, D_GREY, BLACK, CLR],
    [CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, BLACK, RED, RED, RED, BLACK, L_GREY, L_GREY,
     L_GREY, L_GREY, L_GREY, BLACK, D_GREY, BLACK, CLR],
    [CLR, CLR, BLACK, D_GREY, BLACK, L_GREY, L_GREY, BLACK, L_GREY, L_GREY, BLACK, BLACK, BLACK, L_GREY, L_GREY, BLACK,
     L_GREY, L_GREY, BLACK, D_GREY, BLACK, CLR, CLR],
    [CLR, CLR, BLACK, D_GREY, BLACK, BLACK, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY,
     L_GREY, BLACK, BLACK, BLACK, D_GREY, BLACK, CLR, CLR],
    [CLR, CLR, CLR, BLACK, D_GREY, D_GREY, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY,
     L_GREY, BLACK, D_GREY, D_GREY, BLACK, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, BLACK, D_GREY, BLACK, BLACK, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, L_GREY, BLACK,
     BLACK, D_GREY, BLACK, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, BLACK, D_GREY, D_GREY, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, D_GREY, D_GREY,
     BLACK, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, D_GREY, D_GREY, D_GREY, D_GREY, D_GREY, D_GREY, D_GREY, BLACK, BLACK,
     CLR, CLR, CLR, CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, CLR, CLR, CLR, CLR, CLR,
     CLR, CLR, CLR],
    [CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR, CLR],
]

BEEPER_SURF = 0


def replace_2d(array, value, replacement):
    array = array.copy()
    for x in range(0, len(array)):
        for y in range(0, len(array[x])):
            if all(array[x][y] == value):
                array[x][y] = replacement
    return array


def readable_pixarray_to_surface(array):
    # array = array.swapaxes(0, 1)
    surf = pygame.Surface(array.shape[0:2], pygame.SRCALPHA).convert()
    pygame.surfarray.blit_array(surf, array)
    surf.set_colorkey(CLR)
    return surf


def tile_to_point(tile):
    global TILE_WIDTH
    return (tile + 0.5) * TILE_WIDTH


def point_to_tile(point):
    global TILE_WIDTH
    return (point / TILE_WIDTH) - 0.5


def wall_at(tile_x, tile_y):
    global WALLS
    for w in WALLS:
        if w[0] == tile_x and w[1] == tile_y:
            return True
    return False


def extract_vals_inside_parenthesis(string, open_parenthesis_index):
    p = []
    temp = ""
    count = open_parenthesis_index + 1
    while count < len(string):
        if string[count] == ' ':
            p.append(temp)
            temp = ""
        elif string[count] == ')':
            p.append(temp)
            return p
        else:
            temp += string[count]
        count += 1
    return p


class Robot(pygame.sprite.Sprite):
    _ids = count(0)

    def __init__(self, x, y, direction, num_of_beepers, color=-1): #color is an int, maybe add string colors eventually
        super().__init__()
        print("i am a robot")
        self.id = next(self._ids)
        self.__color = SLEEVE_COLORS[(self.id if color == -1 else color) % len(SLEEVE_COLORS)]
        self.tile_x = x
        self.tile_y = y
        self.direction = direction
        self.beepers = num_of_beepers
        self.has_moved_this_frame = True
        self.image = 0
        self.rect = 0
        self.is_alive = True
        self.prev_fps = 4
        self.zoom_fps = 20
        # self.array = numpy.where(KAREL_ON == , self.__color, KAREL_ON)
        self.array = replace_2d(KAREL_ON, GREEN, self.__color[1])
        print(self.array[0][6])

    def scale_image(self, width):
        self.image = pygame.transform.rotate(
            pygame.transform.scale(readable_pixarray_to_surface(self.array), (width, width)),
            self.direction)
        self.rect = self.image.get_rect()
        self.rect.x = tile_to_point(self.tile_x - 0.5)
        self.rect.y = tile_to_point(self.tile_y - 0.5)

    def get_sleeve_color(self):
        return self.__color[0]

    def turn_left(self):
        self.wait()
        print("turned")
        self.image = pygame.transform.rotate(self.image, 90)
        self.direction = (self.direction + 90) % 360
        self.has_moved_this_frame = True

    def move(self):
        self.wait()
        if self.front_is_clear():
            if self.direction == 0:
                self.rect.x += TILE_WIDTH
                self.tile_x += 1
            elif self.direction == 180:
                self.rect.x -= TILE_WIDTH
                self.tile_x -= 1
            elif self.direction == 270:
                self.rect.y += TILE_WIDTH
                self.tile_y += 1
            elif self.direction == 90:
                self.rect.y -= TILE_WIDTH
                self.tile_y -= 1
        else:
            print("hit a wall")
            self.turn_off()

        print("tried to move")
        self.has_moved_this_frame = True

    def pick_beeper(self):
        global BEEPERS
        self.wait()
        beepers_at_pos = [b for b in BEEPERS if b.tile_x == self.tile_x and b.tile_y == self.tile_y]
        if len(beepers_at_pos) > 0:
            beepers_at_pos[0].dec()
            self.beepers += 1
        self.has_moved_this_frame = True

    def put_beeper(self):
        global BEEPERS
        self.wait()
        print("put beeper")
        if self.beepers > 0:
            beepers_at_pos = [b for b in BEEPERS if b.tile_x == self.tile_x and b.tile_y == self.tile_y]
            if len(beepers_at_pos) > 0:
                beepers_at_pos[0].inc()
                self.beepers -= 1
            else:
                BEEPERS.add(Beeper(self.tile_x, self.tile_y, 1))
        self.has_moved_this_frame = True

    def wait(self):
        global FPS
        if self.is_alive:  # prevents commands from being run before the loop checks for dead robots, putting it in an infinite loop
            while self.has_moved_this_frame:
                print("waiting for frame")
                time.sleep(1.0 / FPS)

    def front_is_clear(self):
        if self.direction == 0:
            return not wall_at(self.tile_x + 0.5, self.tile_y)
        elif self.direction == 180:
            return not wall_at(self.tile_x - 0.5, self.tile_y)
        elif self.direction == 270:
            return not wall_at(self.tile_x, self.tile_y + 0.5)
        elif self.direction == 90:
            return not wall_at(self.tile_x, self.tile_y - 0.5)
        else:
            return False

    def turn_off(self):
        global TILE_WIDTH
        self.wait()
        self.is_alive = False
        self.array = replace_2d(self.array, YELLOW, L_GREY)
        self.scale_image(TILE_WIDTH)
        print("am dead")

    def draw(self, screen):
        screen.blit(self.image, (self.rect.x, self.rect.y))

    # https://medium.com/@mgarod/dynamically-add-a-method-to-a-class-in-python-c49204b85bd6
    def add_method(self):
        def decorator(func):
            @wraps(func)
            def wrapper(self, *args, **kwargs):
                return func(self, *args, **kwargs)

            setattr(self, func.__name__, wrapper)
            # Note we are not binding func, but wrapper which accepts self but does exactly the same as func
            return func  # returning func means func can still be used normally

        return decorator

    def set_zoom_fps(self, fps):
        self.zoom_fps = fps

    def zoom(self, on):  # TODO: zooming is quirky
        self.wait()
        global FPS
        self.has_moved_this_frame = True
        if on:
            self.prev_fps = FPS
            FPS = self.zoom_fps
            print("ZOOM")
        else:
            FPS = self.prev_fps
            print("no longer zooming")


class Beeper(pygame.sprite.Sprite):
    def __init__(self, x, y, num):
        super().__init__()
        global BEEPER_SURF
        self.tile_x = x
        self.tile_y = y
        self.num = num
        self.image = 0
        self.rect = 0
        self.update_image()

    def inc(self):
        self.num += 1
        self.update_image()

    def dec(self):
        global BEEPERS
        self.num -= 1
        if self.num < 1:
            BEEPERS.remove(self)
            self.kill()
        else:
            self.update_image()

    def update_image(self):
        global BEEPER_SURF
        self.image = BEEPER_SURF.copy()
        self.image.blit(
            pygame.font.Font('freesansbold.ttf', 10 if len(str(self.num)) == 1 else 8).render(str(self.num), True,
                                                                                              BLACK),
            ((0.475 if len(str(self.num)) == 1 else 0.45) * TILE_WIDTH, 0.425 * TILE_WIDTH))
        self.rect = self.image.get_rect()
        self.rect.x = tile_to_point(self.tile_x - 0.5)
        self.rect.y = tile_to_point(self.tile_y - 0.5)

    def draw(self, screen):
        screen.blit(self.image, (self.rect.x, self.rect.y))


class World(object):
    # maybe add window size attributes
    def __init__(self, width, height, fps=4, beeper_pos=[], wall_pos=[]):
        global FPS, BEEPERS
        self.IDEAL_HEIGHT = 700
        self.IDEAL_WIDTH = 900
        FPS = fps

        self.width = width
        self.height = height
        self.beeper_pos = beeper_pos
        self.wall_pos = wall_pos

        BEEPERS = pygame.sprite.Group()
        self.robots = []
        self.screen = None
        self.background = None
        self.running = False
        self.clock = None
        # self.interval = 10

        self.thread = threading.Thread(target=self.__run, args=())
        self.thread.daemon = True  # Daemonize thread

    @classmethod  # constructor for loading a file
    def from_file(cls, filename):
        file = open(filename)
        width = 0
        height = 0
        fps = 4
        beepers = []
        walls = []

        for l in file:
            if l.startswith("legacy: "):
                print("legacy mode")
            elif l.startswith("size: "):
                for i in range(6, len(l)):
                    if l[i] == '(':
                        p = extract_vals_inside_parenthesis(l, i)
                        if len(p) == 2:
                            width = int(p[0])
                            height = int(p[1])
            elif l.startswith("fps: ") and len(l) > 5:
                fps = int(l[5])
            elif l.startswith("beepers: "):
                for i in range(9, len(l)):
                    if l[i] == '(':
                        p = extract_vals_inside_parenthesis(l, i)
                        if len(p) == 3:
                            beepers.append((int(p[0]), int(p[1]), int(p[2])))
            elif l.startswith("walls: "):
                for i in range(7, len(l)):
                    if l[i] == '(':
                        p = extract_vals_inside_parenthesis(l, i)
                        if len(p) == 4:
                            walls.append((0 if p[0] == 'h' else 1, float(p[1]), float(p[2]), float(p[3])))
        file.close()
        return cls(width, height, fps, beepers, walls)

    def add_robots(self, *robots):
        if self.thread.is_alive():
            raise Exception("Cannot add robots while the world is running")
        else:
            for r in robots:
                self.robots.append(r)
            print("added robot")

    def set_fps(self, fps):
        global FPS
        FPS = fps
        print("changed fps")

    def start(self):
        self.thread.start()

    def __run(self):

        print("Initializing...")
        global SCREEN_WIDTH, SCREEN_HEIGHT, TILE_WIDTH, FPS, BEEPERS, BEEPER_SURF, WALLS
        pygame.init()
        TILE_WIDTH = int(min(self.IDEAL_WIDTH / (self.width + 1), self.IDEAL_HEIGHT / (self.height + 1)))
        if TILE_WIDTH % 2 == 1:
            TILE_WIDTH -= 1
        SCREEN_WIDTH = TILE_WIDTH * (self.width + 1)
        SCREEN_HEIGHT = TILE_WIDTH * (self.height + 1)
        print("screen height is " + str(SCREEN_HEIGHT))
        self.screen = pygame.display.set_mode([SCREEN_WIDTH, SCREEN_HEIGHT])  # add scaling

        BEEPER_SURF = pygame.transform.scale(readable_pixarray_to_surface(numpy.array(BEEPER)),
                                             (TILE_WIDTH, TILE_WIDTH))

        for r in self.robots:
            print("scaling robot")
            r.scale_image(TILE_WIDTH)

        for p in self.beeper_pos:
            BEEPERS.add(Beeper(p[0], p[1], p[2]))

        print("generating background")
        self.background = pygame.Surface([SCREEN_WIDTH, SCREEN_HEIGHT])
        self.background.fill(WHITE)
        for x in range(0, self.width + 1):
            pygame.draw.line(self.background, BLACK, ((x + 0.5) * TILE_WIDTH, 0.5 * TILE_WIDTH),
                             ((x + 0.5) * TILE_WIDTH, (self.height + 0.5) * TILE_WIDTH))
            self.background.blit(pygame.font.Font('freesansbold.ttf', 10).render(str(x), True, BLACK),
                                 ((x + 0.5) * TILE_WIDTH, 0.25 * TILE_WIDTH))
        for y in range(0, self.height + 1):
            pygame.draw.line(self.background, BLACK, (0.5 * TILE_WIDTH, (y + 0.5) * TILE_WIDTH),
                             ((self.width + 0.5) * TILE_WIDTH, (y + 0.5) * TILE_WIDTH))
            self.background.blit(pygame.font.Font('freesansbold.ttf', 10).render(str(y), True, BLACK),
                                 (0.25 * TILE_WIDTH, (y + 0.5) * TILE_WIDTH))

        for w in self.wall_pos:
            pygame.draw.line(self.background, RED,
                             (tile_to_point(w[1]), tile_to_point(w[2])),
                             (tile_to_point(w[3] if w[0] == 0 else w[1]), tile_to_point(w[3] if w[0] == 1 else w[2])),
                             3)
            for i in range(0, int(abs(w[3] - w[1 if w[0] == 0 else 2]))):
                WALLS.append((w[1] + (i + 0.5 if w[0] == 0 else 0), w[2] + (
                    i + 0.5 if w[0] == 1 else 0)))  # TODO: doesnt work if you input walls backwards i think
        print(WALLS)
        pygame.display.set_caption("Karel J Robot")

        self.clock = pygame.time.Clock()
        self.running = True

        print("Done!")
        while sum(r.is_alive for r in self.robots) > 0:  # change later
            pygame.event.get()  # required or else the window thinks it's not responding
            self.screen.blit(self.background, (0, 0))
            BEEPERS.draw(self.screen)

            for r in self.robots:
                r.draw(self.screen)
                r.has_moved_this_frame = False

            self.clock.tick(FPS)
            pygame.display.flip()
        print("about to quit")
        pygame.quit()
