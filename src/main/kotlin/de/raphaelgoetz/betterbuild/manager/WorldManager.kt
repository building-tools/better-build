package de.raphaelgoetz.betterbuild.manager

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import de.raphaelgoetz.betterbuild.BetterBuild
import de.raphaelgoetz.betterbuild.world.BuildWorld
import de.raphaelgoetz.betterbuild.world.BuildWorldTypes
import de.raphaelgoetz.betterbuild.world.VoidGenerator
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

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

        return Bukkit.createWorld(creator)
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

    fun getWorldNames(): MutableList<String> {

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

    fun getCategories(isArchive: Boolean): Map<String, MutableList<String>> {
        val result: MutableMap<String, MutableList<String>> = HashMap()
        val worldNames: Collection<String> = betterBuild.worldManager.getWorldNames()

        if (worldNames.isEmpty()) return result
        val categoryLessWorld: MutableList<String> = ArrayList()

        for (name in worldNames) {
            if (isArchive != isArchived(name)) continue
            if (name.contains("_")) {
                val rest = name.substring(0, name.indexOf("_"))
                result.putIfAbsent(rest, mutableListOf())
                result[rest]?.add(name)
                continue
            }
            categoryLessWorld.add(name)
        }

        if (categoryLessWorld.isNotEmpty()) {
            result["none"] = categoryLessWorld
        }

        return result
    }

    fun isWorld(name: String): Boolean {
        return getWorldNames().contains(name)
    }

    fun hasPhysics(world: World): Boolean {
        return this.physics[world] ?: false
    }

    fun togglePhysics(world: World) {
        val value = this.physics[world]

        if (value == null) {
            this.physics[world] = false
            return
        }

        this.physics[world] = !value

        world.players.forEach {
            if (value) LanguageManager.sendPlayerMessage(it, "manager.world.enable")
            else LanguageManager.sendPlayerMessage(it, "manager.world.disable")
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

    fun changeWorldPermission(world: String, permission: String) {
        if (!isWorld(world)) return

        val worldFolder = File(Bukkit.getWorldContainer().path + "/" + world)
        val permissionFile = File(worldFolder.path + "/permission.json")
        val jsonObject = JsonObject()

        jsonObject.add("permission", JsonPrimitive(permission))
        FileWriter(permissionFile).use { writer ->
            writer.write(jsonObject.toString())
        }
    }

    fun toggleWorldArchive(world: String, value: Boolean) {
        if (!isWorld(world)) return

        val worldFolder = File(Bukkit.getWorldContainer().path + "/" + world)
        val archiveFile = File(worldFolder.path + "/archive.json")
        val jsonObject = JsonObject()

        jsonObject.add("isArchive", JsonPrimitive(value))
        FileWriter(archiveFile).use { writer ->
            writer.write(jsonObject.toString())
        }
    }

    fun isArchived(world: String): Boolean {
        if (!isWorld(world)) return false
        val worldFolder = File(Bukkit.getWorldContainer().path + "/" + world)
        val archiveFile = File(worldFolder.path + "/archive.json")

        if (!archiveFile.exists()) {
            toggleWorldArchive(world, false)
            return false
        }

        val json = JsonParser.parseReader(FileReader(archiveFile)).asJsonObject
        return json.get("isArchive").asBoolean
    }

    fun getWorldPermission(world: String): String {
        if (!isWorld(world)) return ""
        val worldFolder = File(Bukkit.getWorldContainer().path + "/" + world)
        val permissionFile = File(worldFolder.path + "/permission.json")

        if (!permissionFile.exists()) {
            changeWorldPermission(world, "betterbuild.enter.free")
            return "betterbuild.enter.free"
        }

        val json = JsonParser.parseReader(FileReader(permissionFile)).asJsonObject
        return json.get("permission").asString
    }

    private fun deleteFilesInsideFolder(file: File) {
        if (!file.isDirectory() && file.delete()) return
        val files = file.listFiles()
        if (files == null || file.delete()) return
        for (content in files) if (!content.delete()) deleteFilesInsideFolder(content)
        if (!file.delete()) deleteFilesInsideFolder(file)
    }
}