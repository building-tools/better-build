![image](https://github.com/raphael-goetz/better-build/assets/52959657/82a76941-6aa3-47d2-bb12-36820ec5f367)

# BetterBuild

**ATTENTION:** This plugin manipulates the server extensively. This plugin is only made for better building experience and shouldn't be used for other things.

But what does it do?
  - Opens an inventory for world management
  - Opens an inventory to create custom banners
  - Manipulates the physics of worlds
  - Manages a users permissions to build (adds a build-mode)
  - Manipulates many Minecraft-Events for better building experience

# Specs:
## Supported Minecraft-Version: 1.20.4
## Current Java-Version: 17
## Developed for Paper

# Explanation

Explanatory video coming soon...

# Permissions

```
betterbuild.player.back
```
The permission to use the /back command, to get to your last known location.

```
betterbuild.player.mode
```
The permission to use the /build and /clip command.

```
betterbuild.player.speed
```
The permission to update your fly- and walk speed.

```
betterbuild.world.physics
```
The permission to toggle the physics of a world.

```
betterbuild.world.create
```
The permission to create a new world.

```
betterbuild.world.delete
```
The permission to delete an existing world.

# Commands

```
/back
```
By using the command /back, the user will be teleported to his last known location.
```
/build <player>
```
By using the command /build, the given user will be added to build-mode. If no name is present the command-executer will be the user.

```
/clip <player>
```
By using the command /clip, the given user will be added to clip-mode. If no name is present the command-executer will be the user.

```
/fly <0 - 10>
```
By using the command /fly the flying-speed will be updated.

```
/walk <0 - 10>
```
By using the command /walk the walking-speed will be updated.

```
/physics
```
By using the command /physics the physics of the world where the command has been executed will be toggled..

```
/world delete <name>
```
By using the command /world delete the world by the given name will be deleted.

```
/world create <name> <category>
```
By using the command /world create a new world by the given name will be created. When a category is given, it will be automatically sorted by the given category.

# Menus
- World-Overview (Menu to visit/create worlds)
- Player-Overview (Menu to visit all online-players)
- Banner-Creation (Menu to create banners)