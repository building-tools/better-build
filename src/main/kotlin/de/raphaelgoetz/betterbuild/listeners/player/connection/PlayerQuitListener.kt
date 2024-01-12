package de.raphaelgoetz.betterbuild.listeners.player.connection

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener : Listener {

    @EventHandler
    private fun onPlayerQuitEvent(playerQuitEvent: PlayerQuitEvent) {
        val player = playerQuitEvent.player
        playerQuitEvent.quitMessage(MiniMessage.miniMessage().deserialize("<gradient:#EB3349:#F45C43>- " + player.name))
    }
}