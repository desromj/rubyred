# rubyred
Mario/Metroid-esque platformer to test tile editors

In this project, you control a kangaroo named Ruby who needs to jump across rooftops. 
The project is built with LibGDX and uses the Tiled map editor to load in tile maps
and game objects.

As you control a kangaroo, all movement is done by a combination of short and long hops.

NOTE: There is a current known framerate issue. A LibGDX Scene is used to handle actors,
but terminates the drawing batch and uses a ShapeRenderer for every object without assets. 
This is a costly operation, but it will be removed when objects use actual sprites instead of
placeholder shapes for the graphics.

In order to run the project, do the following

1. Import the project into Android Studio
2. Open the terminal
3. Type in "gradlew desktop:run" and press enter
4. The project will build and launch

Controls:

- Left/Right Arrow keys
  - small hop left or right
- Z
  - normal jump
  - jump straight up in the air
  - hold while jumping towards a ledge to grab the edge and pull yourself up
- Hold SHIFT+Z
  - spring jump
  - jump straight up in the air with double height
- Hold CTRL+Z
  - crouch jump
  - a very long, horizontal jump in the direction you are currently facing
