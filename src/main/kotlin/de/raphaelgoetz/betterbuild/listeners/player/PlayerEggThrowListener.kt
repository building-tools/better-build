package de.raphaelgoetz.betterbuild.listeners.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEggThrowEvent

class PlayerEggThrowListener : Listener {

    @EventHandler
    fun onPlayerEggThrowEvent(playerEggThrowEvent: PlayerEggThrowEvent) {
        playerEggThrowEvent.isHatching = false
    }
}