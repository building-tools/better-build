package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFadeEvent

class BlockFadeListener : Listener {

    @EventHandler
    fun onBlockFadeEvent(blockFadeEvent: BlockFadeEvent) {
        blockFadeEvent.isCancelled = true
    }
}