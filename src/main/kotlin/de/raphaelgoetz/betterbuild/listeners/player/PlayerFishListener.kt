package de.raphaelgoetz.betterbuild.listeners.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

class PlayerFishListener : Listener {

    @EventHandler
    fun onPlayerFishEvent(playerFishEvent: PlayerFishEvent) {
        playerFishEvent.isCancelled = true
    }
}