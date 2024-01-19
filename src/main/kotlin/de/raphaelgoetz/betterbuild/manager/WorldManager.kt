package de.raphaelgoetz.betterbuild.manager

import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.utils.VoidGenerator
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import java.io.File

class WorldManager(val betterBuild: BetterBuild) {

    private val physics: MutableMap<World, Boolean> = mutableMapOf()

    fun createEmptyWorld(name: String): World? {

        val worldCreator = WorldCreator(name)

        worldCreator.generateStructures(false)
        worldCreator.environment(World.Environment.NORMAL)
        worldCreator.generator(VoidGenerator())

        return worldCreator.createWorld()
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

        //TODO teleport player when loaded
    }

    fun getWorldCategories(): Map<String, String> {
        val names = mutableMapOf<String, String>()
        for (name in getWorldNames()) {

            if (!name.contains("__")) {
                names[name] = "none"
                continue
            }

            val splitterIndex = name.indexOf("__")
            val category = name.substring(0, splitterIndex)
            names[name] = category
        }

        return names
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