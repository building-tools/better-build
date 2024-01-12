package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockSpreadEvent

class BlockSpreadListener : Listener {

    @EventHandler
    fun onBlockSpreadEvent(blockSpreadEvent: BlockSpreadEvent) {
        blockSpreadEvent.isCancelled = true
    }
}