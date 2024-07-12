package de.raphaelgoetz.betterbuild.manager

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

import java.io.*

class LanguageManager {

    companion object {

        private val singleMessage: MutableMap<String, String> = mutableMapOf()
        private val multiMessage: MutableMap<String, List<String>> = mutableMapOf()

        fun sendPlayerMessage(player: Player, key: String) {
            player.sendMessage(getComponent(key))
        }

        fun sendPlayerMessage(player: Player, key: String, regex: String, value: String) {
            player.sendMessage(getComponent(key, regex, value))
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

        fun readConfig() {

            val inputStream = javaClass.classLoader.getResourceAsStream("language.json")
                ?: throw NullPointerException("ResourceSteam is empty!")
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

        fun getStringFromConfig(key: String): String {
            return this.singleMessage.getOrDefault(key, key)
        }

        fun getStringFromConfig(key: String, optional: String): String {
            return this.singleMessage.getOrDefault(key, optional)
        }

        fun getString(key: String, toReplace: String, value: String): String {
            val message = this.singleMessage.getOrDefault(key, key)
            return message.replace(toReplace, value)
        }

        fun getStringsFromConfig(key: String): List<String> {
            return this.multiMessage.getOrDefault(key, listOf(key))
        }

        fun getStringsFromConfig(key: String, optional: List<String>): List<String> {
            return this.multiMessage.getOrDefault(key, optional)
        }
    }
}
