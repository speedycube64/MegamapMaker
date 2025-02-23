# MegamapMaker
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

To obtain the Python script and all needed assets, clone or download the `python-script` branch of this repository.

To obtain the mod's source code, clone or download the `mod` branch.

## Installing the Mod
This mod uses the [Legacy Fabric](https://legacyfabric.net/downloads.html) modloader. It has been tested to work with OptiFine, SpeedrunIGT, and Custom Map Resetter.

## Creating the Overhead Map Image
This is done with [uNmINeD](https://unmined.net/downloads/):
- Open the game save's level.dat file
- (Optional) If the map has underground areas you wish to have visible, use the slicer toolbar and the X-Ray feature.
- Save your desired selection of the map as an image
  - Make a selection
  - **Take note of the lower bounds on the X and Z coordinates (these will go in the script later)**
  - Keep the scale 1:1 (the script will do the zooming for you if desired)
- (Optional) Use an image editor like [paint.net](https://getpaint.net/) to make any desired markups to the image (wool locations, area names, etc.)
- Name the image `overworld.png` or `nether.png` depending on which dimension it is

## Creating the Map Display
This is where the Python script `MegamapMaker.py` comes into play. I have preloaded it with an example run I did of a trick on Legendary, but below is a complete list of instructions to make your own display.

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

## Limitations and Possible Improvements
First, the mod is only for 1.7.10 and 1.8.9, and I do not currently have plans to port it to other versions. Feel free to contact me if you would like this mod ported to a different version.

~~Since the mod was made with Mod Coder Pack, I won't be able to port it to versions above 1.12.2. I would have to rewrite it to use Fabric, which is not something I know how to do yet.~~

This system also doesn't show the locations of any non-player entities (most notably ender pearls). This is also a functionality I may implement in the future, though it would require drastic changes to both the mod and Python script.

Similarly, the map display is not dynamic. This means any blocks you place or break during gameplay will not show up. I do not plan to implement this, as it would take a radically different approach and reduce the customizability of using a map image.

Lastly, the implementation is not very efficient. That's why it's called "MegamapMaker" and not "MinimapMaker".

## Credits
Thanks to [jr5000](https://www.youtube.com/@jr5000pwp) for helping me learn how to use Mod Coder Pack and for suggesting that I use a custom class to make the mod. Also, thanks to [RedPenGuin111](https://www.youtube.com/@redpenguin111), whose Mapwreck 5 speedrun inspired me to make this.
