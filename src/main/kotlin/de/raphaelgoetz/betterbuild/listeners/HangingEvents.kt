package de.raphaelgoetz.betterbuild.listeners

import de.raphaelgoetz.astralis.event.listen
import de.raphaelgoetz.betterbuild.manager.cancelWhenBuilder
import org.bukkit.entity.Player
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingPlaceEvent

fun registerHangingEvents() {

    listen<HangingBreakByEntityEvent> { hangingBreakEvent ->

        val player = hangingBreakEvent.entity
        if (player is Player) {
            player.cancelWhenBuilder(hangingBreakEvent)
        }
    }

    listen<HangingPlaceEvent> { hangingPlaceEvent ->
        val player = hangingPlaceEvent.player ?: return@listen
        player.cancelWhenBuilder(hangingPlaceEvent)
    }

}