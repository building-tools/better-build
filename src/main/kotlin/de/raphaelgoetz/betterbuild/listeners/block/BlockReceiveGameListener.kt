package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockReceiveGameEvent

class BlockReceiveGameListener : Listener {

    @EventHandler
    fun onBlockReceiveGameEvent(blockReceiveGameEvent: BlockReceiveGameEvent) {
        blockReceiveGameEvent.isCancelled = true
    }
}