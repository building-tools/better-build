package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class PlayerTeleportListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerTeleportEvent(playerTeleportEvent: PlayerTeleportEvent) {
        betterBuild.playerManager.setLastLocation(playerTeleportEvent.player, playerTeleportEvent.from)
    }
}