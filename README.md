# EverPhase Client

A MMORPG based on LWJGL using Java.

This project is by far not complete, thus expect some bugs and crashes while playing this game.
Feel free to test the game and report or help us to fix the problems.

License: BSD 3-Clause (see LICENSE.txt)

## To-Do:

✓ -> Done

◔ -> WIP

✗ -> Not started yet

|Objective|Developer(s)|State|
|---------|------------|-----|
|Render Engine|WerWolv|◔|
|Fonts|WerWolv|✓|
|CMake Build|SmallJoker|◔|
|Inventory System|WerWolv|◔|
|Crafting System|Mrasam|◔|
|Achievements|WerWolv|◔|
|Random Dungeons (Rogue Like)|Mrasam|◔|
|Magic (Teleports, etc.)|-|✗|
|Questing System|WerWolv|✓|
|Skilling System|WerWolv|✓|
|Skilltree|-|✗|
|Tinkering / Invention|-|✗|
|Shadows on Entities|WerWolv|◔|
|Fixing Audio issues when unpluging default device|WerWolv|◔|
|Raytrace interaction with entities|-|◔|
|AABBs|WerWolv|◔|
|Main Menu|WerWolv|✗|
|Login system|WerWolv|✗|
|Server|WerWolv|✗|
|Minimap with the new FBOs|WerWolv|✗|

## Build Requirements

Any Java IDE or only the JDK, in case you do not want to install yet another IDE:

- [Eclipse for Java developers](http://www.eclipse.org/downloads/packages/release/Oxygen/M4)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/?fromMenu#chooseYourEdition), used by the developers
- [NetBeans](http://www.oracle.com/technetwork/java/javase/downloads/jdk-netbeans-jsp-142931.html)
- [Java SDK / JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html), Warning: tricky to build projects!

Please note that the CMake support is not done yet. You will need to bundle the jar libraries manually.

## Run Requirements

The Java Runtime Environment version 1.8 or later has to be installed. Otherwise EverPhase will throw an "UnsupportedClassVersionError: Unsupported major.minor version 52.0".
