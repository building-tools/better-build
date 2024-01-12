package de.raphaelgoetz.betterbuild.manager

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import de.raphaelgoetz.betterbuild.utils.VoidGenerator
import java.io.File

class WorldManager {

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

            val lockFile = File(file, "session.lock")
            if (!lockFile.exists()) continue
            if (worlds.contains(file.name)) continue
            worlds.add(file.name)

        }

        return worlds
    }

}