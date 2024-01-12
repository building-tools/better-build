package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerInteractEvent(playerInteractEvent: PlayerInteractEvent) {
        betterBuild.playerManager.cancelWhenBuilder(playerInteractEvent.player, playerInteractEvent)
    }
}