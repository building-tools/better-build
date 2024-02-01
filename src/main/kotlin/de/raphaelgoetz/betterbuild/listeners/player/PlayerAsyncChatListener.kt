package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

data class PlayerAsyncChatListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerAsyncChatEvent(playerAsyncChatEvent: AsyncChatEvent) {

        val uuid = playerAsyncChatEvent.player.uniqueId
        if (!betterBuild.worldManager.worldCreation.contains(uuid)) return
        val buildWorld = betterBuild.worldManager.worldCreation[uuid] ?: return
        playerAsyncChatEvent.isCancelled = true

        val message = MiniMessage.miniMessage().serialize(playerAsyncChatEvent.message())
        val clearedText = message.replace(Regex("\\W"), "")
        betterBuild.worldManager.createEmptyWorld(clearedText)

        buildWorld.name = message
        Bukkit.getScheduler().runTask(betterBuild, Runnable {
            betterBuild.worldManager.generateWorld(buildWorld)
            betterBuild.languageManager.sendPlayerMessage(playerAsyncChatEvent.player, "message.world.created")
            betterBuild.worldManager.worldCreation.remove(uuid)
        })
    }
}