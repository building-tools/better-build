package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.entity.Player
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingPlaceEvent

fun registerHangingEvents(betterBuild: BetterBuild) {

    listen<HangingBreakByEntityEvent> { hangingBreakEvent ->

        val player = hangingBreakEvent.entity
        if (player is Player) {
            betterBuild.playerManager.cancelWhenBuilder(player, hangingBreakEvent)
        }
    }

    listen<HangingPlaceEvent> { hangingPlaceEvent ->
        if (hangingPlaceEvent.player == null) return@listen
        betterBuild.playerManager.cancelWhenBuilder(hangingPlaceEvent.player!!, hangingPlaceEvent)
    }

}