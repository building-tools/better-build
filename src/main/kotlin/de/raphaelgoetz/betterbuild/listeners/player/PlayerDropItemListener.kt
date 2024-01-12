package de.raphaelgoetz.betterbuild.listeners.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class PlayerDropItemListener : Listener {

    @EventHandler
    fun onPlayerDropItemEvent(playerDropItemEvent: PlayerDropItemEvent) {
        if (playerDropItemEvent.player.isSneaking) return
        playerDropItemEvent.itemDrop.remove()
    }
}