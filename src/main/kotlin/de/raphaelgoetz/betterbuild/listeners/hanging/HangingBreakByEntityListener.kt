package de.raphaelgoetz.betterbuild.listeners.hanging

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.hanging.HangingBreakByEntityEvent

data class HangingBreakByEntityListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onHangingBreakEvent(hangingBreakEvent: HangingBreakByEntityEvent) {
        val player = hangingBreakEvent.entity
        if (player is Player) {
            betterBuild.playerManager.cancelWhenBuilder(player, hangingBreakEvent)
        }
    }
}