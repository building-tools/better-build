package de.raphaelgoetz.betterbuild.listeners.hanging

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.hanging.HangingPlaceEvent

data class HangingPlaceListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onHangingPlaceEvent(hangingPlaceEvent: HangingPlaceEvent) {
        if (hangingPlaceEvent.player != null) betterBuild.playerManager.cancelWhenBuilder(hangingPlaceEvent.player!!, hangingPlaceEvent)
    }
}