package de.raphaelgoetz.betterbuild.manager

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.world.BuildWorld
import de.raphaelgoetz.betterbuild.world.BuildWorldTypes
import de.raphaelgoetz.betterbuild.world.VoidGenerator
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.entity.Player
import java.io.File
import java.util.UUID

class WorldManager(val betterBuild: BetterBuild) {

    private val physics: MutableMap<World, Boolean> = mutableMapOf()
    private val worldQueue: MutableMap<String, MutableList<UUID>> = mutableMapOf()
    val worldCreation: MutableMap<UUID, BuildWorld> = mutableMapOf()

    fun createEmptyWorld(name: String): World? {

        val worldCreator = WorldCreator(name)

        worldCreator.generateStructures(false)
        worldCreator.environment(World.Environment.NORMAL)
        worldCreator.generator(VoidGenerator())

        return worldCreator.createWorld()
    }

    fun generateWorld(buildWorld: BuildWorld): World? {
        val creator = WorldCreator(buildWorld.name)

        creator.environment(buildWorld.environment)
        creator.generateStructures(buildWorld.generateStructures)

        when (buildWorld.types) {
            BuildWorldTypes.VOID -> creator.generator(VoidGenerator())
            BuildWorldTypes.FLAT -> creator.type(WorldType.FLAT)
            BuildWorldTypes.NORMAL -> creator.type(WorldType.NORMAL)
        }

        return Bukkit.createWorld(creator);
    }

    fun removeFromQueue(name: String) {
        worldQueue.remove(name)
    }

    fun addPlayerToQueue(name: String, uuid: UUID) {
        val players = worldQueue[name]

        if (players == null) {
            worldQueue[name] = mutableListOf(uuid)
            return
        }

        players.add(uuid)
    }

    fun getWaitingPlayers(name: String): MutableList<UUID>? {
        return worldQueue[name]
    }

    fun getWorldNames(): List<String> {

        val worlds = mutableListOf<String>()
        val worldContainer = Bukkit.getWorldContainer()
        val folders = worldContainer.listFiles() ?: return worlds

        for (file in folders) {

            if (!isWorldFolder(file)) continue
            if (worlds.contains(file.name)) continue
            worlds.add(file.name)

        }

        return worlds
    }

    fun isWorld(name: String): Boolean {
        return getWorldNames().contains(name)
    }

    fun hasPhysics(world: World): Boolean {
        return this.physics[world] ?: false;
    }

    fun togglePhysics(world: World) {
        val value = this.physics[world]

        if (value == null) {
            this.physics[world] = false
            return
        }

        this.physics[world] = !value

        world.players.forEach {
           if (value) betterBuild.languageManager.sendPlayerMessage(it, "manager.world.enable")
            else betterBuild.languageManager.sendPlayerMessage(it, "manager.world.disable")
        }
    }

    private fun isWorldFolder(file: File): Boolean {
        val lockFile = File(file, "session.lock")
        return lockFile.exists()
    }

    fun deleteWorld(name: String) {
        if (!isWorld(name)) return
        Bukkit.unloadWorld(name, false)
        val folders = Bukkit.getWorldContainer().listFiles() ?: return

        for (folder in folders) {
            if (folder.name != name) continue
            deleteFilesInsideFolder(folder)
        }
    }

    fun teleportPlayer(worldName: String, player: Player) {

        val world = Bukkit.getWorld(worldName)

        if (world != null) {
            player.teleport(world.spawnLocation)
            return
        }
    }

    private fun deleteFiles(file: File) {

        if (!file.isDirectory) {
            file.delete()
            return
        }

        val directory = file.listFiles()
        for (currentFile in directory!!) deleteFiles(currentFile)
    }

    private fun deleteFilesInsideFolder(file: File) {
        if (!file.isDirectory() && file.delete()) return;
        val files = file.listFiles();
        if (files == null || file.delete()) return;
        for (content in files) if (!content.delete()) deleteFilesInsideFolder(content);
        if (!file.delete()) deleteFilesInsideFolder(file);
    }
}
