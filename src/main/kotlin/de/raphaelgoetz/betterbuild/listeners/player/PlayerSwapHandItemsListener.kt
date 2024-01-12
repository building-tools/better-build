package de.raphaelgoetz.betterbuild.listeners.player

import de.raphaelgoetz.betterbuild.BetterBuild
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

class PlayerSwapHandItemsListener(val betterBuild: BetterBuild) : Listener {

    @EventHandler
    private fun onPlayerSwapHandItemsEvent(playerSwapHandItemsEvent: PlayerSwapHandItemsEvent) {
        playerSwapHandItemsEvent.isCancelled = true
        val player = playerSwapHandItemsEvent.player
        //TODO: OPEN PLAYER MENU
    }
}