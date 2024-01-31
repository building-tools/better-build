![image](https://github.com/raphael-goetz/better-build/assets/52959657/82a76941-6aa3-47d2-bb12-36820ec5f367)

# BetterBuild

**ATTENTION:** This plugin manipulates the server extensively. This plugin is only made for better building experience and shouldn't be used for other things.

But what does it do?
  - Opens an inventory for world management
  - Opens an inventory to create custom banners
  - Manipulates the physics of worlds
  - Manages a users permissions to build (adds a build-mode)
  - Manipulates many Minecraft-Events for better building experience

# Specs
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
The permission to use the /build command.

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
The permission to delete a world.


```
betterbuild.world.rename
```
The permission to rename a world.

```
betterbuild.world.spawn
```
The permission to change the spawn of a world.
```
betterbuild.world.permission
```
The permission to change the permission required to enter the world.
```
betterbuild.enter.free
betterbuild.enter.low
betterbuild.enter.medium
betterbuild.enter.high
```
List of default permission that are default from the plugin used to enter a world. (Custom permissions are allowed)

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

```
/world rename <from> <to>
```
By using the command /world rename the given world will be renamed!

```
/world permission <permission>
```
By using the command /world permission the permission required to enter a world will be updated!

```
/world spawn
```
By using the command /world spawn the current spawn of the world will be updated to the location at which the command is executed.

# Events

## Blocks
- Physics are manipulated. Turing them on/off with the /physics toggle command
- Breaking, placing and interacting is only possible on entering the build-mode
- Events that destroy/manipulate blocks on default Minecraft behavior are not permitted to execute

## Entity/Player
- Events that destroy/manipulate entities on default Minecraft behavior are not permitted to execute
- Player teleportation is only possible when the player has the permission required to enter the world
- The player is the only entity on the server that is permitted to interact with the worlds
- Dropping items is only possible when sneaking
- Iron-Trapdoors will open/close on right+sneak-click
- Lightable blocks will lit/unlit on right+sneak-click
- Glazed-Terracotta will rotate by 90degree on right+sneak-click

**ATTENTION:** This plugin manipulates the server extensively. This plugin is only made for better building experience and shouldn't be used for other things.

# Menus
- World-Overview (Menu to visit/create worlds)
- Player-Overview (Menu to visit all online-players)
- Banner-Creation (Menu to create banners)