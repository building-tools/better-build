package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPistonExtendEvent

class BlockPistonExtendListener : Listener {

    @EventHandler
    fun onBlockPistonExtendEvent(blockPistonExtendEvent: BlockPistonExtendEvent) {
        blockPistonExtendEvent.isCancelled = true
    }
}