# Copilot Instructions

## Project
- This is a small Java Swing space-shooter project using the default package and JDK AWT/Swing APIs; there is no Maven or Gradle build.
- `Main` creates the JFrame and starts the game. `GamePanel` owns the Swing timer, game states, input handling, rendering, spawning, and collisions.
- `GameObject` is the abstract base for `Player`, `Enemy`, `Bullet`, `EnemyBullet`, `Obstacle`, and `PowerUp`.
- `GamePanel` stores the persistent high score through the JDK Preferences API.
- `ExportImage` and `ExportSeparateScreens` are standalone PNG-generation utilities. They write image files to the process working directory; the game itself draws its screens in `GamePanel`.
- Source files and game image assets are together in the `Game OOP/` directory.

## Conventions
- Keep the existing default-package structure and use only JDK APIs unless a dependency is explicitly needed.
- Preserve the existing Swing/AWT approach and keep game-state changes in `GamePanel` and entity behavior in the relevant `GameObject` subclasses.
- Keep changes focused and avoid editing generated `.class` files or generated PNGs unless requested.

## Build and Run
Run commands from the `Game OOP/` source directory with a JDK installed:

```sh
javac *.java
java Main
```

To regenerate the character sprite sheet or sample screens, run `java ExportImage` or `java ExportSeparateScreens` from that directory. These commands overwrite their corresponding PNG output files.
