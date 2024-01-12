package de.raphaelgoetz.betterbuild.listeners.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.FluidLevelChangeEvent

class FluidLevelChangeListener : Listener {

    @EventHandler
    fun onFluidLevelChangeEvent(fluidLevelChangeEvent: FluidLevelChangeEvent) {
        fluidLevelChangeEvent.isCancelled = true
    }
}