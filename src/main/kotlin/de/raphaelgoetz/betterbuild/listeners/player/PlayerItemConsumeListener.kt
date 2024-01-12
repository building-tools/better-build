package de.raphaelgoetz.betterbuild.listeners.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent

class PlayerItemConsumeListener : Listener {

    @EventHandler
    fun onPlayerItemConsumeEvent(playerItemConsumeEvent: PlayerItemConsumeEvent) {
        playerItemConsumeEvent.isCancelled = true
    }
}