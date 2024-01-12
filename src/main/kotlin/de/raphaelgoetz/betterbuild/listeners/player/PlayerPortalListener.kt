package de.raphaelgoetz.betterbuild.listeners.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent

class PlayerPortalListener : Listener {

    @EventHandler
    fun onPlayerPortalEvent(playerPortalEvent: PlayerPortalEvent) {
        playerPortalEvent.isCancelled = true
    }
}