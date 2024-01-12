package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent

class BlockDispenseListener : Listener {

    @EventHandler
    fun onBlockDispenseEvent(blockDispenseEvent: BlockDispenseEvent) {
        blockDispenseEvent.isCancelled = true
    }
}
