# MegamapTracker
A mod/script combo for making post-production minimaps

## Demonstration
[Legendary portal route practice run with simultaneous overworld and nether maps](https://www.youtube.com/watch?v=ENjwQe5B1NM)

## Features
- Uses frame interpolation to produce 60+ FPS videos even though Minecraft runs at 20 ticks per second
- Can show overworld and nether position simultaneously by multiplying/dividing by 8
- Synced to real time (meaning the map display is paused when the game is paused)

## How it Works
As you play, the mod saves the player's coordinates, angle, and dimension on every tick to a file in the saves folder called `map_log.txt`. This is buffered so that writing the data only happens once every 5 seconds of IGT or whenever the game is paused. The format looks like this:

```
<time> <x> <z> <yaw> <dim>
<time> <x> <z> <yaw> <dim>
<time> <x> <z> <yaw> <dim>
...
```
The script then reads this file and makes the video. The player head is kept in the center of the frame, the arrow is rotated around it, and the image of the map itself is moved around underneath it.

## Getting the Files
To obtain the mod, download it from the Releases tab.

To obtain the Python script and all needed assets, simply clone or download this repository.
Note that this also includes a source code file I used to make the mod (`MegamapTracker.java`), but you don't need this file to run it.

## Installing the Mod
This is a traditional JAR mod that doesn't use Forge, Fabric, or any other modloader.

The best way to install this kind of mod is by using [MultiMC](https://multimc.org/):
- Right click on the instance
- Select "Edit Instance"
- Go to the "Version" tab
- Select "Add to Minecraft .jar"
- Select the MegamapTracker jar file

**Note: If you wish to also play with Legacy Fabric mods, make a brand new instance and install ALL desired mods before launching the instance for the first time.**

## Creating the Overhead Map Image
This is done with [uNmINeD](https://unmined.net/downloads/)
- Open the game save's level.dat file
- (Optional) If the map has underground areas you wish to have visible, use the slicer toolbar and the X-Ray feature.
- Save your desired selection of the map as an image
  - Make a selection
  - **Take note of the lower bounds on the X and Z coordinates (these will go in the script later)**
  - Keep the scale 1:1 (the script will do the zooming for you if desired)
- (Optional) Use an image editor like [paint.net](https://getpaint.net/) to make any desired markups to the image (wool locations, area names, etc.)
- Name the image `overworld.png` or `nether.png` depending on which dimension it is

## Creating the Map Display

This is where the Python script `MegamapMaker.py` comes into play. I have preloaded it with an example run I did of a trick on Legendary, but below is a list of instructions to make your own display.

After getting your run:

- Find the `map_log.txt` file in your game save folder
- Copy it into the same folder as the Python script
- Edit it if you only want part of your run to have a map display
- Edit the Python script
  - Put in the lower X and Z coordinates from uNmINeD
  - Change the other user parameters if necessary
  - Scroll to the bottom and comment/uncomment some lines depending on which dimensions you want generated
- Put your `overworld.png` and/or `nether.png` map images in the `maps` folder
- Get an 8x8 image of your player head (you can get this from your skin) and put it in the `assets` folder as `player_head.png`
- Run `MegamapMaker.py`

Your minimaps will now be in the `videos` folder, and you can edit them over your run.

