package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockGrowEvent

class BlockGrowListener : Listener {

    @EventHandler
    fun onBlockGrowEvent(blockGrowEvent: BlockGrowEvent) {
        blockGrowEvent.isCancelled = true
    }
}