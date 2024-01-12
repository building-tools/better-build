package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPistonRetractEvent

class BlockPistonRetractListener : Listener {

    @EventHandler
    fun onBlockPistonRetractEvent(blockPistonRetractEvent: BlockPistonRetractEvent) {
        blockPistonRetractEvent.isCancelled = true
    }
}