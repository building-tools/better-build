package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPistonEvent

class BlockPistonListener : Listener {

    @EventHandler
    fun onBlockPistonEvent(blockPistonEvent: BlockPistonEvent) {
        blockPistonEvent.isCancelled = true
    }
}