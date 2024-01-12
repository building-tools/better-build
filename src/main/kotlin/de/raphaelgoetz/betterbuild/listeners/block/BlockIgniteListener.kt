package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent

class BlockIgniteListener : Listener {

    @EventHandler
    fun onBlockIgniteEvent(blockIgniteEvent: BlockIgniteEvent) {
        blockIgniteEvent.isCancelled = true
    }
}