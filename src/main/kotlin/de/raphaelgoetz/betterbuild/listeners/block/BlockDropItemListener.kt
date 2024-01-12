package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent

class BlockDropItemListener : Listener {

    @EventHandler
    fun onBlockDropItemEvent(blockDropItemEvent: BlockDropItemEvent) {
        blockDropItemEvent.isCancelled = true
    }
}