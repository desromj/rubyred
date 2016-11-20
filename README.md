# rubyred
Mario/Metroid-esque platformer to test tile editors

In this project, you control a kangaroo named Ruby who needs to jump across rooftops. 
The project is built with LibGDX and uses the Tiled map editor to load in tile maps
and game objects.

As you control a kangaroo, most movement is done by a combination of short and long hops.

NOTE: There is a current known softlock that triggers if the edge of your character lands exactly on the corner edge of a platform. The game thinks you are still trying to fall when in fact you are not, and you are rendered immobile indefinitely. This will be fixed soon.

In order to run the project, do one of the following

a) Import the project into Android Studio
  
  1. Open the terminal
  
  2. Type in "gradlew desktop:run" and press enter
  
  3. The project will build and launch

CONTROLS:

Left/Right Arrow Keys = walk left or right

Up Arrow = hop while walking (faster movement)

Z = jump

Shift + Z = long jump

Down + Z = high jump

Hold X while jumping towards a ledge to grab it and climb up

R = restart the game, in case of bugs
