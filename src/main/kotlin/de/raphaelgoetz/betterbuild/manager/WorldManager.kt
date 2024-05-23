package de.raphaelgoetz.betterbuild.manager

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import de.raphaelgoetz.astralis.world.existingWorlds
import org.bukkit.Bukkit
import org.bukkit.World
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*


private val physics: MutableMap<World, Boolean> = mutableMapOf()
private val worldQueue: MutableMap<String, MutableList<UUID>> = mutableMapOf()

fun removeFromQueue(world: String) {
    worldQueue.remove(world)
}

fun UUID.addToQueue(name: String) {
    val players = worldQueue[name]

    if (players == null) {
        worldQueue[name] = mutableListOf(this)
        return
    }

    players.add(this)
}

fun getWaitingPlayers(name: String): MutableList<UUID>? {
    return worldQueue[name]
}

fun getCategories(isArchive: Boolean): Map<String, MutableList<String>> {
    val result: MutableMap<String, MutableList<String>> = HashMap()
    val worldNames: Collection<String> = existingWorlds

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
    return existingWorlds.contains(name)
}

fun World.hasPhysics(): Boolean {
    return physics[this] ?: false
}

fun World.togglePhysics() {
    val value = physics[this]

    if (value == null) {
        physics[this] = false
        return
    }

    physics[this] = !value

    players.forEach {
        if (value) LanguageManager.sendPlayerMessage(it, "manager.world.enable")
        else LanguageManager.sendPlayerMessage(it, "manager.world.disable")
    }
}

private fun File.isWorldFolder(): Boolean {
    val lockFile = File(this, "session.lock")
    return lockFile.exists()
}

fun deleteWorld(name: String) {
    if (!isWorld(name)) return
    Bukkit.unloadWorld(name, false)
    val folders = Bukkit.getWorldContainer().listFiles() ?: return

    for (folder in folders) {
        if (folder.name != name) continue
        folder.deleteFilesInsideFolder()
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

private fun File.deleteFilesInsideFolder() {
    if (!this.isDirectory() && this.delete()) return
    val files = this.listFiles()
    if (files == null || this.delete()) return
    for (content in files) if (!content.delete()) content.deleteFilesInsideFolder()
    if (!this.delete()) this.deleteFilesInsideFolder()
}
