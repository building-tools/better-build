package de.raphaelgoetz.betterbuild.manager

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

import java.io.*

class LanguageManager {

    init {
        readConfig()
    }

    private val singleMessage: MutableMap<String, String> = mutableMapOf()
    private val multiMessage: MutableMap<String, List<String>> = mutableMapOf()

    fun sendPlayerMessage(player: Player, key: String) {
        player.sendMessage(getComponent(key))
    }

    fun sendPlayersMessage(players: Collection<Player>, key: String) {
        players.forEach { player ->
            sendPlayerMessage(player, key)
        }
    }

    fun getComponent(key: String, regex: String, value: String): Component {
        val string = getStringFromConfig(key).replace(regex, value)
        return MiniMessage.miniMessage().deserialize(string)
    }

    fun getComponent(key: String): Component {
        val string = getStringFromConfig(key)
        return MiniMessage.miniMessage().deserialize(string)
    }

    fun getComponents(key: String): List<Component> {
        return getStringsFromConfig(key).stream().map { string ->
            MiniMessage.miniMessage().deserialize(string)
        }.toList()
    }

    private fun readLangaugeFromResources(path: String) {

        val inputStream = javaClass.classLoader.getResourceAsStream("langauge.json") ?: return
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line)
            line = reader.readLine()
        }

        val jsonString = stringBuilder.toString()
        val jsonFile = Gson().fromJson(jsonString, JsonObject::class.java)

        jsonFile.keySet().forEach { key ->
            val element = jsonFile[key]
            readConfigData(key, element)
        }

        writeFile("$path/language.json", jsonString)
    }

    private fun writeFile(path: String, data: String) {

        val fileWriter = FileWriter(path)
        fileWriter.write(data)
        fileWriter.flush()

    }

    private fun readLanguageFileFromJson(file: File) {
        val jsonFile = JsonParser.parseReader(FileReader(file)).asJsonObject

        jsonFile.keySet().forEach { key ->
            val element = jsonFile[key]
            readConfigData(key, element)
        }
    }

    private fun readConfigData(key: String, element: JsonElement) {

        if (element.isJsonArray) {

            val list = mutableListOf<String>()

            for (jsonElement in element.asJsonArray.asList()) {
                list.add(jsonElement.asString)
            }

            this.multiMessage[key] = list
            return
        }

        this.singleMessage[key] = element.asString
    }

    private fun readConfig() {

        val path = Bukkit.getPluginsFolder().toString() + "/BetterFlowers"
        val file = File("$path/language.json")

        if (File(path).mkdir()) throw FileNotFoundException()
        if (file.exists()) {
            readLanguageFileFromJson(file)
            return
        }

        readLangaugeFromResources(path)
    }

    private fun getStringFromConfig(key: String): String {
        return this.singleMessage.getOrDefault(key, key)
    }

    private fun getStringFromConfig(key: String, optional: String): String {
        return this.singleMessage.getOrDefault(key, optional)
    }

    private fun getStringsFromConfig(key: String): List<String> {
        return this.multiMessage.getOrDefault(key, listOf(key))
    }

    private fun getStringsFromConfig(key: String, optional: List<String>): List<String> {
        return this.multiMessage.getOrDefault(key, optional)
    }
}