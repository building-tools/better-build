package de.raphaelgoetz.betterbuild.manager

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import de.raphaelgoetz.betterbuild.utils.VoidGenerator

import java.io.File

class WorldManager(

    private val physics: Map<World, Boolean> = mutableMapOf()

) {

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