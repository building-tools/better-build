package de.raphaelgoetz.betterbuild.listeners.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent

class PlayerBedEnterListener : Listener {

    @EventHandler
    fun onPlayerBedEnterEvent(playerBedEnterEvent: PlayerBedEnterEvent) {
        playerBedEnterEvent.isCancelled = true
    }
}