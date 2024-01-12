package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBurnEvent

class BlockBurnListener : Listener {

    @EventHandler
    fun onBlockBurnEvent(blockBurnEvent: BlockBurnEvent) {
        blockBurnEvent.isCancelled = true
    }
}