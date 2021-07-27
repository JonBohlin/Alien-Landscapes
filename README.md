# Alien-Landscapes
The alien landscape generator creates Brownian-motion type random landscapes. It includes a simple 3D-engine (non-moving camera, no clipping) and landscape generator together with a simple lighting-scheme.

![Screenshot_Alien-Landscape-Generator](https://user-images.githubusercontent.com/20295285/126984559-c5995958-08e8-429c-ac51-39a0acbe131c.png)

Zoom in/out using the mouse wheel. Generate a new landscape by holding in shift and rolling the mouse wheel. Control and mouse wheel will speed-up/slow-down landscape rotation (too fast and it will crash).

It is written in Java version 11 but should run in any Java version >= 8. Compile as follows:

javac Engine.java Landscape.java Lightning.java MultiThreadedBrownianLandscapes.java

It can be run by writting:

java MultiThreadedBrownianLandscapes

and a JAR file can be made as follows:

jar cfe AlienLandscapes.jar MultiThreadedBrownianLandscapes ./*.class

It has been tested on Linux (MINT), Windows and macos (M1).
