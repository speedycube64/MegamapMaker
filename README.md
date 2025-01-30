# MegamapTracker
A mod/script combo for making post-production minimaps

## Demonstration
[Legendary portal route practice run with simultaneous overworld and nether maps](https://www.youtube.com/watch?v=ENjwQe5B1NM)

## How it Works
As you play, the mod saves the player's coordinates, angle, and dimension on every tick to a file in the saves folder called `map_log.txt`. This is buffered so that writing the data only happens once every 5 seconds of IGT or whenever the game is paused. The format looks like this:

```
<time> <x> <z> <yaw> <dim>
<time> <x> <z> <yaw> <dim>
<time> <x> <z> <yaw> <dim>
...
```
The script then reads this file and makes the video. The player head is kept in the center of the frame, the arrow is rotated around it, and the image of the map itself is moved around underneath it.

## Installing the mod
The mod can be found in the Releases tab. Currently, I only have it made for 1.7.10.

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
- **Take note of the lower bounds on the X and Z coordinates (these will go in the script later)**
- (Optional) Use an image editor like [paint.net](https://getpaint.net/) to make any desired markups to the image (wool locations, area names, etc.)
- Put this image in the `maps` folder where the script is located
- Name it `overworld.png` or `nether.png` depending on which dimension it is

