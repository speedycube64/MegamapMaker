from PIL import Image, ImageEnhance
from moviepy import ImageSequenceClip
import numpy as np
import os

###### USER PARAMETERS ######

# video settings
out_width, out_height = 360, 360
fps = 60
out_codec = "libx264"

# amount to zoom in each map view
# example: 2 is 2x zoomed in
overworld_zoom = 1
nether_zoom = 1

# lower x and z coordinates of your map from unmined
overworld_start = (-1000, -1500)
nether_start = (-1410, -416)

###### END OF USER PARAMETERS ######

# import the two maps
overworld_map = Image.open("maps/overworld.png")
nether_map = Image.open("maps/nether.png")

# import player head
head = Image.open("assets/player_head.png")
# resize it for visibility
head = head.resize((24, 24), Image.Resampling.NEAREST)

# import arrow to show facing angle
arrow = Image.open("assets/arrow.png")

# import crosshair image
crosshair = Image.open("assets/crosshair.png")

# makes the minimap output file
# parameters:
# map_image: the PIL Image of the map
# map_dim: -1 if nether, 0 if overworld
# out_filename: filename of the output video
# zoom: scaling factor of the map_image
def generate_minimap(map_image, map_dim, out_filename, zoom, start):

    # resize if zooming map in or out
    if zoom != 1:
        scaled_width = int(zoom * map_image.width)
        scaled_height = int(zoom * map_image.height)

        map_image = map_image.resize((scaled_width, scaled_height), Image.Resampling.NEAREST)

    # get the starting x and z coords from the tuple
    start_x, start_z = start

    # open the file
    map_log = open("map_log.txt", "r")

    # read the first two lines so calculations can start
    prev_time, prev_x, prev_z, prev_yaw, prev_dim = get_data_from_line(map_log)
    time, x, z, yaw, dim = get_data_from_line(map_log)

    # make a blank background for the map images to be placed on
    bg = Image.new('RGB', (out_width, out_height), (0, 0, 0))

    # list of frames as numpy arrays
    frames = []

    # number of frames created
    frame_num = 0

    while True:

        # convert the received system time to a frame
        # example: 165 ms -> 10 frames at 60 fps
        ms_per_frame = 1000 / fps
        prev_time_frames = round(prev_time / ms_per_frame)
        time_frames = round(time / ms_per_frame)

        # calculate the amount of frames between each log entry
        frame_diff = time_frames - prev_time_frames

        # calculate the changes in position and angle between each log entry
        x_diff = x - prev_x
        z_diff = z - prev_z

        # do some funky modulo stuff to account for wrap-around at +-360 degrees
        yaw_diff = ((yaw - prev_yaw) + 180) % 360 - 180

        for i in range(0, frame_diff):

            if dim == prev_dim:
                # interpolate ("x_int") an intermediate position/angle 
                # for frames not landing on a log entry
                x_int = prev_x + (x_diff / frame_diff) * i
                z_int = prev_z + (z_diff / frame_diff) * i
                yaw_int = prev_yaw + (yaw_diff / frame_diff) * i
                dim_int = dim
            
            # don't interpolate on dimension switches
            # keep everything the way it was on the previous log entry
            else:
                x_int, z_int, yaw_int, dim_int = prev_x, prev_z, prev_yaw, prev_dim

            # if the player is not in the dimension being processed,
            # multiply or divide coords to adjust
            if dim_int != map_dim:
                # if player is in the nether, multiply by 8 for overworld coords
                if dim_int == -1:
                    x_int = x_int * 8
                    z_int = z_int * 8
                # if player is in the overworld, divide by 8 for nether coords
                else:
                    x_int = x_int / 8
                    z_int = z_int / 8

            # calculate the amount the image needs to be shifted by to have the player position at the center
            # (don't question the magic formula)
            x_shift = round(zoom * start_x + out_width/2 - zoom * x_int)
            z_shift = round(zoom * start_z + out_height/2 - zoom * z_int)

            # add the map image with the shifting to the background
            frame = bg
            frame.paste(map_image, (x_shift, z_shift))

            # add the player head and arrow if in the dimension being processed
            if dim_int == map_dim:
                # add the head
                head_x = out_width//2 - head.width//2
                head_z = out_height//2 - head.height//2
                frame.paste(head, (head_x, head_z))
                # rotate the arrow
                # need to convert Minecraft's CW being positive to PIL's CCW being positive
                arrow_r = arrow.rotate(-yaw_int)
                # paste arrow onto frame with transparency
                arrow_x = out_width//2 - arrow_r.width//2
                arrow_z = out_height//2 - arrow_r.height//2
                arrow_r = Image.alpha_composite(Image.new("RGBA", arrow_r.size), arrow_r.convert('RGBA'))
                frame.paste(arrow_r, (arrow_x, arrow_z), arrow_r)

            # OPTIONAL:
            # if the player is not in the dimension being processed,
            # decrease the image saturation
            # if dim_int != map_dim:
            #     frame = ImageEnhance.Color(frame).enhance(0.5)

            # OPTIONAL:
            # if the player is not in the dimension being processed,
            # add a crosshair where the player head would be
            if dim_int != map_dim:
                cross = crosshair
                cross_x = out_width//2 - cross.width//2
                cross_z = out_height//2 - cross.height//2
                cross = Image.alpha_composite(Image.new("RGBA", cross.size), cross.convert('RGBA'))
                frame.paste(cross, (cross_x, cross_z), cross)

            # convert a frame to a numpy array and save it to the list
            frames.append(np.array(frame))

            # print that it's done making the frame
            print(f"made frame {frame_num}", end='\r', flush=True)
            frame_num = frame_num + 1

        # log data now becomes previous log data
        prev_time, prev_x, prev_z, prev_yaw, prev_dim = time, x, z, yaw, dim

        # read the next line and if it's eof, quit the loop
        try:
            time, x, z, yaw, dim = get_data_from_line(map_log)
        except EOFError:
            break

    map_log.close()

    # make an output directory if it doesn't exist
    if not os.path.exists("videos"):
        os.makedirs("videos")

    # make the frames into a video
    clip = ImageSequenceClip(frames, fps=fps)
    clip.write_videofile(f'videos/{out_filename}', codec=out_codec)

# ...gets data from one line of a file
def get_data_from_line(file):

    # read the line and split it into strings
    line = file.readline()
    if not line:
        raise EOFError()
    time, x, z, yaw, dim = line.split()

    # convert the strings into their appropriate datatypes
    time = int(time)
    x, z, yaw = float(x), float(z), float(yaw)
    dim = int(dim)

    return time, x, z, yaw, dim

# actually make the videos
print("Making overworld minimap:")
generate_minimap(overworld_map, 0, "overworld.mp4", overworld_zoom, overworld_start)
# print("Making nether minimap:")
# generate_minimap(nether_map, -1, "nether.mp4", nether_zoom, nether_start)
# print("Done")
# input("Press enter to exit")