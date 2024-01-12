package de.raphaelgoetz.betterbuild.listeners.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener : Listener {

    @EventHandler
    fun onPlayerDeathEvent(playerDeathEvent: PlayerDeathEvent) {
        playerDeathEvent.keepInventory = true
        playerDeathEvent.isCancelled = true
    }
}