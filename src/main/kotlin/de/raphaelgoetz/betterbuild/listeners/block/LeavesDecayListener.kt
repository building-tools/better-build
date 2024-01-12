package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.LeavesDecayEvent

class LeavesDecayListener : Listener {

    @EventHandler
    fun onLeavesDecayEvent(leavesDecayEvent: LeavesDecayEvent) {
        leavesDecayEvent.isCancelled = true
    }
}