package de.raphaelgoetz.betterbuild.listeners.player

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class PlayerDamageListener : Listener {

    @EventHandler
    fun onPlayerDamageEvent(playerDamageEvent: EntityDamageEvent) {
        val entity = playerDamageEvent.entity
        if (entity is Player) playerDamageEvent.isCancelled = true
    }
}