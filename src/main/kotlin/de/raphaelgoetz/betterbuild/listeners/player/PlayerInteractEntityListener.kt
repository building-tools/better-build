package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

class PlayerInteractEntityListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    fun onPlayerInteractEntityEvent(playerInteractEntityEvent: PlayerInteractEntityEvent) {
        betterBuild.playerManager.cancelWhenBuilder(playerInteractEntityEvent.player, playerInteractEntityEvent)
    }
}