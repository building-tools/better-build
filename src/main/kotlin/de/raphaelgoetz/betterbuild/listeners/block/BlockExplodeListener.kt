package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockExplodeEvent

class BlockExplodeListener : Listener {

    @EventHandler
    fun onBlockExplodeEvent(blockExplodeEvent: BlockExplodeEvent) {
        blockExplodeEvent.isCancelled = true
    }
}